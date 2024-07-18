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
<script>
// 숫자 입력 100이하 0이상으로 조정
$(document).ready(function() {
    $('input[type="number"]').on('input', function() {
        var value = parseInt($(this).val(), 10);
        if (value > 100) {
            $(this).val(100);
        } else if (value < 0) {
            $(this).val(0);
        }
    });

    // 숫자가 아닌 입력을 막기 위한 이벤트 리스너 추가
    $('#participantList').on('input', 'input[type="number"]', function(e) {
        var sanitizedValue = e.target.value.replace(/[^0-9]/g, '');
        e.target.value = sanitizedValue;
    });
});

// addScore 함수 정의
function addScore(event, form) {
    event.preventDefault();

    var formData = $(form).serializeArray();
    var data = {};

    $(formData).each(function(index, obj){
        data[obj.name] = obj.value;
    });

    $.ajax({
        url: '${path}/addSheet.do',
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(response) {
            alert(response.result);
        },
        error: function(xhr, status, error) {
            alert('등록에 실패하였습니다: ' + error);
        }
    });
}
</script>
</head>
<body>
<c:if test="${not empty message}">
    <div class="alert alert-error">${message}</div>
</c:if>
<div class="container">
    <div class="btn-group">
        <button type="button" class="btn btn-primary" id="kim" value="1">김심사(창의적)</button>
        <button type="button" class="btn btn-primary" id="choi" value="2">최심사(창의적)</button>
        <button type="button" class="btn btn-primary" id="park" value="3">박심사(창의적)</button>
        <button type="button" class="btn btn-primary" id="lee" value="4">이심사(환경보호)</button>
    </div>
    <h2 id="resultContainer"></h2>
    <p></p>            
    <table class="table table-hover">
        <thead>
            <tr>
                <th>지원번호</th>
                <th>출 품 작</th>
                <c:forEach var="evaluation_title" items="${elist}">
                    <th>${evaluation_title}</th>
                </c:forEach>
                <th>저장</th>
            </tr>
        </thead>
        <tbody id="participantList">
            <c:forEach var="participant" items="${plist}" varStatus="sts">
                <tr>
                    <form onsubmit="addScore(event, this)">
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
                        <c:forEach var="evaluation" items="${elist}" varStatus="status">
                            <td>
                                <input type="number" min="0" max="100" name="score" required/>
                                <input type="hidden" name="sheet_id" value="${slist[status.index]}"/>
                            </td>					
                        </c:forEach>
                        <td>
                            <input type="hidden" name="participant_id" value="${participant.participant_id}" />
                            <button type="submit" class="btn btn-primary">저장</button>
                        </td>
                    </form>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
