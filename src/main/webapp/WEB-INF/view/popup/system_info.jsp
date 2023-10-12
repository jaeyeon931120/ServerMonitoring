<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-02
  Time: 오전 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="popup_wrapper system_info">
    <div class="popup system_info">
        <div class="popup_head system_info">
            <h1>시스템 정보</h1>
            <a class="btn-close trigger" onclick="popupClose('system_info')">x</a>
        </div>
        <div class="popup_body system_info">
            <ul>
                <li>
                    <label for="system_name">시스템명</label>
                    <input id="system_name" type="text" value="DCAM" disabled>
                </li>
                <li>
                    <label for="system_build">빌드 정보</label>
                    <input id="system_build" type="text" value="v1.0" disabled>
                </li>
                <li>
                    <label for="system_manual_word">메뉴얼(문서)</label>
                    <div class="input_image">
                        <input id="system_manual_word" type="text" value="DCAM_v1.0_메뉴얼_v1.0.docx" disabled>
                        <i class="fa-download" id="manual_word_download" onclick="manual_download('manual_word')"></i>
                    </div>
                </li>
                <li>
                    <label for="system_manual_video">메뉴얼(동영상)</label>
                    <div class="input_image">
                        <input id="system_manual_video" type="text" value="DCAM_v1.0_메뉴얼(동영상)_v1.0.zip" disabled>
                        <i class="fa-download" id="manual_video_download" onclick="manual_download('manual_video')"></i>
                    </div>
                </li>
                <li>
                    <label for="system_product_information">제품 설명서</label>
                    <div class="input_image">
                        <input id="system_product_information" type="text" value="DCAM_v1.0_제품설명서_v1.0.hwp" disabled>
                        <i class="fa-download" id="product_information_download" onclick="manual_download('product_information')"></i>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
