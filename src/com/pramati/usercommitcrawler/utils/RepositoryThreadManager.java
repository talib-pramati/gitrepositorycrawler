package com.pramati.usercommitcrawler.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import com.sun.istack.internal.logging.Logger;

public class RepositoryThreadManager implements Callable<UserCommitHistory> {

	public static final Logger LOGGER = Logger
			.getLogger(RepositoryThreadManager.class);
	private String userName;
	private List<String> projectsCommitHistoryPage;
	ExecutorService executor = Executors
			.newFixedThreadPool(UserCommitCrawlerConstants.Maximum_Threads);

	RepositoryThreadManager(String userName,
			List<String> projectsCommitLocationURL) {

		this.userName = userName;
		this.projectsCommitHistoryPage = projectsCommitLocationURL;

	}

	@Override
	public UserCommitHistory call() {

		UserCommitHistory userCommitHistory = new UserCommitHistory();
		try {

			HashMap<String, List<String>> pagesContainingCommittedText = getAllPagesContainingCommitTexts(projectsCommitHistoryPage);
			List<Callable<RepositoryCommitHistory>> repositoryCommitHistoryCallable = new ArrayList<Callable<RepositoryCommitHistory>>();
			List<Future<RepositoryCommitHistory>> repositoryCommitHistoryFuture = new ArrayList<Future<RepositoryCommitHistory>>();

			for (String repositoryCommitHistoryPage : projectsCommitHistoryPage) {
				repositoryCommitHistoryCallable
						.add(new ExtractCommittedTextThread(
								repositoryCommitHistoryPage,
								pagesContainingCommittedText
										.get(repositoryCommitHistoryPage)));
			}

			try {
				repositoryCommitHistoryFuture = executor
						.invokeAll(repositoryCommitHistoryCallable);
			} catch (InterruptedException exception) {

				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.log(Level.SEVERE, "Severe Exception has occured",
							exception);
				}

			}

			for (Future<RepositoryCommitHistory> future : repositoryCommitHistoryFuture) {

				try {
					RepositoryCommitHistory repositoryCommitHistory = future
							.get();
					// addNetworkTime(repositoryCommitHistory.getNetworkWatitingTime());
					userCommitHistory.getRepositoryCommitHistory().add(
							repositoryCommitHistory);
				} catch (InterruptedException | ExecutionException exception) {

					if (LOGGER.isLoggable(Level.SEVERE)) {
						LOGGER.log(Level.SEVERE,
								"Severe Exception has occured", exception);
					}
				}
			}

		} catch (IOException exception) {

			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "IOException has occured", exception);
			}
		}
		userCommitHistory.setUserName(userName);
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

			CustomizedConnection customizedConnection = CustomizedConnection
					.getInstance();
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
