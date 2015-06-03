package com.pramati.usercommitcrawler.filemanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;
import com.sun.istack.internal.logging.Logger;

public class FileManager {

	public static final Logger LOGGER = Logger.getLogger(FileManager.class);
	public Queue<String> userNamesQueue = new ConcurrentLinkedQueue<String>();

	public ConcurrentHashMap<String, String> readFile() {
		ConcurrentHashMap<String, String> userRepoURLMap = new ConcurrentHashMap<String, String>();
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(
				UserCommitCrawlerConstants.FILE_LOCATION))) {

			String currentLine;
			while ((currentLine = bufferReader.readLine()) != null) {
				String[] nameURLArray = currentLine
						.split(UserCommitCrawlerConstants.DELIMINITOR);
				if (nameURLArray.length > 1) {
					userRepoURLMap.put(nameURLArray[0], nameURLArray[1]);
					userNamesQueue.offer(nameURLArray[0]);
				}

			}

		} catch (Exception exception) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, "Unable to read the File "
						+ UserCommitCrawlerConstants.FILE_LOCATION, exception);
			}
		}

		return userRepoURLMap;
	}

	public Queue<String> getUserNamesQueue() {
		return userNamesQueue;
	}

}
