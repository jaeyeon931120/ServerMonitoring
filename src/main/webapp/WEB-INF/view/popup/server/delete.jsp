<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-08
  Time: 오후 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper delete">
    <div class="popup_delete">
        <div class="popup_head delete">
            <h1>서버 삭제</h1>
            <a class="btn-close trigger" onclick="popupClose('delete')">x</a>
        </div>
        <div class="popup_body delete">
            <ul>
                <li>
                    <label for="system_delete">시스템</label>
                    <input type="text" id="system_delete" placeholder="시스템"/>
                    <p id="system_check_delete">시스템을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="ip_delete">IP</label>
                    <input type="text" id="ip_delete" placeholder="IP"/>
                    <p id="ip_check_delete">IP는 숫자만 사용가능하고<br>IP는 000.000.000.000형식으로 작성해야합니다.</p>
                </li>
                <li>
                    <label for="server_name_delete">서버 이름</label>
                    <input type="text" id="server_name_delete" placeholder="서버 이름"/>
                    <p id="name_check_delete">서버 이름을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_port_delete">서버 포트</label>
                    <input type="text" id="server_port_delete" placeholder="서버 포트"/>
                    <p id="server_port_check_delete">포트번호는 숫자로 작성해야합니다.</p>
                </li>
                <li>
                    <label for="tomcat_port_delete">톰캣 포트</label>
                    <input type="text" id="tomcat_port_delete" placeholder="톰캣 포트"/>
                    <p id="tomcat_port_check_delete">포트번호는 숫자로 작성해야합니다.</p>
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
