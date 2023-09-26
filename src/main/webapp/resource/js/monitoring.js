let token;
let header;
let popup;
let popuphead;
let popupbody;
let h1;
let select_value;
let btn_manual;
let popup_user_plus;
let id_check_btn;
let ok_img;
let user_plus_id;
let pw_check_edit;
let auth_check_edit;
let tel_check_edit;
let id_check_plus;
let pw_check_plus;
let auth_check_plus;
let tel_check_plus;
let id_check_result;
let clock;
let myChartBar;
let myChartLine;
let ctx1;
let ctx2;
let gradient1;
let gradient2;
let gradient3;
let serverlist = [];
let tomcatportMap = new Map();
let iplist = new Map();
let cpulist = new Map();
let memorylist = new Map();
let disklist = new Map();
let trapic = new Map();
let trapic_length;
let animation;
let port_label;
let port_select;
let selectServer;
const open = "popup_wrapper open";

window.onload = function () {
    token = document.querySelector("input[name='_csrf']").value;
    header = document.querySelector("input[name='_csrf_header']").value;
    popup = document.querySelector(".popup_wrapper");
    popuphead = document.querySelector(".popup_head");
    popupbody = document.querySelector(".popup_body");
    h1 = document.createElement("h1");
    btn_manual = document.querySelector('.btn_manual_download');
    popup_user_plus = document.querySelector('.popup.user_plus');
    id_check_btn = document.querySelector('.popup_body.user_plus > ul > li > .btn_check');
    ok_img = document.querySelector('.popup_body.user_plus > ul > li > img');
    user_plus_id = document.getElementById('user_plus_id');
    pw_check_edit = document.getElementById('password_check_user_edit');
    auth_check_edit = document.getElementById('auth_check_user_edit');
    tel_check_edit = document.getElementById('tel_check_user_edit');
    id_check_plus = document.getElementById('id_check_user_plus');
    pw_check_plus = document.getElementById('password_check_user_plus');
    auth_check_plus = document.getElementById('auth_check_user_plus');
    tel_check_plus = document.getElementById('tel_check_user_plus');
    clock = document.getElementById('clock');
    ctx1 = document.getElementById('myChart1').getContext('2d');
    ctx2 = document.getElementById('myChart2').getContext('2d');
    port_select = document.getElementById('port_select');
    const gear_check = document.getElementById("setting");
    const menu_check = document.getElementById("burger-check");
    const head_menu_button = document.querySelectorAll('.menu_body.setting > ul > a');
    const menu_button = document.querySelectorAll('.menu_body > ul > a');
    const content = document.getElementsByClassName('tb1-content');
    const content_table = document.querySelectorAll('table');
    const tb1header = document.getElementsByClassName('tb1-header');
    let scrollwidth;

    getClock();
    setInterval(getClock, 1000);
    setInterval(getServerInfo, 60000);
    setCanvasSize();

    /*메인데이터를 불러오는 함수*/
    getServerInfo();

    for (let i = 0; i < content.length; i++) {
        if (content_table.item(i) !== undefined) {
            scrollwidth = content.item(i).width - content_table.item(i).clientWidth;
        }
    }
    for (let i = 0; i < tb1header.length; i++) {
        tb1header.item(i).css('padding-right', scrollwidth)
    }

    for (let i = 0; i < head_menu_button.length; i++) {
        if (head_menu_button[i].clickHandler) {
            head_menu_button[i].removeEventListener('click', () => gear_check.checked = false);
        }
        head_menu_button[i].addEventListener('click', () => gear_check.checked = false);
    }

    for (let i = 0; i < menu_button.length; i++) {
        if (menu_button[i].clickHandler) {
            menu_button[i].removeEventListener('click', () => menu_check.checked = false);
        }
        menu_button[i].addEventListener('click', () => menu_check.checked = false);
    }
}

function selectOptionCreate() {
    const select = document.getElementById('select_server');

    select.replaceChildren();

    for (let i = 0; i < serverlist.length; i++) {
        const option = document.createElement('option');
        option.className = 'select_item';
        option.value = serverlist[i];
        option.textContent = serverlist[i];
        option.style.height = "40px";

        select.appendChild(option);
    }

    if(selectServer !== undefined) {
        for(let i = 0; i < select.options.length; i++) {
            if(select.options[i].value === selectServer) {
                select.options[i].selected = true;
            }
        }
    }

    if(select.options[select.selectedIndex] !== undefined) {
        selectServer = select.options[select.selectedIndex].value;
    }
}

function addListener(btn, data, action, process, work) {
    if (btn.clickHandler) {
        btn.removeEventListener("click", btn.clickHandler);
    }
    btn.clickHandler = () => {
        if (work === "open") {
            popupOpen(data, action, process);
        } else if (work === "close") {
            popupClose(process);
        } else if (work === "result_open") {
            result_popupOpen(data, action, process);
        } else if (work === "error_open") {
            error_popupOpen(data, action, process);
        } else if (work === "server_manage") {
            server_management(process);
        } else if (work === "user_manage") {
            user_management(process);
        } else if (work === "power_work") {
            power_work(data, action);
        }
    }

    btn.addEventListener("click", btn.clickHandler);
}

function addinputListener(btn, work, process) {

    if (btn.clickHandler) {
        btn.removeEventListener("change", btn.clickHandler);
    }

    btn.clickHandler = () => {
        if(process.indexOf("user") > -1) {
            if(work === "id") {
                console.log("user_id");
                id_check(process);
            } else if(work === "pw") {
                console.log("user_pw");
                pw_check(process);
            } else if(work === "username") {
                console.log("user_name");
                name_check(process, work);
            } else if(work === "tel") {
                tel_check(process);
            }
        } else if(process.indexOf("server") > -1) {
            if(work === "system") {
                system_check(process);
            } else if(work === "ip") {
                ip_check(process);
            } else if(work.indexOf("port") > -1) {
                console.log("sever_port");
                port_check(process, work);
            } else {
                name_check(process, work);
            }
        }
    }

    btn.addEventListener("change", btn.clickHandler);
}

function clickSelectServer(elem) {
    const select_id = elem.parentElement.querySelector('select').id;
    const select = document.getElementById(select_id);
    select.focus();
}

function changeServer() {
    const select_box = document.getElementById("select_server");
    select_box.blur();
    selectServer = select_box.options[select_box.selectedIndex].value;

    createGraphBar(selectServer, cpulist, memorylist, disklist);
    trapicGraphData();
    getLogDataList("main");
}

function changePort() {
    const select_box = document.getElementById("port_select")
    let port = port_select.options[port_select.selectedIndex].value;
    select_box.blur();

    aniGraphLine();
    createGraphLine(port);
}

function getClock() {
    const date = new Date()
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hour = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    const second = String(date.getSeconds()).padStart(2, "0");//number이기 때문에 padStart 붙일 수 없음. String 변환해주어야한다.
    const weakday = new Date("'" + year + "-" + month + "-" + day + "'");
    const WEEKDAY = ['일', '월', '화', '수', '목', '금', '토'];
    let week = WEEKDAY[weakday.getDay()];

    clock.innerText = `${year}-${month}-${day}(${week}) ${hour}:${minutes}:${second}`;
}

function isStringValue(value) {
    return !!value?.trim()
}

function ObjectToMap(object) {
    let dataMap = new Map();

    for (let key of Object.keys(object)) {
        const value = object[key];
        dataMap.set(key, value);
    }

    return dataMap;
}

function date_parse(str) {
    let y = str.substring(0, 4);
    let m = str.substring(4, 6);
    let d = str.substring(6, 8);
    let hh = str.substring(8, 10);
    let mm = str.substring(10, 12);
    let ss = str.substring(12, 14);
    return y + '-' + m + '-' + d + " " + hh + ":" + mm + ":" + ss;
}

function setCanvasSize() {
    const canvas1 = document.querySelector('#myChart1');
    const canvas2 = document.querySelector('#myChart2');
    canvas1.width = canvas1.parentElement.clientWidth;
    canvas2.width = canvas2.parentElement.clientWidth;
    canvas1.height = canvas1.parentElement.clientHeight;
    canvas2.height = canvas2.parentElement.clientHeight;
}

