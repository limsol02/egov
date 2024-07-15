<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:set var="path" value="${pageContext.request.contextPath }" />
<fmt:requestEncoding value="utf-8" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>테스트 페이지</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
.fakeimg {
	height: 200px;
	background: #aaa;
}
</style>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#judgeSession").click(function() {
			manageSession('judge');
		});

		$("#adminSession").click(function() {
			manageSession('admin');
		});

		$("#delSession").click(function() {
			manageSession('delete');
		});

		function manageSession(action) {
			$.ajax({
				url : '${path}/session.do',
				type : 'get',
				data : {
					action : action
				},
				success : function(res) {
					alert(res);
					// f12에서 세션스토리지에서 확인하고시싶으면 주석해제
					//sessionStorage.setItem('role', response);
				},
				error : function(err) {
					console.log(err)
				}
			});
		}
	});
</script>
<body>
	<div class="jumbotron text-center" style="margin-bottom: 0">
		<h1>My First Bootstrap 4 Page</h1>
		<p>Resize this responsive page to see the effect!</p>
	</div>
	<div class="btn-group">
		<button type="button" class="btn btn-primary" id="judgeSession">심사위원
			세션</button>
		<button type="button" class="btn btn-primary" id="adminSession">관리자
			세션</button>
		<button type="button" class="btn btn-primary" id="delSession">세션
			삭제</button>
	</div>
	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<a class="navbar-brand" href="${path}/main.do">MAIN</a>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="${path}/#">링크1</a>
				</li>
				<li class="nav-item"><a class="nav-link" href="${path}/#">링크2</a>
				</li>
				<li class="nav-item"><a class="nav-link" href="${path}/#">링크3</a>
				</li>
			</ul>
		</div>
	</nav>
</body>
</html>