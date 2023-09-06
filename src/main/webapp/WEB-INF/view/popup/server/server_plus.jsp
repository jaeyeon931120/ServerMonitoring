<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-07
  Time: 오후 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper server_plus">
    <div class="popup server_plus">
        <div class="popup_head server_plus">
            <h1>서버 추가</h1>
            <a class="btn-close trigger" onclick="popupClose('server_plus')">x</a>
        </div>
        <div class="popup_body server_plus">
            <ul>
                <li>
                    <label for="server_plus_system">시스템</label>
                    <input type="text" id="server_plus_system" placeholder="시스템"/>
                    <p id="system_check_server_plus">시스템을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_plus_ip">IP</label>
                    <input type="text" id="server_plus_ip" placeholder="IP"/>
                    <p id="ip_check_server_plus">IP는 숫자만 사용가능하고<br>IP는 000.000.000.000형식으로 작성해야합니다</p>
                </li>
                <li>
                    <label for="server_plus_id">서버 ID</label>
                    <input type="text" id="server_plus_id" placeholder="서버 ID"/>
                    <p id="id_check_server_plus">서버 로그인 아이디를 입력하셔야 됩니다.</p>
                </li>
                <li>
                    <form class="popup_form" onsubmit="return false;">
                        <label for="server_plus_password">서버 PW</label>
                        <input type="password" id="server_plus_password" autocomplete="off" placeholder="서버 PW"/>
                        <p id="password_check_server_plus">서버 로그인 비밀번호를 입력하셔야 됩니다.</p>
                    </form>
                </li>
                <li>
                    <label for="server_plus_server_name">서버 이름</label>
                    <input type="text" id="server_plus_server_name" placeholder="서버 이름"/>
                    <p id="server_name_check_server_plus">서버 이름을 입력하셔야됩니다.</p>
                </li>
                <li>
                    <label for="server_plus_server_port">서버 포트</label>
                    <input type="text" id="server_plus_server_port" placeholder="서버 포트"/>
                    <p id="server_port_check_server_plus">포트는 65535이하의 숫자로 입력하셔야 됩니다.</p>
                </li>
                <li>
                    <label for="server_plus_tomcat_port">톰캣 포트</label>
                    <input type="text" id="server_plus_tomcat_port" placeholder="톰캣 포트"/>
                    <p id="tomcat_port_check_server_plus">포트는 65535이하의 숫자로 입력하셔야 됩니다.</p>
                </li>
                <li>
                    <label for="server_plus_tomcat_dir">톰캣 폴더 위치</label>
                    <input type="text" id="server_plus_tomcat_dir" placeholder="톰캣 폴더 위치"/>
                    <p id="tomcat_dir_check_server_plus">톰캣 폴더 위치를 입력하셔야 됩니다.</p>
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