<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="../top.jsp" flush="true" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>심사위원 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
<c:if test="${not empty message}">
    <div class="alert alert-error">${message}</div>
</c:if>
<div class="container">
  <h2>떙땡 공모전</h2>
  <p></p>            
  <table class="table table-hover">
    <thead>
      <tr>
        <th>지원번호</th>
        <th>출 품 작</th>
        <c:forEach var="evaluation_title" items="${elist}">
            <th>${evaluation_title}</th>
        </c:forEach>
      </tr>
    </thead>
    <tbody id="participantList">
   		<c:forEach var="participant" items="${plist}" varStatus="sts">
			<tr>
            	<td>${participant.participant_id}</td>
				<td>
					<c:choose>
						<c:when test="${participant.file != null}">
							<a href="${path}/newtest/download.do?fileName=${participant.file.fname}">${participant.application_title}</a>
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