function createGraphBar(server, cpulist, memorylist, disklist) {
    /* 차트를 새로 만들기 위해 디스트로이 */
    if (myChartBar !== undefined) {
        myChartBar.destroy();
    }

    /* Bar그래프에 활용한 그라데이션 */
    gradient1 = ctx1.createLinearGradient(0, 0, 600, 600);
    gradient1.addColorStop(0, 'rgba(238, 255, 217, 1)');
    gradient1.addColorStop(0.25, 'rgba(199, 238, 165, 1)');
    gradient2 = ctx1.createLinearGradient(0, 0, 600, 600);
    gradient2.addColorStop(0, 'rgba(222, 240, 164, 1)');
    gradient2.addColorStop(0.25, 'rgba(127, 184, 71, 1)');
    gradient3 = ctx1.createLinearGradient(0, 0, 600, 600);
    gradient3.addColorStop(0, 'rgba(159, 208, 91, 1)');
    gradient3.addColorStop(0.25, 'rgba(51, 127, 37, 1)');

    myChartBar = new Chart(ctx1, {
        plugins: [ChartDataLabels],
        type   : 'bar',
        data   : {
            labels  : ['CPU', 'MEMORY', 'DISK'],
            datasets: [
                {
                    data           : [cpulist.get(server), memorylist.get(server), disklist.get(server)],
                    backgroundColor: [
                        gradient1,
                        gradient2,
                        gradient3
                    ],
                    borderColor    : [
                        gradient1,
                        gradient2,
                        gradient3
                    ],
                    borderWidth    : 1
                }
            ]
        },
        options: {
            indexAxis : 'y',
            scales    : {
                x: {
                    ticks: {
                        color: "rgba(255, 255, 255, 1)",
                        Size : 14
                    },
                    grid : {
                        display: false
                    },
                    min  : 0,
                    max  : 100,
                },
                y: {
                    ticks: {
                        beginAtZero: true,
                        color      : "rgba(255, 255, 255, 1)",
                        font       : {
                            size  : 12,
                            weight: "bold"
                        },
                        padding    : 10, // x축 값의 상하 패딩을 설정할 수 있어요.
                    },
                    grid : {
                        display: false
                    }
                },
            },
            responsive: false,
            plugins   : {
                legend    : { // 범례 사용 안 함
                    display: false,
                },
                tooltip   : { // 기존 툴팁 사용 안 함
                    enabled: true
                },
                datalabels: { // datalables 플러그인 세팅
                    formatter: function (value, context) {
                        let idx = context.dataIndex; // 각 데이터 인덱스

                        // 출력 텍스트
                        return context.chart.data.labels[idx] + "\n" + value + '%';
                    },
                    font     : { // font 설정
                        weight: 'bold',
                        size  : '12px',
                    },
                    color    : [gradient1, gradient2, gradient3], // font color,
                    anchor   : 'end',
                    align    : 'right'
                }
            }
        }
    });
}

function aniGraphLine() {
    const totalDuration = 1100;
    const delayBetweenPoints = totalDuration / trapic_length;
    let previousY;
    if (ctx2.chart !== undefined) {
        previousY = (ctx2) => ctx2.index === 0 ? ctx2.chart.scales.y.getPixelForValue(100) : ctx2.chart.getDatasetMeta(ctx2.datasetIndex).data[ctx2.index - 1].getProps(['y'], true).y;
    }
    animation = {
        x: {
            easing  : 'linear',
            duration: delayBetweenPoints,
            from    : NaN, // the point is initially skipped
            delay(ctx2) {
                if (ctx2.type !== 'data' || ctx2.xStarted) {
                    return 0;
                }
                ctx2.xStarted = true;
                return ctx2.index * delayBetweenPoints;
            }
        },
        y: {
            easing  : 'linear',
            duration: delayBetweenPoints,
            from    : previousY,
            delay(ctx2) {
                if (ctx2.type !== 'data' || ctx2.yStarted) {
                    return 0;
                }
                ctx2.yStarted = true;
                return ctx2.index * delayBetweenPoints;
            }
        }
    };
}

function createGraphLine(port) {
    if (myChartLine !== undefined) {
        myChartLine.destroy();
    }

    port_label = tomcatportMap.get(selectServer);
    let config = {
        type   : 'line',
        data   : {
            labels  : ['00', '01', '02', '03', '04', '05', '06', '07', '08',
                '09', '10', '11', '12', '13', '14', '15', '16',
                '17', '18', '19', '20', '21', '22', '23', '24'],
            datasets: [],
        },
        options: {
            responsive : false,
            animation,
            interaction: {
                intersect: false,
            },
            scales     : {
                x: {
                    ticks: {
                        color: "rgba(255, 255, 255, 0.5)",
                        font : {
                            size: 13
                        },
                    },
                    grid : {
                        color    : 'rgba(255, 255, 255, 0.1)',
                        lineWidth: 1
                    },
                },
                y: {
                    ticks: {
                        beginAtZero: false,
                        color      : "rgba(255, 255, 255, 0.5)",
                        font       : {
                            size: 13
                        },
                    },
                    grid : {
                        color    : 'rgba(255, 255, 255, 0.1)',
                        lineWidth: 1
                    },
                },
            },
            plugins    : {
                legend : {
                    position: 'top',
                    labels  : {
                        color: 'rgba(255, 255, 255, 1)'
                    }
                },
                tooltip: {
                    enabled  : true,
                    mode     : 'index',
                    axis     : 'x',
                    position : 'nearest',
                    intersect: false,
                    callbacks: {
                        title: function (tooltipItem) {
                            let label;
                            for (let i = 0; i < tooltipItem.length; i++) {
                                label = tooltipItem[i].label + ":00";
                            }
                            return label;
                        },
                        label: function (tooltipItem) {
                            return " " + tooltipItem.dataset.label + " : " + tooltipItem.formattedValue + " (Kb)";
                        },
                    }
                },
            }
        },
    }

    lineGraphDataSet(config, port);
}

function lineGraphDataSet(config, port) {
    let colorindex = 0;
    let colorNames = [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)'
    ]
    let backNames = [
        'rgba(255, 99, 132, 0.1)',
        'rgba(54, 162, 235, 0.1)',
        'rgba(255, 206, 86, 0.1)',
        'rgba(75, 192, 192, 0.1)',
        'rgba(153, 102, 255, 0.1)',
        'rgba(255, 159, 64, 0.1)'
    ]
    let sort_trapic = ["_RX", "_TX"];

    for (let j = 0; j < sort_trapic.length; j++) {
        let newDataset = {
            label          : "트래픽" + sort_trapic[j].replace("_", " "),
            borderColor    : colorNames[colorindex],
            backgroundColor: backNames[colorindex],
            borderWidth    : 2,
            radius         : 2,
            data           : [],
            fill           : true
        }

        if(trapic.get(port + sort_trapic[j]) !== undefined) {
            for (let i = 0; i < trapic.get(port + sort_trapic[j]).length; i++) {
                let data = trapic.get(port + sort_trapic[j])[i];
                let xy_data = {
                    x: data.get("date"),
                    y: data.get("value")
                };
                newDataset.data.push(xy_data);
            }
        }
        // 데이터 반영
        config.data.datasets.push(newDataset);
        colorindex++;
    }
    // 라인 차트 업데이트
    myChartLine = new Chart(ctx2, config);
}

function getServerInfo() {
    let httpRequest;
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let j = 0;
                const table_id = document.querySelector('#server_list');
                const tbody = document.querySelector('.server_content');
                serverlist = [];
                cpulist = new Map();
                memorylist = new Map();
                disklist = new Map();
                let res = httpRequest.response;
                let tomcatportlist = [];

                /* 서버 슬라이드 및 관련 그래프를 만드는데 필요한 데이터 분류 */
                if (res !== null && res.length !== 0) {
                    while (tbody.hasChildNodes()) {
                        tbody.removeChild(tbody.firstChild);
                    }

                    let author = res.author;
                    let res_list = res.server_list;
                    for (let i = 0; i < res_list.length; i++) {
                        tableCreate(res_list[i], table_id, author);
                        tomcatportlist.push(res_list[i].tomcat_port);
                        if (i === res_list.length - 1 && res_list[j].server_name !== res_list[i].server_name) {
                            serverlist.push(res_list[j].server_name);
                            serverlist.push(res_list[i].server_name);
                            iplist.set(res_list[j].server_name, res_list[j].ip);
                            iplist.set(res_list[i].server_name, res_list[i].ip);
                            cpulist.set(res_list[j].server_name, Number(res_list[j].cpu.substring(0, res_list[j].cpu.length - 1)));
                            cpulist.set(res_list[i].server_name, Number(res_list[i].cpu.substring(0, res_list[i].cpu.length - 1)));
                            memorylist.set(res_list[j].server_name, Number(res_list[j].memory.substring(0, res_list[j].memory.length - 1)));
                            memorylist.set(res_list[i].server_name, Number(res_list[i].memory.substring(0, res_list[i].memory.length - 1)));
                            disklist.set(res_list[j].server_name, Number(res_list[j].disk.substring(0, res_list[j].disk.length - 1)));
                            disklist.set(res_list[i].server_name, Number(res_list[i].disk.substring(0, res_list[i].disk.length - 1)));
                            tomcatportlist.splice(tomcatportlist.length - 1);
                            tomcatportMap.set(res_list[j].server_name, tomcatportlist);
                            tomcatportlist = [];
                            tomcatportlist.push(res_list[i].tomcat_port);
                            tomcatportMap.set(res_list[i].server_name, tomcatportlist);
                        } else if ((res_list[j].ip !== res_list[i].ip) || (i === res_list.length - 1 && res_list[j].server_name === res_list[i].server_name)) {
                            serverlist.push(res_list[j].server_name);
                            iplist.set(res_list[j].server_name, res_list[j].ip);
                            cpulist.set(res_list[j].server_name, Number(res_list[j].cpu.substring(0, res_list[j].cpu.length - 1)));
                            memorylist.set(res_list[j].server_name, Number(res_list[j].memory.substring(0, res_list[j].memory.length - 1)));
                            disklist.set(res_list[j].server_name, Number(res_list[j].disk.substring(0, res_list[j].disk.length - 1)));
                            tomcatportlist.splice(tomcatportlist.length - 1);
                            if (i === res_list.length - 1) {
                                tomcatportlist.push(res_list[i].tomcat_port);
                            }
                            tomcatportMap.set(res_list[j].server_name, tomcatportlist);

                            tomcatportlist = [];
                            tomcatportlist.push(res_list[i].tomcat_port);
                        }
                        j = i;
                    }
                    /*서버 리스트 테이블을 만드는 함수*/
                    tableRowSpan(table_id);

                    /* 서버 선택창을 만드는 함수 */
                    selectOptionCreate();
                    /* Bar그래프를 불러오는 함수 */
                    createGraphBar(selectServer, cpulist, memorylist, disklist);
                    /* Line그래프를 불러오는 함수 */
                    trapicGraphData();
                    /* LogData를 불러오는 함수 */
                    getLogDataList("main");
                    /* 전체 서버 종료된 날짜 확인 */
                    allServerAlarm();
                } else {
                    popupClose("progress");
                    error_popupOpen(null, null, "request");
                    console.log("request error");
                }
            } else {
                popupClose("progress");
                error_popupOpen(null, null, "request");
                console.log("request error");
            }
        }
    }

    httpRequest.open('POST', "/server_list", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send();
}

