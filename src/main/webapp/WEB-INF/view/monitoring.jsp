<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2023-05-23
  Time: 오전 9:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sever Monitoring</title>
    <link href='${pageContext.request.contextPath}/resource/css/monitoring.css' rel='stylesheet'/>
    <link href='${pageContext.request.contextPath}/resource/css/progress.css' rel='stylesheet'/>
</head>
<body>
<script src="${pageContext.request.contextPath}/resource/lib/amcharts5/index.js"></script>
<script src="${pageContext.request.contextPath}/resource/lib/amcharts5/percent.js"></script>
<script src="${pageContext.request.contextPath}/resource/lib/amcharts5/themes/Animated.js"></script>
<script src="${pageContext.request.contextPath}/resource/lib/amcharts5/themes/Dark.js"></script>
<script src="${pageContext.request.contextPath}/resource/lib/amcharts5/themes/Responsive.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/monitoring.js"></script>
<div class="wrap">
    <div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="_csrf_header" value="${_csrf.headerName}"/>
    </div>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/common/head.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/popup.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/server/plus.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/server/delete.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/progress.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_list.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_edit.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_plus.jsp"/>
    <div class="app">
        <div id="chartdiv"></div>
        <div class="menu_toggle">
            <input class="burger-check" type="checkbox" id="burger-check"/>
            <label class="burger-icon" for="burger-check">
                <span class="burger-sticks"></span></label>
            <div class="menu">
                <div class="menu_head"></div>
                <div class="menu_body">
                    <ul>
                        <a onclick="popupOpen(null, null, 'plus')">
                            <li>추가</li>
                        </a>
                        <a onclick="popupOpen(null, null, 'delete')">
                            <li>삭제</li>
                        </a>
                    </ul>
                </div>
            </div>
        </div>
        <div class="content">
            <div class="server_list_head">
                <table class="server_table">
                    <colgroup>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                    </colgroup>
                    <thead class="server_header">
                    <tr>
                        <th>시스템</th>
                        <th>IP</th>
                        <th>서버</th>
                        <th>서버포트</th>
                        <th>상태</th>
                        <th>트래픽 TX</th>
                        <th>트래픽 RX</th>
                        <th>CPU</th>
                        <th>메모리</th>
                        <th>DISK</th>
                        <th>가동</th>
                        <th>정지</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="server_list_body">
                <table class="server_table" id="server_list">
                    <colgroup>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                        <col/>
                    </colgroup>
                    <tbody class="server_content">
                    <tr>
                        <td rowspan="4">HEMS</td>
                        <td rowspan="2">118.67.130.184</td>
                        <td rowspan="2">api-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td rowspan="2">20%</td>
                        <td rowspan="2">25%</td>
                        <td rowspan="2">10%</td>
                        <td rowspan="2">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="2">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">HEMS</td>
                        <td hidden="hidden">118.67.130.184</td>
                        <td hidden="hidden">api-server</td>
                        <td>9091</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">HEMS</td>
                        <td rowspan="2">118.67.131.221</td>
                        <td rowspan="2">fep-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>1.0KB</td>
                        <td>0.6KB</td>
                        <td rowspan="2">10%</td>
                        <td rowspan="2">20%</td>
                        <td rowspan="2">10%</td>
                        <td rowspan="2">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="2">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">HEMS</td>
                        <td hidden="hidden">118.67.131.221</td>
                        <td hidden="hidden">fep-server</td>
                        <td>9091</td>
                        <td>정지</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="8">BEMS</td>
                        <td rowspan="4">118.67.131.170</td>
                        <td rowspan="3">bems-fep-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td rowspan="4">20%</td>
                        <td rowspan="4">25%</td>
                        <td rowspan="4">10%</td>
                        <td rowspan="3">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="3">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9001</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9010</td>
                        <td>정지</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td>ntek-server-san</td>
                        <td>9005</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td>
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td>
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td rowspan="4">118.67.133.83</td>
                        <td rowspan="3">bems-fep-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td rowspan="4">20%</td>
                        <td rowspan="4">25%</td>
                        <td rowspan="4">10%</td>
                        <td rowspan="3">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="3">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9001</td>
                        <td>정지</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9010</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td>ntek-server-san</td>
                        <td>9005</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td>
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td>
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td rowspan="8">BEMS</td>
                        <td rowspan="4">118.67.131.170</td>
                        <td rowspan="3">bems-fep-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td rowspan="4">20%</td>
                        <td rowspan="4">25%</td>
                        <td rowspan="4">10%</td>
                        <td rowspan="3">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="3">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9001</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9010</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.131.170</td>
                        <td>ntek-server-san</td>
                        <td>9005</td>
                        <td>정지</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td>
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td>
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td rowspan="4">118.67.133.83</td>
                        <td rowspan="3">bems-fep-server</td>
                        <td>8080</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td rowspan="4">20%</td>
                        <td rowspan="4">25%</td>
                        <td rowspan="4">10%</td>
                        <td rowspan="3">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td rowspan="3">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9001</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td hidden="hidden">bems-fep-server</td>
                        <td>9010</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td hidden="hidden">
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td hidden="hidden">
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    <tr>
                        <td hidden="hidden">BEMS</td>
                        <td hidden="hidden">118.67.133.83</td>
                        <td>ntek-server-san</td>
                        <td>9005</td>
                        <td>가동</td>
                        <td>5.14KB</td>
                        <td>6.34KB</td>
                        <td hidden="hidden">20%</td>
                        <td hidden="hidden">25%</td>
                        <td hidden="hidden">10%</td>
                        <td>
                            <button class="btn blue"><span>Θ</span></button>
                        </td>
                        <td>
                            <button class="btn red"><span>Θ</span></button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div style="clear:both;"></div>
            <div class="ranking_combination">
                <div class="alarm_list">
                    <h1>알람 리스트</h1>
                    <div class="alarm_list_body">
                        <ul class="alarm_content">
                            <li>lbems-web-was-02</li>
                            <li>위치 : LBEMS/118.67.128.134/lbems-web-was-02</li>
                            <li>포트번호 : 8080</li>
                            <li>발생 시간 : 2023-06-16 00:00:00</li>
                            <li>hems-was-01</li>
                            <li>위치 : HEMS/118.67.128.134/hems-was-01</li>
                            <li>포트번호 : 9001</li>
                            <li>발생 시간 : 2023-06-16 00:00:00</li>
                            <li>toc-server-01</li>
                            <li>위치 : HEMS/118.67.128.134/toc-server-01</li>
                            <li>포트번호 : 8080</li>
                            <li>발생 시간 : 2023-06-16 00:00:00</li>
                            <li>rems-web-was-01</li>
                            <li>위치 : LBEMS/118.67.128.134/rems-web-was-01</li>
                            <li>포트번호 : 9005</li>
                            <li>발생 시간 : 2023-06-16 00:00:00</li>
                            <li>hems-new-live1</li>
                            <li>위치 : HEMS/118.67.128.134/hems-new-live1</li>
                            <li>포트번호 : 8080</li>
                            <li>발생 시간 : 2023-06-16 00:00:00</li>
                        </ul>
                    </div>
                </div>
                <div class="ranking_list">
                    <h1>CPU 사용랭킹</h1>
                    <div class="ranking_list_body">
                        <ul class="ranking_content">
                            <li>lbems-web-was-02</li>
                            <li>CPU: 51.77%</li>
                            <li>hems-was-01</li>
                            <li>CPU: 0.96%</li>
                            <li>toc-server-01</li>
                            <li>CPU: 0.91%</li>
                            <li>rems-web-was-01</li>
                            <li>CPU: 0.67%</li>
                            <li>hems-new-live1</li>
                            <li>CPU: 0.59%</li>
                        </ul>
                    </div>
                </div>
                <div class="ranking_list">
                    <h1>메모리 사용량 랭킹</h1>
                    <div class="ranking_list_body">
                        <ul class="ranking_content">
                            <li>hems-new-live1</li>
                            <li>MEM: 59.90%</li>
                            <li>toc-server-01</li>
                            <li>MEM: 32.22%</li>
                            <li>rems-web-was-01</li>
                            <li>MEM: 29.60%</li>
                            <li>hems-was-01</li>
                            <li>MEM: 27.22%</li>
                            <li>lbems-web-was-02</li>
                            <li>MEM: 25.73%</li>
                        </ul>
                    </div>
                </div>
                <div class="ranking_list">
                    <h1>디스크 사용량 랭킹</h1>
                    <div class="ranking_list_body">
                        <ul class="ranking_content">
                            <li>hems-new-live1</li>
                            <li>DISK: 36.19%</li>
                            <li>toc-server-01</li>
                            <li>DISK: 22.56%</li>
                            <li>lbems-web-was-02</li>
                            <li>DISK: 8.89%</li>
                            <li>hems-was-01</li>
                            <li>DISK: 7.64%</li>
                            <li>rems-web-was-01</li>
                            <li>DISK: 6.60%</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/common/footer.jsp"/>
</div>
</body>
</html>
