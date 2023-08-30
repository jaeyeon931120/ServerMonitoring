<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-02
  Time: 오전 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper server_log">
    <div class="popup server_log">
        <div class="popup_head server_log">
            <h1>서버 로그</h1>
            <a class="btn-close trigger" onclick="popupClose('server_log')">x</a>
        </div>
        <div class="popup_body server_log main_box">
            <div class="select_div">
                <label for="time_select"></label>
                <select class="select_box" id="time_select" onchange="getLogDataList('popup')"></select>
                <button class="select_button" onclick="clickSelectServer(this)">▼</button>
            </div>
            <div class="server_log_table_head">
                <table class="server_log_table">
                    <colgroup>
                        <col/>
                        <col/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th>발생 시간</th>
                        <th>로그 내용</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="server_log_table_body">
                <table class="server_log_table" id="server_log_list">
                    <colgroup>
                        <col/>
                        <col/>
                    </colgroup>
                    <tbody id="server_log_list_tbody">
                    </tbody>
                </table>
            </div>
            <!--/* 페이지네이션 렌더링 영역 */-->
            <div class="paging">

            </div>
        </div>
    </div>
</div>