function trapicGraphData() {
    let httpRequest;
    let data = {};
    httpRequest = new XMLHttpRequest();

    data.server_name = selectServer;
    data.ip = iplist.get(selectServer);
    data.tomcat_port = tomcatportMap.get(selectServer);

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let res = httpRequest.response;
                let port_label = tomcatportMap.get(selectServer);
                trapic = new Map();

                if (res !== null) {
                    for (let i = 0; i < port_label.length; i++) {
                        let tx_list = [];
                        let rx_list = [];
                        let port_length = 0;
                        for (let j = 0; j < res.length; j++) {
                            let tx_Map = new Map();
                            let rx_Map = new Map();
                            if (res[j].tomcat_port === port_label[i]) {
                                port_length++;
                                tx_Map.set('date', res[j].val_date);
                                tx_Map.set('value', res[j].trapic_tx);
                                rx_Map.set('date', res[j].val_date);
                                rx_Map.set('value', res[j].trapic_rx);
                                tx_list.push(tx_Map);
                                rx_list.push(rx_Map);
                            }
                        }
                        trapic_length = port_length;
                        trapic.set(port_label[i] + "_TX", tx_list);
                        trapic.set(port_label[i] + "_RX", rx_list);
                    }
                }

                /* 셀렉트 박스 다시 만들기 위해 삭제 */
                port_select.options.length = 0;

                /* PORT 셀렉트 박스 생성 */
                for (let i = 0; i < port_label.length; i++) {
                    let option = document.createElement('option');
                    option.value = port_label[i];
                    option.textContent = selectServer + " (" + port_label[i] + ")";
                    port_select.appendChild(option);
                }

                changePort();
            } else {
                console.log("request error");
            }
        }
    }

    httpRequest.open('POST', "/trapic_data", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send(JSON.stringify(data));
}

function tableCreate(data, table_id, author) {
    const newRow = table_id.insertRow();
    const newCell1 = newRow.insertCell(0);
    const newCell2 = newRow.insertCell(1);
    const newCell3 = newRow.insertCell(2);
    const newCell4 = newRow.insertCell(3);
    const newCell5 = newRow.insertCell(4);
    const newCell6 = newRow.insertCell(5);
    const newCell7 = newRow.insertCell(6);
    const newCell8 = newRow.insertCell(7);
    const newCell9 = newRow.insertCell(8);
    const newCell10 = newRow.insertCell(9);
    let system = data.system;
    let ip = data.ip;
    let server_name = data.server_name;
    let port = data.tomcat_port;
    let cpu = data.cpu;
    let memory = data.memory;
    let disk = data.disk;
    let port_status = data.status;
    let rx = data.trapic_rx;
    let tx = data.trapic_tx;

    const btn_blue = document.createElement('button');
    const btn_red = document.createElement('button');
    const span1 = document.createElement('span');
    const span2 = document.createElement('span');
    btn_blue.className = "btn blue";
    btn_red.className = "btn red";
    span1.innerText = "Θ"
    span2.innerText = "Θ"
    btn_blue.appendChild(span1);
    btn_red.appendChild(span2);

    newCell1.innerText = system;
    newCell2.innerText = ip;
    newCell3.innerText = server_name;
    newCell4.innerText = port;
    if (port_status === "가동") {
        newCell5.innerText = port_status;
        newCell5.style.color = "rgba(2, 126, 251, 1)";
        newCell5.style.fontWeight = "bold";
    } else if (port_status === "정지") {
        newCell5.innerText = port_status;
        newCell5.style.color = "rgba(251, 75, 2, 1)";
        newCell5.style.fontWeight = "bold";
    }
    newCell5.innerText = port_status;
    newCell6.innerText = rx;
    newCell7.innerText = tx;
    newCell8.innerText = cpu;
    newCell9.innerText = memory;
    newCell10.innerText = disk;
    if (author === "ADMIN") {
        const newCell11 = newRow.insertCell(10);
        const newCell12 = newRow.insertCell(11);

        newCell11.appendChild(btn_blue);
        newCell12.appendChild(btn_red);

        addListener(btn_blue, data, 'on', 'power', 'open');
        addListener(btn_red, data, 'off', 'power', 'open');
    }
}

function tableRowSpan(table_id) {
    let rows = table_id.getElementsByTagName("tr");
    let j;

    // tr만큼 루프돌면서 컬럼값 접근
    for (j = 1; j < rows.length; j++) {
        let i = 1;
        let cells1 = rows[j - 1].getElementsByTagName("td");
        let cells2 = rows[j].getElementsByTagName("td");
        let ip_span_check = false;
        let name_span_check = false;

        for (let r = 0; r < cells1.length; r++) {
            if (r === 0 || r === 1 || r === 2) {
                if (cells1[r].innerHTML === cells2[r].innerHTML) {
                    cells1[r].rowSpan = i + 1;
                    cells2[r].hidden = true;
                    if (r === 1) {
                        ip_span_check = true;
                    } else if (r === 2) {
                        name_span_check = true;
                    }
                }
            }

            if (ip_span_check) {
                if (r === 7 || r === 8 || r === 9) {
                    cells1[r].rowSpan = i + 1;
                    cells2[r].hidden = true;
                }
            }
            if (name_span_check) {
                if (r === 10 || r === 11) {
                    cells1[r].rowSpan = i + 1;
                    cells2[r].hidden = true;
                    if (r === 11) {
                        i++;
                    }
                }
            }
        }
    }
}

