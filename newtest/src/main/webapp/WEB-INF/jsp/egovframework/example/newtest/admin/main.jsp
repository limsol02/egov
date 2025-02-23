<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath }" />
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
<title>어드민 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-sm-4">
      <h3><a href="${path}/competition.do">공모전 등록하기</a></h3>
    </div>
    <div class="col-sm-4">
      <h3>공모전 참여현황</h3>
      <p>Lorem ipsum dolor..</p>
    </div>
    <div class="col-sm-4">
      <h3>지원자 등록하기</h3>
      <p>Lorem ipsum dolor..</p>
    </div>
    <div class="col-sm-4">
      	<h3><a href="${path}/evaluation.do">평가 항목 등록하기</a></h3>
   	 </div>
    <div class="col-sm-4">
      <h3><a href="${path}/sheet.do">평가지 만들기</a>평가지 만들기</h3>
    </div>
  </div>
</div>
</body>
</html>