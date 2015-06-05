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
		<form action="UserCommitCrawlerServlet" method="post"
			enctype="multipart/form-data">
			<input type="file" name="inputFile"> <input type="submit"
				value="upload">
			<div id="message">${requestScope.message}</div>
		</form>
	</div>
	<c:if test="${not empty requestScope.userCommitHistoryList}">
	<div>

		<table border=1>
			<thead>
				<tr>
					<th>User Name</th>
					<th>Commit Description</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${requestScope.userCommitHistoryList}"
					var="userCommitHistory">
					<tr>
						<td>${userCommitHistory.userName}</td>
						<td>
							<table border=1>
								<thead>
									<tr>
										<th width="28%">repository name</th>
										<th width="28%">days before yesterdays commit</th>
										<th width="28%">yesterdays commit</th>
										<th width="28%">todays commit</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userCommitHistory.repositoryCommitHistory}"
										var="repoCommitHistory">
										<tr>
											<td width="28%">${repoCommitHistory.repoSitoryName}</td>
											<td width="28%">${repoCommitHistory.dayBeforeYesrdaysCommit}</td>
											<td width="28%">${repoCommitHistory.yesterdaysCommit}</td>
											<td width="28%">${repoCommitHistory.todaysCommit}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</c:if>
</body>
</html>