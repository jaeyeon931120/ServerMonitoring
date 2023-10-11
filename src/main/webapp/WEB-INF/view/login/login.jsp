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
    <link rel="favicon" href="${pageContext.request.contextPath}/resource/image/favicon.ico"/>
</head>
<script>
    window.onload = function () {
        passwordView().then();

        /* 비밀번호 보이게하는 이벤트 */
        async function passwordView() {
            const eye_view = document.querySelector('.fa-eye')
            let input = document.getElementById('password');

            await eye_view.addEventListener('click', function () {
                if(input.className === 'active') {
                    eye_view.className = 'login fa-eye';
                    input.type = 'password';
                    input.className = '';
                } else {
                    eye_view.className = 'login fa-eye-blur';
                    input.type = 'text';
                    input.className = 'active';
                }
            })
        }
    }
</script>
<body class="login_bg">
<div class="login_box">
    <img src="${pageContext.request.contextPath}/resource/image/login_box.jpg" alt="" />
    <form class="login_form" action="/auth" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <img src="${pageContext.request.contextPath}/resource/image/kevin_logo.jpg" alt=""/>
        <input type="text" id="id" name="id" placeholder="아이디" required="" autofocus="">
        <div class="login_pw">
            <input type="password" id="password" name="password" placeholder="비밀번호" required="">
            <i class="login fa-eye"></i>
        </div>
        <span class="error_text">
            <c:if test="${error}">
                <p class="danger">${exception}</p>
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
