<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="top.jsp" flush="true" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Main Page</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<h1 id="resultContainer"></h1>

<c:if test="${not empty message}">
    <div class="alert alert-error">${message}</div>
</c:if>
    <div class="container">
        <h2>지원자 리스트</h2>
        <p>지원자 참여명 & 파일 다운로드</p>
        <form action="partList.do" method="get">
            <div class="form-group">
                <label for="sel1">참여 공모전 선택</label>
                <select class="form-control" id="competition_id" name="competition_id" onchange="this.form.submit()">
                    <option value="0">공모전선택</option>
                    <c:forEach var="competition" items="${comList}">
                        <option value="${competition.competition_id}" 
                        <c:if test="${competition.competition_id == param.competition_id}">selected</c:if>>${competition.competition_title}</option>
                    </c:forEach>
                </select>
            </div>
        </form>

        <table class="table table-hover">
            <thead>
                <tr>
                    <th>지원번호</th>
                    <th>지원명</th>
                    <th>파일이름</th>
                </tr>
            </thead>
            <tbody id="participantList">
                <c:forEach var="participant" items="${plist}" varStatus="sts">
                    <tr>
                        <td>${participant.participant_id}</td>
                        <td>${participant.application_title}</td>
                        <td>
                            <c:choose>
                                <c:when test="${participant.file != null}">
                                    <a href="${path}/newtest/download.do?fileName=${participant.file.fname}">${participant.file.fname}</a>
                                </c:when>
                                <c:otherwise>
                                    첨부파일 없음
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
