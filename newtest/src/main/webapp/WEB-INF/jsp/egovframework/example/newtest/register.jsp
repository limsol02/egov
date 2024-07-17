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
        dataType: 'json',
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
            console.log(err.responseJSON ? err.responseJSON.error : err.responseText);
        }
    });
}
function uploadFiles(participantId) {
    var fileInput = document.getElementById('file-' + participantId);
    var formData = new FormData();
    formData.append('files', fileInput.files[0]);

    $.ajax({
        url: '${path}/newtest/fileUpload.do',
        type: 'POST',
        enctype: 'multipart/form-data',
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        dataType: 'json',
        success: function(response) {
            alert('파일 업로드 성공');
            console.log(response); // JSON 응답 형태 확인

            if (response.uploaded) {
                var viewLink = document.getElementById('view-' + participantId);
                viewLink.href = response.url;
                viewLink.textContent = response.fileName;
            }
        },
        error: function(err) {
            alert('파일 업로드 실패');
            console.log('에러: ', err.responseText);
        }
    });
}

    // openNewWindow 함수에서 인코딩된 URL을 사용하여 viewFile.do를 호출
    function openNewWindow(fileUrl) {
        window.open(fileUrl, '_blank');
    }
    
    function base64UrlEncode(str) {
        return btoa(unescape(encodeURIComponent(str)))
            .replace(/\+/g, '-')
            .replace(/\//g, '_')
            .replace(/=+$/, '');
    }

    function generateURL(participantId) {
        var fileName = document.getElementById('file-' + participantId).value;
        if (!fileName) {
            alert('파일 이름이 없습니다.');
            return;
        }

        // URL 안전한 Base64 인코딩
        var encodedFileName = base64UrlEncode(fileName);
        var fileUrl = '${path}/newtest/viewFile.do?filename=' + encodedFileName;
        
        var viewLink = document.getElementById('view-' + participantId);
        viewLink.href = fileUrl;
        viewLink.textContent = fileName;
		
        console.log("아이디 : "+participantId)
        console.log("URL : "+fileUrl)
        
        var formdata = new FormData();
        formdata.append("participant_id",participantId)
         formdata.append("file_url", fileUrl);
        
        console.log(formdata)
        // 파일 URL을 서버로 전송하여 저장
        $.ajax({
            url: '${path}/newtest/savefileurl.do',
            type: 'POST',
            processData: false, 
            contentType: false,
            dataType:'json',
            data: formdata,
            success: function(response) {
                console.log('파일 URL 저장 성공');
            },
            error: function(err) {
                console.log('파일 URL 저장 실패: ', err.responseText);
            }
        });
    }
</script>

<body>
	<div class="container" style="margin: 2%; width: 100%; max-width: 95%;">
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
		<!--   <form id="frm02" enctype="multipart/form-data">
            <input type="file" class="form-control" id="files" name="files" multiple>
            <button type="button" class="btn btn-primary" onclick="uploadFiles()">업로드</button>
        </form>
        <input type="text" id="fileURL" class="form-control" readonly>
        <div id="uploadedFiles"></div> -->
		<!--여기까지 -->
		<table class="table table-hover">
			<thead>
				<tr>
					<th>지원번호</th>
					<th>지원명</th>
					<th>지원공모전명</th>
					<th>파일첨부</th>
					<th>업로드 버튼</th>
					<th>뷰어/다운</th>
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
						<td><input type="text" class="form-control"
							value="${participant.file.fname}" readonly
							id="file-${participant.participant_id}" name="files"></td>
						<td><button type="button" class="btn btn-primary"
								onclick="generateURL(${participant.participant_id})">URL
								생성</button></td>

						<td><a href="#" id="view-${participant.participant_id}"
							target="_blank">뷰어/다운</a></td>
						<td><p class="text-danger"
								onclick='delData("${participant.participant_id}")'>삭제</p></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	
</body>
</html>
