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
<title>어드민 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
function addCompetiton(){
	var serData = $('#CompetitionForm').serialize();
	$.ajax({
        url: '${path}/addCompetition.do',
        type: 'post',
        data: serData,
        dataType: 'json',
        success: function(res) {
            alert(res.result);
        },
        error: function(xhr, status, err) {
            alert('오류가 발생했습니다: ' + err);
        }
    });
}
</script>
</head>
<body>
	  <form method="post" id="CompetitionForm">
		공모전 이름 : <input type="text" class="form-control-plaintext" name="title">
		<button type="button" class="btn btn-primary" onclick="addCompetiton()">등록</button>
		<button type="reset" class="btn btn-primary">취소</button>
	</form>
</body>
</html>