function popupOpen(data, action, process) {
    const input_ps = document.querySelectorAll('.popup_body > ul > li p');
    let datamap = new Map();

    for(let i = 0; i < input_ps.length; i++) {
        input_ps[i].style.display = 'none';
    }

    if (data !== undefined && data !== null) {
        datamap = ObjectToMap(data);
    }

    if (process === 'power') {
        popup.className = open;

        const div_btn = document.createElement("div");
        const div_sep = document.createElement("div");
        const btn_green = document.createElement('button');
        const btn_red = document.createElement('button');
        div_btn.className = "button_box"
        div_sep.className = "button_separator"
        btn_green.className = "button button-normal";
        btn_red.className = "button button-normal";
        btn_green.innerText = "확인"
        btn_red.innerText = "취소"
        div_btn.appendChild(btn_green);
        div_btn.appendChild(div_sep);
        div_btn.appendChild(btn_red);


        popupbody.replaceChildren();
        data.power = action;

        if (action === "on") {
            h1.textContent = datamap.get("server_name") + "서버를 구동 하시겠습니까?";
        } else if (action === "off") {
            h1.textContent = datamap.get("server_name") + "서버를 종료 하시겠습니까?";
        }

        popupbody.appendChild(h1);
        popupbody.appendChild(div_btn);
        addListener(btn_green, data, action, process, 'power_work');
        addListener(btn_red, data, action, process, 'close');
    } else if (process === 'server_plus') {
        const popup_wrapper_plus = document.querySelector(".popup_wrapper.server_plus");
        const btn_plus_submit = document.querySelector(".popup_body.server_plus > .button_box > .btn_submit");
        const btn_plus_cancel = document.querySelector(".popup_body.server_plus > .button_box > .btn_cancel");
        const server_plus_system = document.getElementById("server_plus_system");
        const server_plus_ip = document.getElementById("server_plus_ip");
        const server_plus_id = document.getElementById("server_plus_id");
        const server_plus_pw = document.getElementById("server_plus_password");
        const server_plus_name = document.getElementById("server_plus_server_name");
        const server_plus_port = document.getElementById("server_plus_server_port");
        const server_plus_tomcat_port = document.getElementById("server_plus_tomcat_port");
        const server_plus_tomcat_dir = document.getElementById("server_plus_tomcat_dir");
        popup_wrapper_plus.className = "popup_wrapper open server_plus";


        addinputListener(server_plus_system, "system", process);
        addinputListener(server_plus_ip, "ip", process);
        addinputListener(server_plus_id, "id", process);
        addinputListener(server_plus_pw, "password", process);
        addinputListener(server_plus_name, "server_name", process);
        addinputListener(server_plus_port, "server_port", process);
        addinputListener(server_plus_tomcat_port, "tomcat_port", process);
        addinputListener(server_plus_tomcat_dir, "tomcat_dir", process);
        addListener(btn_plus_submit, data, action, process, 'server_manage');
        addListener(btn_plus_cancel, data, action, process, 'close');
    } else if (process === 'server_delete') {
        const popup_wrapper_delete = document.querySelector(".popup_wrapper.server_delete");
        const btn_delete_submit = document.querySelector(".popup_body.server_delete > .button_box > .btn_submit");
        const btn_delete_cancel = document.querySelector(".popup_body.server_delete > .button_box > .btn_cancel");
        const server_delete_system = document.getElementById("server_delete_system");
        const server_delete_ip = document.getElementById("server_delete_ip");
        const server_delete_name = document.getElementById("server_delete_server_name");
        const server_delete_port = document.getElementById("server_delete_server_port");
        const server_delete_tomcat_port = document.getElementById("server_delete_tomcat_port");
        popup_wrapper_delete.className = "popup_wrapper open server_delete";

        addinputListener(server_delete_system, "system", process);
        addinputListener(server_delete_ip, "ip", process);
        addinputListener(server_delete_name, "server_name", process);
        addinputListener(server_delete_port, "server_port", process);
        addinputListener(server_delete_tomcat_port, "tomcat_port", process);
        addListener(btn_delete_submit, data, action, process, 'server_manage');
        addListener(btn_delete_cancel, data, action, process, 'close');
    } else if (process === 'progress') {
        const popup_wrapper_progress = document.querySelector(".popup_progress_back");
        popup_wrapper_progress.className = "popup_progress_back open";
    } else if (process === 'user_list') {
        getUserList();
    } else if (process === 'user_edit') {
        const popup_wrapper_edit = document.querySelector(".popup_wrapper.user_edit");
        const btn_edit_submit = document.querySelector(".popup_body.user_edit > .button_box > .btn_submit");
        const btn_edit_cancel = document.querySelector(".popup_body.user_edit > .button_box > .btn_cancel");
        const btn_edit_select = document.getElementById('user_edit_auth');
        const user_id = document.getElementById("user_edit_id");
        const user_pw = document.getElementById("user_edit_password");
        const username = document.getElementById("user_edit_username");
        const user_tel = document.getElementById("user_edit_tel");
        select_value = data.author;
        btn_edit_select.textContent = data.author;
        user_id.value = data.id;
        user_pw.value = "";
        username.value = data.username;
        user_tel.value = data.tel;
        pw_check_edit.style.display = "none";
        auth_check_edit.style.display = "none";
        tel_check_edit.style.display = "none";
        popup_wrapper_edit.className = "popup_wrapper open user_edit blur";
        select_function('user_edit');

        addinputListener(user_id, "id", "user_edit");
        addinputListener(user_pw, "pw", "user_edit");
        addinputListener(username, "username", "user_edit");
        addinputListener(user_tel, "tel", "user_edit");
        addListener(btn_edit_submit, data, action, process, 'user_manage');
        addListener(btn_edit_cancel, data, action, process, 'close');
    } else if (process === 'user_plus') {
        const popup_wrapper_user_plus = document.querySelector(".popup_wrapper.user_plus");
        const btn_user_plus_submit = document.querySelector(".popup_body.user_plus > .button_box > .btn_submit");
        const btn_user_plus_cancel = document.querySelector(".popup_body.user_plus > .button_box > .btn_cancel");
        const user_plus_id = document.getElementById('user_plus_id');
        const user_plus_pw = document.getElementById('user_plus_password');
        const user_plus_name = document.getElementById('user_plus_username');
        const user_plus_auth = document.getElementById('user_plus_auth');
        const user_plus_tel = document.getElementById('user_plus_tel');
        popup_wrapper_user_plus.className = "popup_wrapper open user_plus";
        user_plus_auth.innerText = "사용자 권한";
        user_plus_auth.style.color = "#D3D3D3";
        select_function('user_plus');
        user_plus_id.style.width = "229px";
        user_plus_id.value = "";
        ok_img.style.display = "none";
        id_check_btn.style.display = "block";
        id_check_plus.style.display = "none";
        pw_check_plus.style.display = "none";
        auth_check_plus.style.display = "none";
        tel_check_plus.style.display = "none";
        id_check_result = undefined;
        user_plus_id.disabled = false;
        user_plus_pw.value = "";
        user_plus_name.value = "";
        user_plus_tel.value = "";

        addinputListener(user_plus_id, "id", "user_plus");
        addinputListener(user_plus_pw, "pw", "user_plus");
        addinputListener(user_plus_name, "username", "user_plus");
        addinputListener(user_plus_tel, "tel", "user_plus");
        addListener(btn_user_plus_submit, data, action, process, 'user_manage');
        addListener(btn_user_plus_cancel, data, action, process, 'close');
    } else if (process === 'user_delete') {
        popup.className = open;
        const popup_user_list = document.querySelector('.popup_wrapper.user_list');
        popup_user_list.style.zIndex = "800";
        const btn_box = document.createElement("div");
        const btn_submit = document.createElement("button");
        popupbody.replaceChildren();
        if (action === "empty") {
            h1.textContent = "체크박스가 비어있습니다. 삭제하고싶은 사용자를 체크하시고 삭제버튼을 눌러주세요."
            popupbody.appendChild(h1);
        } else {
            for (let i = 0; i < data.length; i++) {
                const h = document.createElement('h1');

                if (action === "ok") {
                    h.textContent = data[i].toString() + " 사용자를 삭제하는데 성공했습니다.";
                } else if (action === "nok") {
                    h.textContent = data[i].toString() + " 사용자를 삭제하는데 실패했습니다.";
                } else if (action === "error") {
                    h.textContent = "사용자 삭제중에 오류가 발생했습니다. 다시 시도해주시기 바랍니다.";
                    break;
                }

                popupbody.appendChild(h);
            }
        }
        btn_submit.className = "button button-normal btn_submit";
        btn_submit.textContent = "확인";
        btn_box.className = "button_box";
        btn_box.appendChild(btn_submit);
        popupbody.appendChild(btn_box);
        addListener(btn_submit, data, action, 'power', 'close');
        getUserList();
    } else if (process === 'server_log') {
        let today = new Date();
        const popup_log_select = document.getElementById('time_select');
        popup_log_select.replaceChildren();

        if (today.getHours() === 0) {
            let option1 = document.createElement('option');
            let option2 = document.createElement('option');

            option1.value = "00:00";
            option2.value = "01:00";

            popup_log_select.appendChild(option1);
            popup_log_select.appendChild(option2);
        } else {
            for (let i = 0; i < today.getHours() + 1; i++) {
                let hour = today.getHours() - i;
                let nowHour = ('0' + hour).slice(-2);
                let nextHour = ('0' + (hour + 1)).slice(-2);
                let option = document.createElement('option');
                option.value = nowHour;
                option.textContent = nowHour + ":00 ~ " + nextHour + ":00";
                popup_log_select.appendChild(option);
            }
        }

        const popup_server_log = document.querySelector(".popup_wrapper.server_log");
        popup_server_log.className = "popup_wrapper open server_log";
        getLogDataList("popup");
    }
}

function result_popupOpen(data, action, process) {
    let datamap = new Map();

    if (data !== undefined && data !== null) {
        datamap = ObjectToMap(data);
    }

    popup.className = open;

    const popup_user_list = document.querySelector('.popup_wrapper.user_list');
    const btn_box = document.createElement("div");
    const btn_submit = document.createElement("button");

    popupbody.replaceChildren();
    btn_box.className = "button_box"
    btn_submit.className = "button button-normal btn_submit";
    if (process === "server_plus") {
        h1.textContent = datamap.get("server_name") + " 서버를 추가하는데 성공했습니다.";
    } else if (process === "server_delete") {
        h1.textContent = datamap.get("server_name") + " 서버를 삭제하는데 성공했습니다.";
    } else if (process === "user_plus") {
        popup_user_list.style.zIndex = "800";
        h1.textContent = datamap.get("id") + " 사용자를 추가하는데 성공했습니다.";
    } else if (process === "user_edit") {
        popup_user_list.style.zIndex = "800";
        popupbody.style.width = "auto";
        popuphead.style.width = "auto";
        h1.textContent = datamap.get("id") + " 사용자 정보를 수정하는데 성공했습니다.";
    }

    btn_submit.textContent = "확인";
    btn_box.appendChild(btn_submit);

    popupbody.appendChild(h1);
    popupbody.appendChild(btn_box);
    addListener(btn_submit, data, action, 'power', 'close');

    if (process.indexOf('user') >= 0) {
        getUserList();
    }
}

