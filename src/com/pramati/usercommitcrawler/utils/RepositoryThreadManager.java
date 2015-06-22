package com.pramati.usercommitcrawler.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.usercommitcrawler.beans.RepositoryCommitHistory;
import com.pramati.usercommitcrawler.beans.UserCommitHistory;
import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;
import com.pramati.usercommitcrawler.mutex.CustomizedConnection;
import com.pramati.usercommitcrawler.mutex.TimeManager;
import com.sun.istack.internal.logging.Logger;

public class RepositoryThreadManager implements Callable<UserCommitHistory> {

	public static final Logger LOGGER = Logger
			.getLogger(RepositoryThreadManager.class);
	private String userName;
	private List<String> projectsCommitHistoryPage;
	ExecutorService executor = Executors
			.newFixedThreadPool(UserCommitCrawlerConstants.Maximum_Threads);
	CompletionService<RepositoryCommitHistory> completionService = new ExecutorCompletionService<RepositoryCommitHistory>(
			executor);

	RepositoryThreadManager(String userName,
			List<String> projectsCommitLocationURL) {

		this.userName = userName;
		this.projectsCommitHistoryPage = projectsCommitLocationURL;

	}

	@Override
	public UserCommitHistory call() throws InterruptedException,
			ExecutionException {

		long userThraedExecutionStartTime = TimeManager.getUserTime(Thread
				.currentThread().getId());
		long systemThraedExecutionStartTime = TimeManager.getSystemTime(Thread
				.currentThread().getId());
		UserCommitHistory userCommitHistory = new UserCommitHistory();
		try {

			HashMap<String, List<String>> pagesContainingCommittedText = getAllPagesContainingCommitTexts(projectsCommitHistoryPage);

			for (String repositoryCommitHistoryPage : projectsCommitHistoryPage) {
				completionService.submit(new ExtractCommittedTextThread(
						repositoryCommitHistoryPage,
						pagesContainingCommittedText
								.get(repositoryCommitHistoryPage)));
			}

			for (int i = 0; i < projectsCommitHistoryPage.size(); i++) {
				Future<RepositoryCommitHistory> take = completionService.take();

				try {
					RepositoryCommitHistory repositoryCommitHistory = take
							.get();
					userCommitHistory.getRepositoryCommitHistory().add(
							repositoryCommitHistory);
				} catch (InterruptedException | ExecutionException exception) {

					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.log(Level.SEVERE,
								"Severe Exception has occured", exception);
					}
				}
			}

		} catch (SocketTimeoutException socketTimeoutException) {

			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "SocketTimeoutException has occured");
			}
		}

		catch (IOException exception) {

			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "IOException has occured", exception);
			}
		}

		// TimeManager.addTime(Thread.currentThread().getId());
		long userThraedExecutionEndTime = TimeManager.getUserTime(Thread
				.currentThread().getId());
		long systemThraedExecutionEndTime = TimeManager.getSystemTime(Thread
				.currentThread().getId());
		TimeManager.addUserTime(userThraedExecutionEndTime - userThraedExecutionStartTime);
		TimeManager.addSystemTime(systemThraedExecutionEndTime - systemThraedExecutionStartTime);
		userCommitHistory.setUserName(userName);
		System.out
				.println("This thread should take less time."
						+ ((systemThraedExecutionEndTime - systemThraedExecutionStartTime) + (userThraedExecutionEndTime - userThraedExecutionStartTime)));
		return userCommitHistory;

	}

	public HashMap<String, List<String>> getAllPagesContainingCommitTexts(
			List<String> projectsCommitHistoryPage) throws IOException {

		HashMap<String, List<String>> pagesContainingCommittedTextMap = new HashMap<String, List<String>>();

		for (String commitHistoryPage : projectsCommitHistoryPage) {

			pagesContainingCommittedTextMap.put(commitHistoryPage,
					getPagesContainingCommittedText(commitHistoryPage));
		}

		return pagesContainingCommittedTextMap;
	}

	public List<String> getPagesContainingCommittedText(String url)
			throws IOException {

		List<String> pagesContainingCommittedText = new ArrayList<String>();
		try {

			CustomizedConnection customizedConnection = new CustomizedConnection();
			Connection connect = customizedConnection.makeConnection(url);
			Document document = connect.get();
			Elements pages = document
					.select(UserCommitCrawlerConstants.COMMITED_TEXT_PAGE_FINDER_REGEX);

			for (Element page : pages) {
				pagesContainingCommittedText.add(page.attr("abs:href"));
			}
		} catch (SocketTimeoutException socketTimeoutException) {

			if (LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.log(Level.WARNING, " Could not connect to URL url "
						+ url);
			}
		}
		return pagesContainingCommittedText;
	}

}
