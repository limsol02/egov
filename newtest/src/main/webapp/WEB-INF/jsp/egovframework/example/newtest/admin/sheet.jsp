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
function addSheet(){
	var serData = $('#Form').serialize();
	$.ajax({
        url: '${path}/addSheet.do',
        type: 'post',
        data: serData,
        dataType: 'json',
        success: function(response) {
            alert(response.message);
        },
        error: function(xhr, status, err) {
            alert('오류가 발생했습니다: ' + err);
        }
    });
}
</script>
</head>
<body>

	<form id="SheetForm">
        <label for="competitionSelect">공모전 선택하기:</label>
        <select id="competitionSelect">
            <option value="">선택해주세요...</option>
            <c:forEach items="${list}" var="competition">
                <option value="${competition.competition_id}">
                    ${competition.competition_title}
                </option>
            </c:forEach>
        </select>
        
        <label for="evaluationSelect">평가항목 선택하기:</label>
        <select id="evaluationSelect">
            <option value="">선택해주세요...</option>
            <c:forEach items="${item}" var="Items">
                <option value="${Items.evaluation_id}">
                    ${Items.evaluation_items}
                </option>
            </c:forEach>
        </select>
        
        <button type="button" class="btn btn-primary" onclick="addSheet()">등록</button>
     </form>
       
</body>
</html>