function error_popupOpen(data, action, process) {
    let datamap = new Map();

    if (data !== undefined && data !== null) {
        datamap = ObjectToMap(data);
    }

    popup.className = open;

    const popup_user_list = document.querySelector('.popup_wrapper.user_list');
    const popup_plus = document.querySelector('.popup_wrapper.server_plus');
    const btn_box = document.createElement("div");
    const btn_submit = document.createElement("button");

    popupbody.replaceChildren();
    popup_plus.style.zIndex = "800";
    btn_box.className = "button_box"
    btn_submit.className = "button button-normal btn_submit";

    if (process === "server_plus") {
        const popup_plus = document.querySelector('.popup_wrapper.server_plus');
        popup_plus.style.zIndex = "800";
        if (action === 'detect') {
            h1.textContent = data.server_name + " 서버가 이미 리스트에 존재합니다.";
        } else {
            h1.textContent = data.server_name + " 서버를 추가하는데 실패했습니다.";
        }
    } else if (process === "server_delete") {
        const popup_delete = document.querySelector('.popup_wrapper.server_delete');
        popup_delete.style.zIndex = "800";
        h1.textContent = "입력하신 정보의 " + datamap.get("server_name") + "가 서버리스트에 없습니다.";
    } else if (process === "user_plus") {
        const popup_user_plus = document.querySelector('.popup_wrapper.user_plus');
        popup_user_list.style.zIndex = "700";
        popup_user_plus.style.zIndex = "800";
        h1.textContent = datamap.get("id") + " 사용자를 추가하는데 실패했습니다.";
    } else if (process === "user_edit") {
        const popup_user_edit = document.querySelector('.popup_wrapper.user_edit');
        popup_user_list.style.zIndex = "700";
        popup_user_edit.style.zIndex = "800";
        h1.textContent = datamap.get("id") + " 사용자 정보를 수정하는데 실패했습니다.";
    } else if (process === "request") {
        h1.textContent = "세션이 만료되었습니다. 다시 로그인 해주시기 바랍니다."
    }

    btn_submit.textContent = "확인";
    btn_box.appendChild(btn_submit);

    popupbody.appendChild(h1);
    popupbody.appendChild(btn_box);

    if(process === "request") {
        btn_submit.addEventListener("click", () => {window.location.reload()});
    } else {
        addListener(btn_submit, data, action, 'power', 'close');
    }

    if (process.indexOf('user') >= 0) {
        getUserList();
    }
}

function popupClose(process) {
    if (process === 'power') {
        popup.className = "popup_wrapper close";
        popupbody.replaceChildren();
    } else if (process === 'server_plus') {
        const popup_plus = document.querySelector(".popup_wrapper.server_plus");
        popup_plus.className = "popup_wrapper close server_plus";
    } else if (process === 'server_delete') {
        const popup_plus = document.querySelector(".popup_wrapper.server_delete");
        popup_plus.className = "popup_wrapper close server_delete";
    } else if (process === 'progress') {
        const popup_wrapper_progress = document.querySelector(".popup_progress_back");
        popup_wrapper_progress.className = "popup_progress_back close";
    } else if (process === 'user_list') {
        const popup_wrapper_userlist = document.querySelector(".popup_wrapper.user_list");
        popup_wrapper_userlist.className = "popup_wrapper close user_list";
    } else if (process === 'user_edit') {
        const popup_wrapper_auth = document.querySelector(".popup_wrapper.user_edit");
        popup_wrapper_auth.className = "popup_wrapper close user_edit";
        select_value = undefined;
    } else if (process === 'user_plus') {
        const popup_user_plus = document.querySelector(".popup_wrapper.user_plus");
        popup_user_plus.className = "popup_wrapper close user_plus";
        select_value = undefined;
    } else if (process === 'server_log') {
        const popup_server_log = document.querySelector(".popup_wrapper.server_log");
        popup_server_log.className = "popup_wrapper close server_log";
    }
}

function power_work(data, action) {
    popupClose('power');
    popupbody.replaceChildren();
    let datamap = new Map();
    let httpRequest;
    httpRequest = new XMLHttpRequest();

    for (let key of Object.keys(data)) {
        const value = data[key];
        datamap.set(key, value);
    }

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            popupClose("progress");
            popup.className = open;
            if (httpRequest.status === 200) {
                let res = httpRequest.response;
                const btn_box = document.createElement("div");
                const btn_submit = document.createElement("button");
                btn_box.className = "button_box"
                btn_submit.className = "button button-normal btn_submit";
                btn_submit.textContent = "확인";
                btn_box.appendChild(btn_submit);

                if (res.result === "ok") {
                    if (action === "on") {
                        h1.textContent = datamap.get("server_name") + "서버를 구동 하였습니다.";
                    } else if (action === "off") {
                        h1.textContent = datamap.get("server_name") + "서버를 종료 하였습니다.";
                    }
                } else if (res.result === "reason") {
                    if (action === "on") {
                        h1.textContent = datamap.get("server_name") + "서버가 이미 작동중입니다.";
                    } else if (action === "off") {
                        h1.textContent = datamap.get("server_name") + "서버가 이미 종료되있습니다.";
                    }
                } else {
                    if (action === "on") {
                        h1.textContent = datamap.get("server_name") + "서버 구동에 실패했습니다.";
                    } else if (action === "off") {
                        h1.textContent = datamap.get("server_name") + "서버 종료에 실패했습니다.";
                    }
                }

                popupbody.appendChild(h1);
                popupbody.appendChild(btn_box);
                addListener(btn_submit, data, action, 'power', 'close');
                getServerInfo();
            } else {
                if (action === "on") {
                    h1.textContent = datamap.get("server_name") + "서버 구동에 실패했습니다.";
                } else if (action === "off") {
                    h1.textContent = datamap.get("server_name") + "서버 종료에 실패했습니다.";
                }
                popupbody.appendChild(h1);
                console.log("request error");
            }
        }
    }

    httpRequest.open('POST', "/power", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send(JSON.stringify(data));
}

function select_function(process) {
    const btn_select = document.getElementById(process + "_auth");
    const list_select = document.getElementById(process + "-member");
    const popup_wrapper_select = document.querySelector(".popup_wrapper." + process);
    const popup_head_select = document.querySelector(".popup_head." + process);
    const popup_body_select = document.querySelector(".popup." + process);

    if (btn_select.clickHandler) {
        btn_select.removeEventListener('click', btn_select.clickHandler);
    }
    if (list_select.clickHandler) {
        list_select.removeEventListener('click', list_select.clickHandler);
    }
    if (popup_wrapper_select.clickHandler) {
        popup_wrapper_select.removeEventListener('click', popup_wrapper_select.clickHandler);
    }
    if (popup_head_select.clickHandler) {
        popup_head_select.removeEventListener('click', popup_head_select.clickHandler);
    }
    if (popup_body_select.clickHandler) {
        popup_body_select.removeEventListener('click', popup_body_select.clickHandler);
    }

    btn_select.clickHandler = () => {
        if (select_value !== "사용자 권한" && select_value !== undefined) {
            btn_select.innerText = select_value;
        } else {
            btn_select.innerText = "사용자 권한";
        }
        btn_select.classList.toggle('on');
    };
    list_select.clickHandler = (event) => {
        if (event.target.nodeName === "BUTTON") {
            select_value = event.target.innerText;
            btn_select.innerText = event.target.innerText;
            btn_select.classList.remove('on');
            if (process === "user_plus") {
                btn_select.style.color = "#ffffff";
            }
        } else {
            btn_select.innerText = "사용자 권한";
            btn_select.classList.remove('on');
        }
    };
    popup_wrapper_select.clickHandler = (event) => {
        if (event.target.tagName !== "BUTTON") {
            if (select_value !== "사용자 권한" && select_value !== undefined) {
                btn_select.innerText = select_value;
            }
            btn_select.classList.remove('on');
        }
    };
    popup_head_select.clickHandler = () => {
        if (select_value !== "사용자 권한" && select_value !== undefined) {
            btn_select.innerText = select_value;
        }
        btn_select.classList.remove('on');
    };
    popup_body_select.clickHandler = (event) => {
        if (event.target.tagName !== "BUTTON") {
            if (select_value !== "사용자 권한" && select_value !== undefined) {
                btn_select.innerText = select_value;
            }
            btn_select.classList.remove('on');
        }
    };

    btn_select.addEventListener('click', btn_select.clickHandler);
    list_select.addEventListener('click', list_select.clickHandler);
    popup_wrapper_select.addEventListener('click', popup_wrapper_select.clickHandler);
    popup_head_select.addEventListener('click', popup_head_select.clickHandler);
    popup_body_select.addEventListener('click', popup_body_select.clickHandler);
}

