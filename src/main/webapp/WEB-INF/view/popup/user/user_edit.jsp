<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-16
  Time: 오후 2:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup_wrapper user_edit">
    <div class="popup user_edit">
        <div class="popup_head user_edit">
            <h1>사용자 정보 수정</h1>
            <a class="btn-close trigger" onclick="popupClose('user_edit')">x</a>
        </div>
        <div class="popup_body user_edit">
            <ul>
                <li>
                    <label for="user_edit_id">사용자 ID</label><input type="text" id="user_edit_id" value="" disabled/>
                </li>
                <li>
                    <form class="popup_form">
                        <label for="user_edit_password">
                            사용자 PW
                        </label>
                        <input type="password" id="user_edit_password" autocomplete="off" placeholder="영어와 숫자, 특수문자를 포함 8~20자리"/>
                        <p class="guide">비밀번호를 변경하지 않으시면 자동으로 이전 비밀번호가 저장됩니다.</p>
                        <p id="pw_check_user_edit">비밀번호는 영어와 숫자, 특수문자를 포함하여 8자리 이상 20자리 이하로 작성해주세요.</p>
                    </form>
                </li>
                <li>
                    <label for="user_edit_username">사용자 이름</label>
                    <input type="text" id="user_edit_username" placeholder="사용자 이름"/>
                    <p id="username_check_user_edit">사용자의 이름을 입력하지 않았습니다.<br>사용자의 이름을 입력해주세요</p>
                </li>
                <li>
                    <article class="cont-select">
                        <label for="user_edit_auth">사용자 권한</label>
                        <button class="btn-select" id="user_edit_auth">사용자 권한</button>
                        <ul class="list-member" id="user_edit-member">
                            <li>
                                <button type="button">관리자</button>
                            </li>
                            <li>
                                <button type="button">일반 사용자</button>
                            </li>
                        </ul>
                    </article>
                    <p id="auth_check_user_edit">사용자 권한을 선택하지 않으셨습니다.<br>사용자 권한을 선택해주세요.</p>
                </li>
                <li>
                    <label for="user_edit_tel">사용자 연락처</label><input type="text" id="user_edit_tel" value="">
                    <p id="tel_check_user_edit">전화번호는 숫자만 가능하고, 형식은 000-0000-0000으로<br/>-를 포함하여 입력해주세요.</p>
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