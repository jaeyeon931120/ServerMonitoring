<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-08
  Time: 오후 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper server_delete">
    <div class="popup server_delete">
        <div class="popup_head server_delete">
            <h1>서버 삭제</h1>
            <a class="btn-close trigger" onclick="popupClose('server_delete')">x</a>
        </div>
        <div class="popup_body server_delete">
            <ul>
                <li>
                    <label for="server_delete_system">시스템</label>
                    <input type="text" id="server_delete_system" placeholder="시스템"/>
                    <p id="system_check_server_delete">시스템을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_delete_ip">IP</label>
                    <input type="text" id="server_delete_ip" placeholder="IP"/>
                    <p id="ip_check_server_delete">IP는 숫자만 사용가능하고<br>IP는 000.000.000.000형식으로 작성해야합니다.</p>
                </li>
                <li>
                    <label for="server_delete_server_name">서버 이름</label>
                    <input type="text" id="server_delete_server_name" placeholder="서버 이름"/>
                    <p id="server_name_check_server_delete">서버 이름을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_delete_server_port">서버 포트</label>
                    <input type="text" id="server_delete_server_port" placeholder="서버 포트"/>
                    <p id="server_port_check_server_delete">포트번호는 숫자로 작성해야합니다.</p>
                </li>
                <li>
                    <label for="server_delete_tomcat_port">톰캣 포트</label>
                    <input type="text" id="server_delete_tomcat_port" placeholder="톰캣 포트"/>
                    <p id="tomcat_port_check_server_delete">포트번호는 숫자로 작성해야합니다.</p>
                </li>
            </ul>
            <div class="button_box">
                <button class="button button-normal btn_submit" type="button">확인</button>
                <div class="button_separator"></div>
                <button class="button button-normal btn_cancel" type="button">취소</button>
            </div>
        </div>
    </div>
</div>
