<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-05-23
  Time: 오후 3:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>INVALID</title>
    <link rel="stylesheet" type='text/css' href="${pageContext.request.contextPath}/resource/bootstrap-5.0.2-dist/css/bootstrap.css">
    <link rel='stylesheet' type='text/css' href="${pageContext.request.contextPath}/resource/css/monitoring.css">
    <link rel='stylesheet' type='text/css' href="${pageContext.request.contextPath}/resource/css/button.css">
    <link rel='stylesheet' type='text/css' href="${pageContext.request.contextPath}/resource/css/error.css">
    <link rel="favicon" href="${pageContext.request.contextPath}/resource/image/favicon.ico"/>
</head>
<body class="login_bg">
<div class="error_box">
    <div class="inner">
        <!--BEGIN CONTENT-->
        <div class="inner-circle">
            <i class="fa fa-home"></i>
            <span>ERROR</span>
        </div>
        <span class="inner-status">세션이 유효하지 않습니다.</span>
        <span class="inner-detail">
            세션이 유효하지 않습니다.새로 로그인 해주시기 바랍니다.
        </span>
        <a href="/login" class="button button-normal btn_error_home">
            <i class="fa fa-home"></i>
            &nbsp;로그인창으로 돌아가기
        </a>
    </div>
</div>
</body>
</html>