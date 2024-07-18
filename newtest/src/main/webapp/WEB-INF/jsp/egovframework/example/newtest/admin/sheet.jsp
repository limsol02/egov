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
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<title>어드민 페이지</title>
<script>
$(document).ready(function() {
    function addSheet() {
        const selectedEvaluations = [];
        $('input[name="evaluationSelect"]:checked').each(function() {
            selectedEvaluations.push($(this).val());
        });

        const formData = new FormData();
        formData.append('competition_id', $('#competitionSelect').val());
        selectedEvaluations.forEach(id => formData.append('evaluation_ids', id));

        // Use AJAX to send the data to the server
        $.ajax({
            url: '${path}/addSheet.do',
            type: 'POST',
            data: formData,
            dataType: 'json'
            contentType: false,
            processData: false,
            success: function(res) {
            	alert(res.result);
                // Handle the response from the server if needed
            },
            error: function(xhr, status, error) {
                alert('등록에 실패하였습니다: ' + error);
                // Handle the error if needed
            }
        });
    }

    // Attach the addSheet function to the button click event
    $('.btn-success').click(function() {
        addSheet();
    });
});
</script>
</head>
<body>

<form id="SheetForm">
    <label for="competitionSelect">공모전 선택하기:</label>
    <select id="competitionSelect" class="form-control">
        <option value="">선택해주세요...</option>
        <c:forEach items="${list}" var="competition">
            <option value="${competition.competition_id}">
                ${competition.competition_title}
            </option>
        </c:forEach>
    </select>
    
    <label>평가항목 선택하기:</label>
    <div>
        <c:forEach items="${item}" var="Items">
            <div class="form-check">
                <input type="checkbox" class="form-check-input" id="evaluation_${Items.evaluation_id}" name="evaluationSelect" value="${Items.evaluation_id}">
                <label class="form-check-label" for="evaluation_${Items.evaluation_id}">
                    ${Items.evaluation_items}
                </label>
            </div>
        </c:forEach>
    </div>
    <button type="button" class="btn btn-success">등록하기</button>
</form>

</body>
</html>