/* 메뉴얼 다운로드 */
async function manual_download() {
    btn_manual.removeEventListener('click', () => manual_download());
    const response = await fetch('/resource/manual/manual.docx');
    const file = await response.blob();
    const downloadUrl = window.URL.createObjectURL(file); // 해당 file을 가리키는 url 생성
    const aElement = document.createElement('a');

    document.body.appendChild(aElement);
    aElement.download = 'KevinLAB 서비스 관리 시스템 메뉴얼'; // a tag에 download 속성을 줘서 클릭할 때 다운로드가 일어날 수 있도록 하기
    aElement.href = downloadUrl; // href에 url 달아주기

    aElement.click(); // 코드 상으로 클릭을 해줘서 다운로드를 트리거

    document.body.removeChild(aElement); // cleanup - 쓰임을 다한 a 태그 삭제
    window.URL.revokeObjectURL(downloadUrl); // cleanup - 쓰임을 다한 url 객체 삭제
    btn_manual.addEventListener('click', () => manual_download());
}

function id_duplication_check(process) {
    const input_id = document.getElementById(process + '_id');
    let id = input_id.value;
    let httpRequest;
    if (isStringValue(id)) {
        let data = {};
        data.id = id;
        httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = () => {
            popupOpen(null, null, "progress");
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                popupClose("progress");
                if (httpRequest.status === 200) {
                    let res = httpRequest.response;
                    let check = res.result;
                    if (check === "ok") {
                        id_check_btn.style.display = "none";
                        user_plus_id.style.width = "277px";
                        user_plus_id.disabled = true;
                        ok_img.style.display = "block";
                        id_check_plus.style.display = "none";

                        id_check_result = true;
                    } else if (check === "error") {
                        id_check_plus.innerText = "중복된 ID 확인중에 오류가 발생했습니다."
                        id_check_plus.style.display = "block";
                        popup_user_plus.style.overflow = 'hidden';

                        id_check_result = false;
                    } else if (check === "nok") {
                        id_check_plus.innerText = "중복된 ID가 존재합니다. 다른 ID를 입력해주세요.";
                        id_check_plus.style.display = "block";
                        popup_user_plus.style.overflow = 'hidden';

                        id_check_result = false;
                    }
                } else {
                    console.log("request error");
                }
            }
        }

        httpRequest.open('POST', "/id_check", true);
        httpRequest.responseType = "json";
        httpRequest.setRequestHeader('Content-Type', 'application/json');
        httpRequest.setRequestHeader(header, token);
        httpRequest.send(JSON.stringify(data));
    } else {
        id_check_plus.innerText = "ID가 입력되지 않았습니다."
        id_check_plus.style.display = "block";
        popup_user_plus.style.overflow = 'hidden';
    }
}

function id_check(process) {
    if (process === "user_edit") {
        id_check_result = true;
        return true;
    } else if (process === "user_delete") {
        return true;
    } else {
        const id_input = document.getElementById(process + '_id');
        let id = id_input.value;
        if (isStringValue(id)) {
            if (id_check_result === undefined) {
                id_check_plus.innerText = "ID 중복체크를 하지 않았습니다. ID 중복체크를 해주세요."
                id_check_plus.style.display = "block";
                popup_user_plus.style.overflow = 'hidden';
                return false;
            } else if (id_check_result === true) {
                id_check_btn.style.display = "none";
                user_plus_id.style.width = "277px";
                user_plus_id.disabled = true;
                ok_img.style.display = "block";
                id_check_plus.style.display = "none";
                return true;
            } else {
                id_check_result = undefined;
                id_check_plus.innerText = "ID 중복체크를 하지 않았습니다. ID 중복체크를 해주세요."
                id_check_plus.style.display = "block";
                popup_user_plus.style.overflow = 'hidden';
                return false;
            }
        } else {
            id_check_plus.innerText = "ID가 입력되지 않았습니다."
            id_check_plus.style.display = "block";
            popup_user_plus.style.overflow = 'hidden';
            return false;
        }
    }
}

function pw_check(process) {
    if (process === "user_delete") {
        return true;
    } else {
        const popup = document.querySelector(".popup." + process);
        const id_input = document.getElementById(process + '_id');
        const pw_input = document.getElementById(process + '_password');
        const pw_p = document.getElementById('password_check_' + process);
        let id = id_input.value;
        let pw = pw_input.value;
        let checkNumber = pw.search(/[0-9]/g);
        let checkEnglish = pw.search(/[a-z]/ig);
        if (isStringValue(pw)) {
            if (!/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/.test(pw)) {
                pw_p.innerText = `숫자+영문자+특수문자 조합으로 8~20자리 사용해야 합니다.`;
                pw_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            } else if (checkNumber < 0 || checkEnglish < 0) {
                pw_p.innerText = "숫자와 영문자를 혼용하여야 합니다.";
                pw_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            } else if (/(\w)\1\1\1/.test(pw)) {
                pw_p.innerText = "같은 문자를 4번 이상 사용하실 수 없습니다.";
                pw_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            } else if (pw.search(id) > -1) {
                pw_p.innerText = "비밀번호에 아이디가 포함되었습니다.";
                pw_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            } else {
                pw_p.style.display = 'none';
                return true;
            }
        } else {
            if(process === "user_edit") {
                pw_p.style.display = 'none';
                return true;
            } else {
                pw_p.innerText = "PW를 입력하지 않았습니다. 패스워드를 입력해주세요.";
                pw_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            }
        }
    }
}

function auth_check(process) {
    if (process === "user_delete") {
        return true;
    } else {
        const popup = document.querySelector(".popup." + process);
        const auth_select = document.getElementById(process + '_auth');
        const auth_p = document.getElementById('auth_check_' + process);
        let auth = auth_select.textContent;
        if (auth === "사용자 권한") {
            auth_p.style.display = 'block';
            popup.style.overflow = 'hidden';

            return false;
        } else {
            auth_p.style.display = 'none';

            return true;
        }
    }
}

function tel_check(process) {
    if (process === "user_delete") {
        return true;
    } else {
        let regexTel = /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/;
        const popup = document.querySelector(".popup." + process);
        const tel_input = document.getElementById(process + '_tel');
        const tel_p = document.getElementById('tel_check_' + process);
        let tel = tel_input.value;

        if (isStringValue(tel)) {
            if (regexTel.test(tel)) {
                tel_p.style.display = 'none';
                return true;
            } else {
                tel_p.innerText = "전화번호는 숫자만 가능하고, 형식은 000-0000-0000으로\n-를 포함하여 입력해주세요.";
                tel_p.style.display = 'block';
                popup.style.overflow = 'hidden';
                return false;
            }
        } else {
            tel_p.innerText = '전화번호를 입력하셔야 됩니다.'
            tel_p.style.display = 'block';
            popup.style.overflow = 'hidden';
            return false;
        }
    }
}

function ip_check(process) {
    let regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    const popup = document.querySelector(".popup." + process);
    const ip_input = document.getElementById(process + '_ip');
    const ip_p = document.getElementById('ip_check_' + process);
    let ip = ip_input.value;

    if (isStringValue(ip)) {
        if (!regexIP.test(ip)) {
            ip_p.innerText = "IP는 숫자만 사용가능하고\nIP는 000.000.000.000형식으로 작성해야합니다";
            ip_p.style.display = 'block';
            popup.style.overflow = 'hidden';
            return false;
        } else {
            ip_p.style.display = 'none';

            return true;
        }
    } else {
        ip_p.innerText = 'IP를 입력하셔야됩니다.'
        ip_p.style.display = 'block';
        popup.style.overflow = 'hidden';

        return false;
    }
}

function port_check(process, portname) {
    console.log("process : ", process);
    console.log("portname : ",  portname);
    let regexPort = /(6553[0-5]|655[0-2]\d|65[0-4]\d{2}|6[0-4]\d{3}|5\d{4}|[0-9]\d{0,3})/;
    const popup = document.querySelector(".popup." + process);
    const port_input = document.getElementById(process + "_" + portname);
    const port_p = document.getElementById(portname + '_check_' + process);
    let port = port_input.value;

    if (isStringValue(port)) {
        if (!regexPort.test(port)) {
            port_p.innerText = '포트는 65535이하의 숫자로 입력하셔야 됩니다.';
            port_p.style.display = 'block';
            popup.style.overflow = 'hidden';
            return false;
        } else {
            port_p.style.display = 'none';

            return true;
        }
    } else {
        port_p.innerText = '포트번호를 입력하셔야됩니다.';
        port_p.style.display = 'block';
        popup.style.overflow = 'hidden';

        return false;
    }
}

function system_check(process) {
    const popup = document.querySelector(".popup." + process);
    const system_input = document.getElementById(process + '_system');
    const system_p = document.getElementById('system_check_' + process);
    let system = system_input.value;

    if (!isStringValue(system)) {
        system_p.innerText = "시스템이 입력되지 않았습니다."
        system_p.style.display = "block";
        popup.style.overflow = 'hidden';

        return false;
    } else {
        system_p.style.display = 'none';

        return true;
    }
}

