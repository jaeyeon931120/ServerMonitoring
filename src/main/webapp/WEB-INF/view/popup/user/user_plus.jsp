<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-07
  Time: 오후 2:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper user_plus">
    <div class="popup_user_plus">
        <div class="popup_head user_plus">
            <h1>사용자 추가</h1>
            <a class="btn-close trigger" onclick="popupClose('user_plus')">x</a>
        </div>
        <div class="popup_body user_plus">
            <ul>
                <li>
                    <label for="user_plus_id">
                        사용자 ID
                    </label>
                    <input type="text" id="user_plus_id" placeholder="ID를 입력하고 중복 확인"/>
                    <button class="button button-normal btn_check" onclick="id_duplication_check('user_plus')"><span>중복<br/>확인</span></button>
                    <img src="${pageContext.request.contextPath}/resource/image/check_good.png" alt=""/>
                    <p id="id_check_user_plus">중복된 ID가 존재합니다. 다른 ID를 입력해주세요.</p>
                </li>
                <li>
                    <label for="user_plus_password">
                        사용자 PW
                    </label>
                    <input type="password" id="user_plus_password" placeholder="영어와 숫자, 특수문자를 포함 8~20자리"/>
                    <p id="pw_check_user_plus">비밀번호는 영어와 숫자, 특수문자를 포함하여 8자리 이상 20자리 이하로 작성해주세요.</p>
                </li>
                <li>
                    <label for="user_plus_username">사용자 이름</label>
                    <input type="text" id="user_plus_username" placeholder="사용자 이름"/>
                    <p id="username_check_user_plus">사용자의 이름을 입력하지 않았습니다.<br>사용자의 이름을 입력해주세요</p>
                </li>
                <li>
                    <article class="cont-select">
                        <label for="user_plus_auth">사용자 권한</label>
                        <button class="btn-select" id="user_plus_auth">사용자 권한</button>
                        <ul class="list-member" id="user_plus-member">
                            <li><button type="button">관리자</button></li>
                            <li><button type="button">일반 사용자</button></li>
                        </ul>
                    </article>
                    <p id="auth_check_user_plus">사용자 권한을 선택하지 않으셨습니다.<br>사용자 권한을 선택해주세요.</p>
                </li>
                <li>
                    <label for="user_plus_tel">사용자 TEL</label>
                    <input type="text" id="user_plus_tel" placeholder="-를 포함하여 입력"/>
                    <p id="tel_check_user_plus">전화번호는 숫자만 가능하고, 형식은 000-0000-0000으로<br/>-를 포함하여 입력해주세요.</p>
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