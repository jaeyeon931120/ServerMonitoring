<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-07
  Time: 오후 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper plus">
    <div class="popup_plus">
        <div class="popup_head plus">
            <h1>서버 추가</h1>
            <a class="btn-close trigger" onclick="popupClose('plus')">x</a>
        </div>
        <div class="popup_body plus">
            <ul>
                <li>
                    <label for="system_plus">시스템</label>
                    <input type="text" id="system_plus" placeholder="시스템"/>
                    <p id="system_check_plus">시스템을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="ip_plus">IP</label>
                    <input type="text" id="ip_plus" placeholder="IP"/>
                    <p id="ip_check_plus">IP는 숫자만 사용가능하고<br>IP는 000.000.000.000형식으로 작성해야합니다</p>
                </li>
                <li>
                    <label for="server_name_plus">서버 이름</label>
                    <input type="text" id="server_name_plus" placeholder="서버 이름"/>
                    <p id="name_check_plus">서버 이름을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_port_plus">서버 포트</label>
                    <input type="text" id="server_port_plus" placeholder="서버 포트"/>
                    <p id="port_check_plus">포트는 65535이하의 숫자로 입력하셔야 됩니다.</p>
                </li>
            </ul>
            <button class="btn blue"><span>확인</span></button>
            <button class="btn red"><span>취소</span></button>
        </div>
    </div>
</div>