function name_check(process, checkname) {
    if (process === "user_delete") {
        return true;
    } else {
        const popup = document.querySelector(".popup." + process);
        const input = document.getElementById(process + "_" + checkname);
        const name_p = document.getElementById(checkname + '_check_' + process);
        console.log(process + "_" + checkname);
        let name = input.value;

        if (!isStringValue(name)) {
            if (checkname === "server_name") {
                name_p.innerText = "서버 이름이 입력되지 않았습니다."
            } else if (checkname === "id") {
                name_p.innerText = "서버 로그인 ID가 입력되지 않았습니다."
            } else if (checkname === "password") {
                name_p.innerText = "서버 로그인 PW가 입력되지 않았습니다."
            } else if (checkname === "tomcat_dir") {
                name_p.innerText = "톰캣 폴더 위치가 입력되지 않았습니다."
            } else if (checkname === "username") {
                name_p.innerText = "사용자의 이름이 입력되지 않았습니다."
            }
            name_p.style.display = "block";
            popup.style.overflow = 'hidden';

            return false;
        } else {
            if (name_p !== null && name_p !== undefined) {
                name_p.style.display = 'none';
            } else {
                return true;
            }
            return true;
        }
    }
}

function user_management(process) {
    const user_id = document.getElementById(process + '_id');
    const user_pw = document.getElementById(process + '_password');
    const user_auth = document.getElementById(process + '_auth');
    const user_tel = document.getElementById(process + '_tel');
    const username = document.getElementById(process + '_username');
    let checkbox = document.querySelectorAll("input[name=userlist_check]:checked");
    let pw_check_result = false;
    let id;
    let pw;
    let name;
    let auth;
    let tel;
    let httpRequest;
    httpRequest = new XMLHttpRequest();
    if (process !== "user_delete") {
        id = user_id.value;
        pw = user_pw.value;
        name = username.value;
        auth = user_auth.innerText;
        tel = user_tel.value;
    } else {
        if (checkbox.length <= 0) {
            popupOpen(null, "empty", process);
            return;
        }
    }
    if (id_check(process)) {
        let data = {}
        let userList = []
        if (process === "user_edit") {
            if (isStringValue(pw)) {
                pw_check_result = pw_check(process);
                data.password = pw;
            } else {
                pw_check_result = true;
                data.password = null;
            }
        } else {
            pw_check_result = pw_check(process);
            if (pw_check_result === true) {
                data.password = pw;
            }
        }
        if (pw_check_result && name_check(process, "username") && auth_check(process)
            && tel_check(process)) {
            if (process !== "user_delete") {
                data.id = id;
                data.username = name;
                data.author = auth;
                data.tel = tel;
            } else {
                for (let i = 0; i < checkbox.length; i++) {
                    data = {};
                    // checkbox.item(i).parentElement : 테이블 td, checkbox.item(i).parentElement.parentELement : 테이블 tr;
                    const tds = checkbox.item(i).parentElement.parentElement.children;
                    data.id = tds.item(1).textContent;
                    data.username = tds.item(2).textContent;
                    data.auth = tds.item(3).textContent;
                    data.tel = tds.item(4).textContent;
                    userList.push(data);
                }
            }

            httpRequest.onreadystatechange = () => {
                popupOpen(null, null, "progress");
                if (httpRequest.readyState === XMLHttpRequest.DONE) {
                    popupClose("progress");
                    if (httpRequest.status === 200) {
                        let res = httpRequest.response;
                        let nokcount = 0;
                        let errorcount = 0;
                        let okList = [];
                        let nokList = [];
                        if (process === "user_delete") {
                            for (let i = 0; i < res.length; i++) {
                                let result = res[i].result;
                                if (result === "ok") {
                                    okList.push(res[i].id);
                                } else if (result === "nok") {
                                    nokcount++;
                                    nokList.push(res[i].id);
                                } else if (result === "error") {
                                    nokcount++;
                                    errorcount++;
                                }
                            }
                            if (nokcount > 0) {
                                if (errorcount > 0) {
                                    popupOpen(nokList, "error", process);
                                } else {
                                    popupOpen(nokList, "nok", process);
                                }
                            } else {
                                popupOpen(okList, "ok", process);
                            }
                        } else {
                            let result = res.result;

                            if (result === "ok") {
                                popupClose(process);
                                result_popupOpen(data, "ok", process);
                            } else if (result === "nok") {
                                error_popupOpen(data, "nok", process);
                            }
                        }
                    } else {
                        console.log("request error");
                    }
                }
            }

            httpRequest.open('POST', "/" + process, true);
            httpRequest.responseType = "json";
            httpRequest.setRequestHeader('Content-Type', 'application/json');
            httpRequest.setRequestHeader(header, token);
            if (process !== "user_delete") {
                httpRequest.send(JSON.stringify(data));
            } else {
                httpRequest.send(JSON.stringify(userList));
            }
        }
    }
}

function server_management(process) {
    const server_system = document.getElementById(process + "_system");
    const server_ip = document.getElementById(process + "_ip");
    const server_id = document.getElementById(process + "_id");
    const server_pw = document.getElementById(process + "_password");
    const server_name = document.getElementById(process + "_server_name");
    const server_port = document.getElementById(process + "_server_port");
    const tomcat_port = document.getElementById(process + "_tomcat_port");
    let tomcat_dir;
    let system = server_system.value;
    let ip = server_ip.value;
    let id;
    let pw;
    let name = server_name.value;
    let serverport = server_port.value;
    let tomcatport = tomcat_port.value;
    let tomcatdir;
    let id_check_result = false;
    let pw_check_result = false;
    let dir_check_result = false;
    if (process === "server_plus") {
        id = server_id.value;
        pw = server_pw.value;

        tomcat_dir = document.getElementById(process + '_tomcat_dir');
        tomcatdir = tomcat_dir.value;
    } else if (process === "server_delete") {
        id_check_result = true;
        pw_check_result = true;
        dir_check_result = true;
        tomcatdir = "delete";
    }

    if (system_check(process) && ip_check(process)
        && (id_check_result || name_check(process, "id"))
        && (pw_check_result || name_check(process, "password"))
        && name_check(process, "server_name") && port_check(process, "server_port") && port_check(process, "tomcat_port")
        && (dir_check_result || name_check(process, "tomcat_dir"))) {
        let data = {}
        data.system = system;
        data.ip = ip;
        data.id = id;
        data.pw = pw;
        data.server_name = name;
        data.server_port = serverport;
        data.tomcat_port = tomcatport;
        data.tomcat_dir = tomcatdir;

        let httpRequest;
        httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = () => {
            popupOpen(null, null, "progress");
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                popupClose("progress");
                if (httpRequest.status === 200) {
                    let res = httpRequest.response;
                    if (res.result === "ok") {
                        popupClose(process);
                        result_popupOpen(data, null, process);
                    } else if (res.result === "detect") {
                        error_popupOpen(data, "detect", process);
                    } else {
                        error_popupOpen(data, null, process);
                    }
                    getServerInfo();
                } else {
                    console.log("request error");
                }
            }
        }

        httpRequest.open('POST', process, true);
        httpRequest.responseType = "json";
        httpRequest.setRequestHeader('Content-Type', 'application/json');
        httpRequest.setRequestHeader(header, token);
        httpRequest.send(JSON.stringify(data));
    }
}

function getUserList() {
    let httpRequest;
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            popupClose("progress");
            if (httpRequest.status === 200) {
                let userList = httpRequest.response;

                const popup_wrapper_userlist = document.querySelector(".popup_wrapper.user_list");
                const btn_list_submit = document.querySelector(".popup_body.user_list > .button_box > .btn_submit");
                const btn_list_cancel = document.querySelector(".popup_body.user_list > .button_box > .btn_cancel");
                popup_wrapper_userlist.className = "popup_wrapper open user_list";

                const table = document.getElementById('user_list');
                const tbody = document.querySelector('#user_list > tbody');

                while (tbody.hasChildNodes()) {
                    tbody.removeChild(tbody.firstChild);
                }

                for (let i = 0; i < userList.length; i++) {
                    if (userList[i].author === "ADMIN") {
                        userList[i].author = "관리자";
                    } else if (userList[i].author === "USER") {
                        userList[i].author = "일반 사용자";
                    }
                    const newRow = table.insertRow();
                    const newCell1 = newRow.insertCell(0);
                    const newCell2 = newRow.insertCell(1);
                    const newCell3 = newRow.insertCell(2);
                    const newCell4 = newRow.insertCell(3);
                    const newCell5 = newRow.insertCell(4);
                    const newCell6 = newRow.insertCell(5);

                    newCell1.innerHTML = '<input class="userlist_check" type="checkbox" name="userlist_check"/>';
                    newCell2.innerText = userList[i].id;
                    newCell3.innerText = userList[i].username;
                    newCell4.innerText = userList[i].author;
                    newCell5.innerText = userList[i].tel;
                    newCell6.innerHTML = '<button class="button button-normal btn_edit">수정</button>';

                    const btn_edit = newRow.querySelector('.btn_edit');
                    addListener(btn_edit, userList[i], null, "user_edit", "open");
                }
                addListener(btn_list_submit, null, null, "user_plus", "open");
                addListener(btn_list_cancel, null, null, "user_delete", "user_manage");
            } else {
                console.log("request error");
            }
        }
    }

    httpRequest.open('POST', "/user_list", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send();
}

