package com.pramati.usercommitcrawler.constants;

public class UserCommitCrawlerConstants {
	
	public static final String JSP_PATH = "/Pages/UserCommits.jsp";
	public static final int Maximum_Threads = 50;
	public static final String FILE_LOCATION = "/input/user-repourl";
	public static final String DELIMINITOR = ",";
	public static final String PUBLIC_REPO_REGEX = "a[href].mini-repo-list-item";
	public static final String COMMITED_TEXT_PAGE_FINDER_REGEX = "p.commit-title a[href]";
	public static final String TEXT_SELECTOR_REGEX = "td.blob-code-addition,.blob-code-deletion span.blob-code-inner";
	public static final String COMMIT_HISTORY_LOCATION_APPENDER = "/commits/master";
	
}
