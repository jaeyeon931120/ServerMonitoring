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
    <title>500</title>
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
            <span>500</span>
        </div>
        <span class="inner-status">서버에서 에러가 발생했습니다.</span>
        <span class="inner-detail">
            서비스 사용에 불편을 끼쳐드려서 대단히 죄송합니다.
            빠른시간내에 문제를 처리하겠습니다.
        </span>
        <a href="/monitoring" class="button button-normal btn_error_home">
            <i class="fa fa-home"></i>
            &nbsp;메인으로 돌아가기
        </a>
    </div>
</div>
</body>
</html>
