package com.pramati.usercommitcrawler.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import com.pramati.usercommitcrawler.beans.UserCommitHistory;
import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;
import com.sun.istack.internal.logging.Logger;

public class UserCommitCrawlerThreadManager {

	public static final Logger LOGGER = Logger
			.getLogger(UserCommitCrawlerThreadManager.class);
	private ExecutorService executor = Executors
			.newFixedThreadPool(UserCommitCrawlerConstants.Maximum_Threads);

	public List<UserCommitHistory>  manageThreads(Queue<String> nameQ,
			ConcurrentHashMap<String, List<String>> userProjectsMap) {

		List<UserCommitHistory> userCommitHistoryList = new ArrayList<UserCommitHistory>();
		List<Callable<UserCommitHistory>> userCommitHistoryCallables = new ArrayList<Callable<UserCommitHistory>>();
		List<Future<UserCommitHistory>> userCommitHistoryfutures = new ArrayList<Future<UserCommitHistory>>();
		
		while (!nameQ.isEmpty()) {
			String userName = nameQ.poll();
			List<String> projectsCommitHistoryPage = userProjectsMap
					.get(userName);
			userCommitHistoryCallables.add(new ExtractCommittedTextThraed(
					userName, projectsCommitHistoryPage));
		}
		try {
			userCommitHistoryfutures = executor
					.invokeAll(userCommitHistoryCallables);
		} catch (InterruptedException exception) {

			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "Severe Exception has occured",
						exception);
			}

		}

		for (Future<UserCommitHistory> commitHistoryFuture : userCommitHistoryfutures) {

			try {
				userCommitHistoryList.add(commitHistoryFuture.get());
			} catch (InterruptedException | ExecutionException exception) {

				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.log(Level.SEVERE, "Severe Exception has occured",
							exception);
				}
			}

		}
		return userCommitHistoryList;
	}
}
