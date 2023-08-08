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
    <title>로그인</title>
    <link rel='stylesheet' type='text/css' href="${pageContext.request.contextPath}/resource/css/monitoring.css">
</head>
<body class="login_bg">
<div class="login_box">
    <img src="${pageContext.request.contextPath}/resource/image/login_box.jpg" alt="" />
    <form class="login_form" action="/auth" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <img src="${pageContext.request.contextPath}/resource/image/kevin_logo.jpg" alt=""/>
        <label for="id"></label>
        <input type="text" id="id" name="id" placeholder="아이디" required="" autofocus="">
        <label for="password"></label>
        <input type="password" id="password" name="password" placeholder="비밀번호" required="">
        <span>
            <c:if test="${error}">
                <p id="danger">${exception}</p>
            </c:if>
        </span>
        <button class="loginBtn" type="submit">로그인</button>
    </form>
</div>
<div class="login_foot">
    <p>Copyright 2023 KevinLAB. All rights reserved.</p>
</div>
</body>
</html>
