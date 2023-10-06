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
    <title>ERROR</title>
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
        <span class="inner-status">원인을 알 수 없는 오류가 발생했습니다.</span>
        <span class="inner-detail">
            인터넷창을 닫고 다시 한번 접속해주시기 바랍니다.
            재접속 후에도 오류가 해결되지 않을시, 사이트 관리자에게 문의해주시기 바랍니다.
        </span>
        <a href="/monitoring" class="button button-normal btn_error_home">
            <i class="fa fa-home"></i>
            &nbsp;메인으로 돌아가기
        </a>
    </div>
</div>
</body>
</html>
