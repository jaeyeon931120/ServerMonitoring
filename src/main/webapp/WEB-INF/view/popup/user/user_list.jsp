<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-21
  Time: 오후 6:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup_wrapper user_list">
    <div class="popup user_list">
        <div class="popup_head user_list">
            <h1>사용자 리스트</h1>
            <a class="btn-close trigger" onclick="popupClose('user_list')">x</a>
        </div>
        <div class="popup_body user_list">
            <div class="user_list_table_head">
                <table class="user_list_table">
                    <colgroup>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                    </colgroup>
                    <thead>
                    <tr>
                        <th></th>
                        <th>사용자 ID</th>
                        <th>사용자 이름</th>
                        <th>사용자 권한</th>
                        <th>사용자 TEL</th>
                        <th>수정</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="user_list_table_body">
                <table class="user_list_table" id="user_list">
                    <colgroup>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                    </colgroup>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="button_box">
                <button class="button button-normal btn_submit" type="button">추가</button>
                <div class="button_separator"></div>
                <button class="button button-normal btn_cancel" type="button">삭제</button>
            </div>
        </div>
    </div>
</div>