function getLogDataList(process) {
    let httpRequest;
    let data = {};
    httpRequest = new XMLHttpRequest();
    const left_content = document.querySelector('.left-content');
    const serverlog_body = document.querySelector('.alarm_list_body.server');
    const log_icon = document.querySelector('.log_icon');
    const popup_log_select = document.getElementById('time_select');
    const serverlog_ul = document.createElement('ul');
    let selectHours;
    serverlog_ul.className = 'alarm_content server';
    popup_log_select.blur();

    data.server_name = selectServer;
    data.ip = iplist.get(selectServer);
    if (process === "main") {
        serverlog_body.replaceChildren();
        let today = new Date();
        data.hours = ('0' + today.getHours()).slice(-2);
        data.process = "main";
    } else if (process === "popup") {
        selectHours = popup_log_select.options[popup_log_select.selectedIndex].value;
        data.hours = selectHours;
        data.process = "popup";
    }

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let res = httpRequest.response;

                if (process === "main") {
                    if(res != null) {
                        if (res.list !== null && res.list.length !== 0) {
                            left_content.style.width = "30%";
                            log_icon.style.display = 'inline-block';

                            for (let i = 0; i < res.list.length; i++) {
                                const li = document.createElement("li");
                                li.textContent = "( " + res.list[i].val_date + " ) " + res.list[i].log;

                                serverlog_body.appendChild(serverlog_ul);
                                serverlog_ul.appendChild(li);
                            }
                        } else {
                            left_content.style.width = "33%";
                            const p = document.createElement('p');
                            p.textContent = "해당 서버의 최근에 기록된 로그가 없습니다."

                            log_icon.style.display = 'none';
                            serverlog_body.appendChild(p);
                        }
                    }
                } else if (process === "popup") {
                    findAlllog(res);
                }
                popupClose("progress");
            } else {
                console.log("request error");
                popupClose("progress");
            }
        }
    }

    httpRequest.open('POST', "/log_data", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send(JSON.stringify(data));
}

function allServerAlarm() {
    let httpRequest;
    let data = {};
    httpRequest = new XMLHttpRequest();
    const alarm_body = document.querySelector(".alarm_list_body.all");
    const alarm_ul = document.createElement('ul');
    alarm_ul.className = "alarm_content all"

    alarm_body.replaceChildren();

    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                alarm_body.replaceChildren();
                let res = httpRequest.response;

                if (res !== null && res.length !== 0) {
                    for (let i = 0; i < res.length; i++) {
                        if (res[i].end_date != null) {
                            const li_name = document.createElement("li");
                            const li_location = document.createElement("li");
                            const li_tomcatport = document.createElement("li");
                            const li_enddate = document.createElement("li");

                            li_name.textContent = res[i].server_name;
                            li_location.textContent = "위치 : " + res[i].system + "/" + res[i].ip + "/" + res[i].server_name;
                            li_tomcatport.textContent = "포트번호 : " + res[i].tomcat_port;
                            li_enddate.textContent = "종료 시간 : " + date_parse(res[i].end_date);

                            alarm_body.appendChild(alarm_ul);
                            alarm_ul.appendChild(li_name);
                            alarm_ul.appendChild(li_location);
                            alarm_ul.appendChild(li_tomcatport);
                            alarm_ul.appendChild(li_enddate);
                        }
                    }
                } else {
                    const p = document.createElement("p");

                    p.textContent = "최근 일주일 동안 발생한 알람이 없습니다."

                    alarm_body.appendChild(p);
                }
            } else {
                console.log("request error");
            }
        }
    }

    httpRequest.open('POST', "/alarm_data", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send(JSON.stringify(data));
}

function findAlllog(res) {
    // 1. PagingResponse의 멤버인 List<T> 타입의 list를 의미
    const table = document.getElementById('server_log_list');
    const tbody = document.getElementById('server_log_list_tbody');
    const list = res.list;

    while (tbody.hasChildNodes()) {
        tbody.removeChild(tbody.firstChild);
    }

    // 2. 리스트가 비어있는 경우, 행에 "검색 결과가 없다"는 메시지를 출력하고, 페이지 번호(페이지네이션) HTML을 제거(초기화)한 후 로직을 종료
    if (!list.length) {
        const newRow = table.insertRow();
        const newCell1 = newRow.insertCell(0);

        newCell1.colSpan = 2;
        newCell1.className = "no-data-msg";
        newCell1.innerText = "해당 시간의 서버 로그가 없습니다.";
    }

    // 3. PagingResponse의 멤버인 pagination을 의미
    const pagination = res.pagination;

    // 4. SearchDto 타입의 객체인 params를 의미
    const params = 1;

    // 6. 리스트 데이터 렌더링
    drawList(list);

    // 7. 페이지 번호 렌더링
    drawPage(pagination, params);
}

// 리스트 HTML draw
function drawList(list) {

    /*
     * 1. 기존에 리스트 데이터를 그리던 것과 유사한 로직
     *    기존에는 게시글 번호를 (전체 데이터 수 - loop의 인덱스 번호)로 처리했으나, 현재는 (전체 데이터 수 - ((현재 페이지 번호 - 1) * 페이지당 출력할 데이터 개수))로 정밀히 계산
     */
    const table = document.getElementById('server_log_list');

    for (let i = 0; i < list.length; i++) {
        const newRow = table.insertRow();
        const newCell1 = newRow.insertCell(0);
        const newCell2 = newRow.insertCell(1);

        newCell1.innerText = list[i].val_date;
        newCell2.innerText = list[i].log;
    }
}


// 페이지 HTML draw
function drawPage(pagination, params) {
    const paging = document.querySelector('.paging');

    // 1. 필수 파라미터가 없는 경우, 페이지 번호(페이지네이션) HTML을 제거(초기화)한 후 로직 종료
    if (!pagination || !params) {
        paging.replaceChildren();
    } else {
        paging.replaceChildren();
        // 3. 이전 페이지가 있는 경우, 즉 시작 페이지(startPage)가 1이 아닌 경우 첫 페이지 버튼과 이전 페이지 버튼을 HTML에 추가
        const first = document.createElement('button');
        const prev = document.createElement('button');

        first.className = "button button-normal page_button";
        first.addEventListener('click', () => movePage(1));
        first.textContent = "<<"
        prev.className = "button button-normal page_button";
        prev.addEventListener('click', () => movePage(pagination.page - 1));
        prev.textContent = "<"
        paging.appendChild(first);
        paging.appendChild(prev);


        /*
         * 4. 시작 페이지(startPage)와 끝 페이지(endPage) 사이의 페이지 번호(i)를 넘버링 하는 로직
         *    페이지 번호(i)와 현재 페이지 번호(params.page)가 동일한 경우, 페이지 번호(i)를 활성화(on) 처리
         */
        const input_page = document.createElement('input');
        input_page.className = 'input-page';
        input_page.value = pagination.page;
        input_page.addEventListener('change', () => movePage(input_page.value));
        paging.appendChild(input_page);


        // 5. 현재 위치한 페이지 뒤에 데이터가 더 있는 경우, 다음 페이지 버튼과 끝 페이지 버튼을 HTML에 추가
        const last = document.createElement('button');
        const next = document.createElement('button');

        last.className = "button button-normal page_button";
        last.addEventListener('click', () => movePage(pagination.totalPageCount));
        last.textContent = ">>"
        next.className = "button button-normal page_button";
        next.addEventListener('click', () => movePage(pagination.page + 1));
        next.textContent = ">"
        paging.appendChild(next);
        paging.appendChild(last);
    }
}

// 페이지 이동
function movePage(page) {
    /*
     * 1. 리스트 번호를 변경하거나 페이지 좌우 이동시
     *    AJAX 통신을 이용하여 로그 데이터 표시
     */
    let httpRequest;
    httpRequest = new XMLHttpRequest();
    const popup_log_select = document.getElementById('time_select');
    let selectHours = popup_log_select.options[popup_log_select.selectedIndex].value;

    if (page === 0) {
        page = 1;
    }

    // 2. drawPage( )의 각 버튼에 선언된 onclick 이벤트를 통해 전달받는 page(페이지 번호)를 기준으로 객체 생성
    let pageParam = {
        page      : page,
        recordSize: 10,
        pageSize  : 10
    };
    let data = {
        server_name: selectServer,
        ip         : iplist.get(selectServer),
        hours      : selectHours,
        process    : "popup",
        searchDto  : pageParam
    };

    popupOpen(null, null, "progress");
    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let res = httpRequest.response;
                findAlllog(res);
            } else {
                console.log("request error");
            }
            popupClose('progress');
        }
    }

    httpRequest.open('POST', "/log_data", true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.setRequestHeader(header, token);
    httpRequest.send(JSON.stringify(data));
}

