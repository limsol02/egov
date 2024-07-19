<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="../top.jsp" flush="true" />
<c:set var="path" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<title>심사위원 전용 공모전</title>
<body>
<c:if test="${not empty message}">
    <div class="alert alert-error">${message}</div>
</c:if>
    <div class="container">
        <table class="table table-hover">
            <thead>
                <tr>
                    <th>공모전번호</th>
                    <th>공모전명</th>
                </tr>
            </thead>
            <tbody id="participantList">
                <c:forEach var="c" items="${clist}" varStatus="sts">
                    <tr>
                        <td>${c.competition_id}</td>
                        <td><a href="${path}/judge.do?competition_id=${c.competition_id}&judge_id=${judge_id}">${c.competition_title}</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
