package com.pramati.usercommitcrawler.utils;

import java.io.IOException;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pramati.usercommitcrawler.beans.UserCommitHistory;
import com.pramati.usercommitcrawler.beans.UserInformation;
import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;

public class RepositoryCrawler {

	private UserProjects userProjects = new UserProjects();
	private UserThreadManager threadManager = new UserThreadManager();

	public List<UserCommitHistory> getUsersCommitHistory(StringBuilder fileInput)
			throws IOException {

		UserInformation userInformation = getUserInformation(fileInput);

		ConcurrentHashMap<String, String> userRepositoryURLMap = userInformation
				.getUserRepositoryURLMap();

		Queue<String> userNameQ = userInformation.getUserNameQ();

		ConcurrentHashMap<String, List<String>> userProjectstMap = userProjects
				.findUsersPublicProjects(userRepositoryURLMap);
		List<UserCommitHistory> userCommitHistoryList = threadManager
				.manageThreads(userNameQ, userProjectstMap);

		return userCommitHistoryList;
	}

	public UserInformation getUserInformation(StringBuilder fileInput) {

		long startNanoTime = System.nanoTime();
		UserInformation userInformation = new UserInformation();
		ConcurrentHashMap<String, String> userRepositoryURLMap = new ConcurrentHashMap<String, String>();
		Queue<String> userNameQ = new ConcurrentLinkedQueue<String>();

		String lines[] = fileInput.toString().split("\\r?\\n");
		for (String line : lines) {
			if (line != null && line.length() > 1) {
				String[] nameURL = line
						.split(UserCommitCrawlerConstants.DELIMINITOR);
				userRepositoryURLMap.put(nameURL[0].trim(), nameURL[1].trim());
				userNameQ.offer(nameURL[0].trim());
			}
		}

		userInformation.setUserNameQ(userNameQ);
		userInformation.setUserRepositoryURLMap(userRepositoryURLMap);
		long endNanoTime = System.nanoTime();
		System.out.println("user info " + (endNanoTime - startNanoTime)/1000000);
		return userInformation;
	}

	public StringBuilder readMultiPartRequest(HttpServletRequest request)
			throws FileUploadException {

		long startNanoTime = System.nanoTime();
		StringBuilder fileInput = new StringBuilder();
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory())
				.parseRequest(request);

		for (FileItem item : items) {
			fileInput.append(item.getString());
		}
		
		long endNanoTime = System.nanoTime();
		
		System.out.println("MultiPartExecutionTime " + (endNanoTime - startNanoTime)/1000000);

		return fileInput;
	}

}
