<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-06-21
  Time: 오후 6:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup_wrapper userlist">
    <div class="popup_userlist">
        <div class="popup_head userlist">
            <h1>사용자 리스트</h1>
            <a class="btn-close trigger" onclick="popupClose('userlist')">x</a>
        </div>
        <div class="popup_body userlist">
            <div class="userlist_table_head">
                <table class="userlist_table">
                    <colgroup>
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
                        <th>사용자 권한</th>
                        <th>사용자 TEL</th>
                        <th>수정</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="userlist_table_body">
                <table class="userlist_table" id="user_list">
                    <colgroup>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                    </colgroup>
                    <tbody>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>t</td>
                        <td>일반</td>
                        <td>010-9867-8790</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test1</td>
                        <td>관리자</td>
                        <td>031-400-3794</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test1</td>
                        <td>일반</td>
                        <td>010-3799-6854</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test2</td>
                        <td>관리자</td>
                        <td>010-3799-6854</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test3</td>
                        <td>관리자</td>
                        <td>010-3768-0907</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test4</td>
                        <td>일반</td>
                        <td>010-9766-8790</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test5</td>
                        <td>일반</td>
                        <td>010-5677-8892</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test6</td>
                        <td>관리자</td>
                        <td>010-6771-7091</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test7</td>
                        <td>관리자</td>
                        <td>010-4211-3391</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test8</td>
                        <td>일반</td>
                        <td>010-1778-3491</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>
                                <input class="userlist_check" type="checkbox"/>
                            </label>
                        </td>
                        <td>test9</td>
                        <td>관리자</td>
                        <td>010-3775-9491</td>
                        <td>
                            <button class="btn red"><span>수정</span></button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <button class="btn blue"><span>추가</span></button>
            <button class="btn red"><span>삭제</span></button>
        </div>
    </div>
</div>
