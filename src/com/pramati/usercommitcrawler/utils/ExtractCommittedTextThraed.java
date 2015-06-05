package com.pramati.usercommitcrawler.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.usercommitcrawler.beans.RepositoryCommitHistory;
import com.pramati.usercommitcrawler.beans.UserCommitHistory;
import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;
import com.sun.istack.internal.logging.Logger;

public class ExtractCommittedTextThraed implements Callable<UserCommitHistory> {

	public static final Logger LOGGER = Logger
			.getLogger(ExtractCommittedTextThraed.class);

	private String userName;
	private String lastCommitPage;
	List<String> projectsCommitHistoryPage;

	ExtractCommittedTextThraed(String userName,
			List<String> projectsCommitLocationURL) {

		this.userName = userName;
		this.projectsCommitHistoryPage = projectsCommitLocationURL;

	}

	@Override
	public UserCommitHistory call() {

		UserCommitHistory userCommitHistory = new UserCommitHistory();
		try {

			HashMap<String, List<String>> pagesContainingCommittedText = getAllPagesContainingCommitTexts(projectsCommitHistoryPage);
			userCommitHistory = extractCommittedText(projectsCommitHistoryPage,
					pagesContainingCommittedText);
			/*
			 * appendLatestCommitText(userCommitHistory, lastCommitPage);
			 * userCommitHistory.setUserName(userName);
			 */

		} catch (IOException exception) {

			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "IOException has occured", exception);
			}
		}
		return userCommitHistory;

	}

	public void appendLatestCommitText(UserCommitHistory userCommitHistory,
			String lastCommitURL) throws IOException {
		if (!lastCommitURL.equals(null)) {
			try {
				Document document = Jsoup.connect(lastCommitURL).get();
				Elements committedTexts = document
						.select(UserCommitCrawlerConstants.TEXT_SELECTOR_REGEX);
				StringBuilder commitTextStringBulider = new StringBuilder();
				for (Element committedText : committedTexts) {
					String text = committedText.text();
					text = text.replace("+", "\n").replace("<", "");
					commitTextStringBulider.append(text);
				}

			} catch (SocketTimeoutException socketTimeoutException) {
				if (LOGGER.isLoggable(Level.WARNING)) {
					LOGGER.log(Level.WARNING,
							"Could not append the recent commit");
				}
			}
		}

	}

	public UserCommitHistory extractCommittedText(
			List<String> projectsCommitHistoryPage,
			HashMap<String, List<String>> pagesContainingCommittedTextMap)
			throws IOException {

		UserCommitHistory userCommitHistory = new UserCommitHistory();

		for (String commitHistoryPage : projectsCommitHistoryPage) {
			RepositoryCommitHistory repositoryCommitHistory = new RepositoryCommitHistory();
			repositoryCommitHistory.setRepoSitoryName(commitHistoryPage);
			List<String> pagesContainingCommittedText = pagesContainingCommittedTextMap
					.get(commitHistoryPage);
			for (String pageContainingCommittedText : pagesContainingCommittedText) {
				Document documnent = Jsoup.connect(pageContainingCommittedText)
						.get();
				String date = documnent.select("time").attr("datetime");

				DateTime committedPagesdDateTime = new DateTime(date);
				DateTime today = DateTime.now();
				DateTime yesterday = DateTime.now().minusDays(1);

				if (committedPagesdDateTime.getYear() == today.getYear()
						&& committedPagesdDateTime.getMonthOfYear() == today
								.getMonthOfYear()
						&& committedPagesdDateTime.getDayOfMonth() == today
								.getDayOfMonth()) {
					appendCommitText(repositoryCommitHistory,
							CommitHistoryFields.TODAY,
							pageContainingCommittedText);
				}

				else if (committedPagesdDateTime.getYear() == yesterday
						.getYear()
						&& committedPagesdDateTime.getMonthOfYear() == yesterday
								.getMonthOfYear()
						&& committedPagesdDateTime.getDayOfMonth() == yesterday
								.getDayOfMonth()) {
					appendCommitText(repositoryCommitHistory,
							CommitHistoryFields.YESTERDAY,
							pageContainingCommittedText);
				}

				else {

					appendCommitText(repositoryCommitHistory,
							CommitHistoryFields.DAYS_BEFORE_YESTERDAY,
							pageContainingCommittedText);
				}
			}
			userCommitHistory.getRepositoryCommitHistory().add(
					repositoryCommitHistory);
		}

		userCommitHistory.setUserName(userName);
		return userCommitHistory;
	}

	public void appendCommitText(
			RepositoryCommitHistory repositoryCommitHistory,
			CommitHistoryFields fieldValue, String url) throws IOException {

		try {
			Document document = Jsoup.connect(url).get();
			Elements commitTexts = document
					.select(UserCommitCrawlerConstants.TEXT_SELECTOR_REGEX);
			StringBuilder commitTextStringBuilder = new StringBuilder();
			for (Element committedText : commitTexts) {
				String text = committedText.text();
				text = text.replace("+", "").replace("<", "<'")
						.replace(";", "<BR>");
				commitTextStringBuilder.append(text);
			}

			switch (fieldValue) {
			case YESTERDAY:
				// repositoryCommitHistory.setYesterDaysCommit(commitTextStringBuilder);
				repositoryCommitHistory.getYesterdaysCommit().append(
						commitTextStringBuilder);
				break;
			case TODAY:
				// userCommitHistory.setTodaysCommit(commitTextStringBuilder);
				repositoryCommitHistory.getTodaysCommit().append(
						commitTextStringBuilder);
				break;
			case DAYS_BEFORE_YESTERDAY:
				repositoryCommitHistory.getDayBeforeYesrdaysCommit().append(
						commitTextStringBuilder);
				break;
			}

		} catch (SocketTimeoutException exception) {
			if (LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.log(Level.WARNING,
						"SocketTimeOut Exception has occured. ");
			}
		}

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
			Document document = Jsoup.connect(url).get();
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
