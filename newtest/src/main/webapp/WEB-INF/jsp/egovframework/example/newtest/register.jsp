<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="top.jsp" flush="true" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Main Page</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">
	function delData(id) {
		$.ajax({
			url : "${path}/newtest/delPart.do?participant_id=" + id,
			type : 'get',
			success : function(res) {
				console.log(res)
				if (res.result != '') {
					alert("삭제완료");
					location.href = "${path}/newtest/register.do";
				} else {
					alert("삭제 에러");
				}
			},
			error : function(err) {
				console.log(err)
				console.log(err.responseJSON ? err.responseJSON.error
						: err.responseText);
			}
		});
	}
	// 업로드 및 url 매핑
	     function uploadFiles() {
            var form = $('#frm02')[0];
            var data = new FormData(form);

            $.ajax({
                url: '/newtest/uploadFiles.do',
                type: 'POST',
                enctype: 'multipart/form-data',
                data: data,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (response) {
                    alert('파일 업로드 성공');
                    var uploadedFilesDiv = $('#uploadedFiles');
                    uploadedFilesDiv.empty();
                    $.each(response, function(filename, url) {
                        uploadedFilesDiv.append('<p><a href="' + url + '" target="_blank">' + filename + '</a></p>');
                        $('#fileURL').val(url); // 업로드된 파일 URL을 input 필드에 표시
                    });
                },
                error: function (err) {
                    alert('파일 업로드 실패');
                    console.log('에러: ', err.responseText);
                }
            });
        }
</script>
<body>
	<div class="container">
		<h2>심사명단 등록</h2>
		<p>파일 URL 매핑</p>
		<form action="register.do" method="get" id="frm01">
			<div class="form-group">
				<label for="sel1">참여 공모전 선택</label> <select class="form-control"
					id="competition_id" name="competition_id"
					onchange="this.form.submit()">
					<option value="0">공모전선택</option>
					<c:forEach var="competition" items="${comList}">
						<option value="${competition.competition_id}"
							<c:if test="${competition.competition_id == param.competition_id}">selected</c:if>>${competition.competition_title}</option>
					</c:forEach>
				</select>
			</div>
		</form>
		<!-- 파일업로드 후 매핑 -->
		<form id="frm02" enctype="multipart/form-data">
            <input type="file" class="form-control" id="files" name="files" multiple>
            <button type="button" class="btn btn-primary" onclick="uploadFiles()">업로드</button>
        </form>
        <input type="text" id="fileURL" class="form-control" readonly>
        <div id="uploadedFiles"></div>
        <!--여기까지 -->
		<table class="table table-hover">
			<thead>
				<tr>
					<th>지원번호</th>
					<th>지원명</th>
					<th>지원공모전명</th>
					<!-- <th>파일업로드</th> -->
					<th></th>
				</tr>
			</thead>
			<tbody id="participantList">
				<c:forEach var="participant" items="${plist}" varStatus="sts">
					<tr>
						<td>${participant.participant_id}</td>
						<td><input type="text"
							value="${participant.application_title}"></td>
						<td><input type="text"
							value="${participant.competitionTitle}"></td>
						<!-- <td><input type="file" class="form-control" id="files" name="files" multiple></td> -->
						<td><p class="text-danger"
								onclick='delData("${participant.participant_id}")'>삭제</p></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
</body>
</html>
