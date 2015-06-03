<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<div>
	<form action="UserCommitCrawlerServlet" method="post" enctype="multipart/form-data">
		<input type="file" name = "inputFile">
		<input type = "submit" value = "upload">
		<div id = "message">${requestScope.message}</div>
	</form>
	</div>
	<div>
	<table border = 1>
		<tr>
			<th width="5%">UserName</th>
			<th width="10%">RepositoryURL</th>
			<th width="28%">Yesterdays Commit</th>
			<th width="28%">Todays Commit</th>
			<th width="28%">Last Commit</th>
		</tr>
		<c:forEach items="${requestScope.userCommitHistoryList}"
			var="userCommitHistory">
			<tr>
				<td width="5%">${userCommitHistory.userName}</td>
				<td width="10%">${userCommitHistory.repoURL}</td>
				<td width="28%">${userCommitHistory.yesterDaysCommit}</td>
				<td width="28%">${userCommitHistory.todaysCommit}</td>
				<td width="28%">${userCommitHistory.lastCommit}</td>
			</tr>

		</c:forEach>
	</table>
	</div>
</body>
</html>