<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-05-26
  Time: 오전 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <div class="innerheader">
        <a href="/monitoring"><img src="${pageContext.request.contextPath}/resource/image/logo.png" id="logo" alt="4st BEMS"></a>
        <div class="headerlist">
            <ul>
                <c:if test="${author eq 'ADMIN'}">
                <li>
                    <label for="setting" id="setting_label">
                        <img src="${pageContext.request.contextPath}/resource/image/gear_icon.png" alt="gear_icon"/>
                    </label>
                    <input id="setting" type="checkbox"/>
                    <div class="menu_setting">
                        <div class="menu_head setting"></div>
                        <div class="menu_body setting">
                            <ul>
                                <a onclick="popupOpen(null, null, 'user_list')">
                                    <li>사용자 관리</li>
                                </a>
                            </ul>
                        </div>
                    </div>
                </li>
                </c:if>
                <li><p id="username">${username}님 환영합니다.</p></li>
                <li><p id="clock">2023-05-31(화) 22:01:31</p></li>
                <li>
                    <a class="btn_manual_download" onclick="manual_download()">
                        <img src="${pageContext.request.contextPath}/resource/image/download.png" alt=""/>
                        <span>메뉴얼</span>
                    </a>
                </li>
                <li><a id="logout" onclick="location.href='logout'">로그아웃</a></li>
            </ul>
        </div>
    </div>
</header>
