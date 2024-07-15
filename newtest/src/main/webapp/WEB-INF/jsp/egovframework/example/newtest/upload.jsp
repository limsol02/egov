<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="top.jsp" flush="true" />
<!DOCTYPE html>
<html lang="en">
<script type="text/javascript">
	$(document).ready(function() {
		$("#insBtn").click(function() {
			var title = $("#application_title").val();
			alert($("#competition_id").val());
			if (title != null) {
				$("#frm1").submit();
			} else {
				alert("폼을 작성해주세요");
			}
			;

		})

	})
</script>
<head>
<meta charset="UTF-8">
<title>지원페이지</title>
</head>
<body>

<c:if test="${not empty message}">
    <div class="alert alert-success">${message}</div>
</c:if>

	<form id="frm1" action="${path}/newtest/insPart.do" method="post" enctype="multipart/form-data">

		<div class="form-group">
			<label for="usr">지원명 :</label> <input type="text"
				class="form-control" id="application_title" name="application_title">
		</div>
		<div class="form-group">
			<label for="sel1">참여 공모전 선택</label> 
			<select class="form-control" id="competition_id" name="competition_id">
				<c:forEach var="competition" items="${comList}">
					<option value="${competition.competition_id}">${competition.competition_title}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label for="files">파일 첨부</label> <input type="file"
				class="form-control" id="files" name="files" multiple>
		</div>

		<button type="button" class="btn btn-outline-primary" id="insBtn">제출하기</button>
	</form>
</body>
</html>