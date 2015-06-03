package com.pramati.usercommitcrawler.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.usercommitcrawler.constants.UserCommitCrawlerConstants;
import com.sun.istack.internal.logging.Logger;

public class UserProjects {
	
	public static final Logger LOGGER = Logger.getLogger(UserProjects.class);
	public ConcurrentHashMap<String, List<String>> findUsersPublicProjects( ConcurrentHashMap<String, String> userRepositoryURLMap) throws IOException
	{
		ConcurrentHashMap<String, List<String>> userProjsCommitHistoryPage = new ConcurrentHashMap<String, List<String>>();
		Set<String> userNames = userRepositoryURLMap.keySet();
				
		for(String userName : userNames)
		{
			String userRepoURL = userRepositoryURLMap.get(userName);
			userProjsCommitHistoryPage.put(userName, getUsersPublicProjsCommitHistoryPage(userRepoURL));
		}
		
		return userProjsCommitHistoryPage;
	}

	public List<String> getUsersPublicProjsCommitHistoryPage(String userRepoURL) throws IOException {
	
		List<String> userPublicProjCommitHistoryPage = new ArrayList<String>();
		try{
			
			Document doc = Jsoup.connect(userRepoURL).get();
			Elements repoLinks = doc.select(UserCommitCrawlerConstants.PUBLIC_REPO_REGEX);
			
			for(Element repolink: repoLinks)
			{
				userPublicProjCommitHistoryPage.add(repolink.attr("abs:href") + UserCommitCrawlerConstants.COMMIT_HISTORY_LOCATION_APPENDER);
			}
		}
		catch(SocketTimeoutException socketTimeoutException)
		{
			if(LOGGER.isLoggable(Level.SEVERE))
			{
				LOGGER.log(Level.WARNING, "Could not connect to url " + userRepoURL);
			}
		}
		
		return userPublicProjCommitHistoryPage;
	}

}
