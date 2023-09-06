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
    <link href='${pageContext.request.contextPath}/resource/css/select.css' rel='stylesheet'/>
    <link href='${pageContext.request.contextPath}/resource/css/animation.css' rel='stylesheet'/>
    <link href='${pageContext.request.contextPath}/resource/css/button.css' rel='stylesheet'/>
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/chart.js" defer></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.0.0/dist/chart.min.js" defer></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2" defer></script>
<script src="${pageContext.request.contextPath}/resource/js/monitoring.js" defer></script>
<div class="wrap">
    <div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="_csrf_header" value="${_csrf.headerName}"/>
    </div>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/common/head.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/popup.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/popup_log.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/server/server_plus.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/server/server_delete.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/progress.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_list.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_edit.jsp"/>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/view/popup/user/user_plus.jsp"/>
    <div class="app">
        <div class="left-content">
            <div class="server_select_div main_box">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <div class="select_div">
                    <label for="select_server"></label>
                    <select class="select_box" id="select_server" onchange="changeServer()"></select>
                    <button class="select_button" onclick="clickSelectServer(this)">▼</button>
                </div>
            </div>
            <div class="alarm_list server main_box">
                <div class="title_box">
                    <h1 class="title log">서버 로그 (최근 10개)</h1>
                    <a class="log_icon" onclick="popupOpen(null, null, 'server_log')"></a>
                </div>
                <div class="alarm_list_body server">
                    <ul class="alarm_content server">
                    </ul>
                </div>
            </div>
            <div class="main_box flex_box server">
                <div class="title_box"><h1 class="title">서버 하드웨어 사용량</h1></div>
                <div class="graph_bar">
                    <canvas id="myChart1"></canvas>
                </div>
            </div>
        </div>
        <div class="right-content">
            <div class="right_top_content">
                <div class="server_list_head user">
                    <table class="server_table user">
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
                        </colgroup>
                        <thead class="server_header user">
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
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="server_list_body user">
                    <table class="server_table user" id="server_list">
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
                        </colgroup>
                        <tbody class="server_content user">
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="right_bottom_content">
                <div class="graph_line main_box">
                    <div class="title_box flex_box">
                        <div class="h1_box">
                            <h1 class="title">시간별 서버 트래픽 사용량</h1>
                        </div>
                        <div class="select_div select_port">
                            <label for="port_select"></label>
                            <select class="select_box" id="port_select" name="port_selectbox" onchange="changePort()"></select>
                            <button class="select_button" onclick="clickSelectServer(this)">▼</button>
                        </div>
                    </div>
                    <div class="graph_content">
                        <canvas id="myChart2"></canvas>
                    </div>
                </div>
                <div class="alarm_list all main_box">
                    <div class="title_box"><h1 class="title">전체 서버 알람 (최근 일주일)</h1></div>
                    <div class="alarm_list_body all">
                        <ul class="alarm_content all">
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