let token;
let header;
let popup;
let popupbody;
let h1;
let select_value;
let plus_value;
let btn_manual;
let httpRequest;
let popup_user_plus;
let id_check_btn;
let ok_img;
let user_plus_id;
let user_plus_pw;
let id_check_plus;
let pw_check_plus;
let auth_check_plus;
let tel_check_plus;
let id_check_plus_result;
let clock;
const open = "popup_wrapper open";

window.onload = function () {
    token = document.querySelector("input[name='_csrf']").value;
    header = document.querySelector("input[name='_csrf_header']").value;
    popup = document.querySelector(".popup_wrapper");
    popupbody = document.querySelector(".popup_body");
    h1 = document.createElement("h1");
    btn_manual = document.querySelector('.btn_manual_download');
    popup_user_plus = document.querySelector('.popup_user_plus');
    id_check_btn = document.querySelector('.popup_body.user_plus > ul > li > .btn.red');
    ok_img = document.querySelector('.popup_body.user_plus > ul > li > img');
    user_plus_id = document.getElementById('user_plus_id');
    user_plus_pw = document.getElementById('user_plus_password');
    id_check_plus = document.getElementById('id_check_user_plus');
    pw_check_plus = document.getElementById('pw_check_user_plus');
    auth_check_plus = document.getElementById('auth_check_user_plus');
    tel_check_plus = document.getElementById('tel_check_user_plus');
    clock = document.getElementById('clock');
    const gear_check = document.getElementById("setting")
    const menu_button = document.querySelectorAll('.menu_body.setting > ul > a');
    const content = document.getElementsByClassName('tb1-content');
    const content_table = document.querySelectorAll('table');
    const tb1header = document.getElementsByClassName('tb1-header');
    const table = document.getElementById('server_list');
    const rowList = table.rows;
    let scrollwidth;

    getClock();
    setInterval(getClock, 1000);

    /*서버리스트 및 정보 불러오는 함수*/
    // serverList();
    for (let i = 0; i < content.length; i++) {
        if (content_table.item(i) !== undefined) {
            scrollwidth = content.item(i).width - content_table.item(i).clientWidth;
        }
    }
    for (let i = 0; i < tb1header.length; i++) {
        tb1header.item(i).css('padding-right', scrollwidth)
    }

    btn_manual.addEventListener("click", () => manual_download());

    for (let i = 0; i < menu_button.length; i++) {
        menu_button[i].addEventListener('click', () => gear_check.checked = false);
    }

    for (let i = 0; i < rowList.length; i++) {
        let row = rowList[i];
        const btnblue = row.querySelector('.btn.blue');
        if (btnblue !== null) {
            const btnred = row.querySelector('.btn.red');
            let tdsNum = row.childElementCount;

            let data = {};
            for (let j = 0; j < (tdsNum - 2); j++) {
                let row_value = row.cells[j].innerHTML;
                switch (j) {
                    case 0 :
                        data.system = row_value;
                        break;
                    case 1:
                        data.ip = row_value;
                        break;
                    case 2:
                        data.server_name = row_value;
                        break;
                    case 3:
                        data.server_port = row_value;
                        break;
                    case 4:
                        data.status = row_value;
                        break;
                    case 5:
                        data.cpu = row_value;
                        break;
                    case 6:
                        data.memory = row_value;
                        break;
                    case 7:
                        data.disk = row_value;
                        break;
                    case 8:
                        data.trapictx = row_value;
                        break;
                    case 9:
                        data.trapicrx = row_value;
                        break;
                }
                if (row_value === "가동") {
                    row.cells[j].style.background = "linear-gradient(0deg, rgb(0, 172, 238) 0%, rgb(2, 126, 251) 100%)";
                    row.cells[j].style.fontWeight = "bold";
                } else if (row_value === "정지") {
                    row.cells[j].style.background = "linear-gradient(0deg, rgb(255, 151, 0) 0%, rgb(251, 75, 2) 100%)";
                    row.cells[j].style.fontWeight = "bold";
                }
            }

            btnblue.addEventListener("click", () => popupOpen(data, 'on', 'power'));
            btnred.addEventListener("click", () => popupOpen(data, 'off', 'power'));
        }
    }
}

function getClock(){
    const date = new Date()
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const day = ("0" + date.getDate()).slice(-2);
    const hour = String(date.getHours()).padStart(2,"0");
    const minutes = String(date.getMinutes()).padStart(2,"0");
    const second = String(date.getSeconds()).padStart(2,"0");//number이기 때문에 padStart 붙일 수 없음. String 변환해주어야한다.
    const weakday = new Date("'"+year+"-"+month+"-"+day+"'");
    const WEEKDAY = ['일', '월', '화', '수', '목', '금', '토'];
    let week = WEEKDAY[weakday.getDay()];

    clock.innerText = `${year}-${month}-${day}(${week}) ${hour}:${minutes}:${second}`;
}

am5.ready(function () {
// Create root element
// https://www.amcharts.com/docs/v5/getting-started/#Root_element
    let root = am5.Root.new("chartdiv");

    let responsive = am5themes_Responsive.newEmpty(root);

    responsive.addRule({
        relevant: function (width, height) {
            return height < 600;
        },
        applying: function () {
            legend.setAll({
                y      : am5.percent(125),
                centerY: am5.percent(125),
                x      : am5.percent(50),
                centerX: am5.percent(50)
            });
        },
        removing: function () {
            legend.setAll({
                y      : am5.percent(105),
                centerY: am5.percent(105),
                x      : am5.percent(50),
                centerX: am5.percent(50)
            });
        }
    });

    const myTheme = am5.Theme.new(root);

    myTheme.rule("Label").setAll({
        fontSize: "20px"
    })

// Set themes
// https://www.amcharts.com/docs/v5/concepts/themes/
    root.setThemes([
        am5themes_Animated.new(root),
        am5themes_Dark.new(root),
        myTheme, responsive
    ]);

// Create chart
// https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
    let chart = root.container.children.push(
        am5percent.PieChart.new(root, {
            endAngle   : 270,
            width      : root.dom.clientWidth,
            height     : root.dom.clientHeight - 150,
            innerRadius: am5.percent(50)
        })
    );

// Create series
// https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series
    let series = chart.series.push(
        am5percent.PieSeries.new(root, {
            valueField     : "value",
            categoryField  : "category",
            endAngle       : 270,
            alignLabels    : false,
            legendLabelText: "[{fill}]{category}[/]",
            legendValueText: "[bold {fill}]{value}[/]"
        })
    );

    series.states.create("hidden", {
        endAngle: -90
    });

// Set data
// https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Setting_data
    let data = [{
        category: "HEMS",
        value   : 301.9
    }, {
        category: "BEMS",
        value   : 201.9
    }, {
        category: "L-BEMS",
        value   : 501.9
    }];
    series.data.setAll(data);

    series.labels.template.setAll({
        fontSize: 16,
        text    : "{category}",
        textType: "radial",
        radius  : 0,
        centerX : am5.percent(100),
        fill    : am5.color(0xffffff)
    });

    series.ticks.template.set("forceHidden", true);

    // Add legend
    let legend = chart.children.push(am5.Legend.new(root, {
        y      : am5.percent(105),
        centerY: am5.percent(105),
        x      : am5.percent(50),
        centerX: am5.percent(50),
        layout : am5.GridLayout.new(root, {
            maxColumns    : 2,
            fixedWidthGrid: true
        })
    }));
    legend.data.setAll(series.dataItems);

    // Add label
    chart.children.unshift(am5.Label.new(root, {
        text     : "트래픽(RX/TX)",
        fontSize : 18,
        textAlign: "center",
        x        : am5.percent(50),
        centerX  : am5.percent(50),
        y        : am5.percent(50),
        centerY  : am5.percent(50)
    }));
}); // end am5.ready()

function serverList() {
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            popupClose("progress");
            if (httpRequest.status === 200) {
                const table_id = document.querySelector('#server_list');
                const tbody = document.querySelector('.server_content');
                while (tbody.hasChildNodes()) {
                    tbody.removeChild(tbody.firstChild);
                }
                let res = httpRequest.response;
                let resMap = new Map();

                if (res !== null) {
                    for (let key of Object.keys(res)) {
                        const value = res[key];
                        resMap.set(key, value);
                        console.log(key, value);
                    }

                    resMap.get("table").map(function (result) {
                        tableCreate(result, table_id);
                    })
                    resMap.get("ranking_cpu").map(function (result) {
                        rankCreate(result);
                    })
                    resMap.get("ranking_memory").map(function (result) {
                        rankCreate(result);
                    })
                    resMap.get("ranking_disk").map(function (result) {
                        rankCreate(result);
                    })

                    tableRowSpan(table_id);
                    ranking_create(table_id);
                }
            } else {
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

function tableCreate(result, table_id) {
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
    const newCell11 = newRow.insertCell(10);
    const newCell12 = newRow.insertCell(11);
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
    let system = result.system;
    let ip = result.ip;
    let server_name = result.server_name;
    let port = result.port;
    let cpu = result.cpu;
    let memory = result.memory;
    let disk = result.disk;
    let port_status = result.port_status;
    let rx = result.RX;
    let tx = result.TX;
    let cpu_rank = result.cpu_rank;
    let memory_rank = result.memory_rank;
    let disk_rank = result.disk_rank;
    console.log("서버 정보(tablecreate) : " + system, ip, server_name, port, cpu, memory, disk, port_status, rx, tx, cpu_rank, memory_rank, disk_rank);

    newCell1.innerText = system;
    newCell2.innerText = ip;
    newCell3.innerText = server_name;
    newCell4.innerText = port;
    if (port_status === "가동") {
        newCell5.innerText = port_status;
        newCell5.style.background = "linear-gradient(0deg, rgba(0, 172, 238, 1) 0%, rgba(2, 126, 251, 1) 100%)";
        newCell5.style.fontWeight = "bold";
    } else if (port_status === "정지") {
        newCell5.innerText = port_status;
        newCell5.style.background = "linear-gradient(0deg, rgba(255, 151, 0, 1) 0%, rgba(251, 75, 2, 1) 100%)";
        newCell5.style.fontWeight = "bold";
    }
    newCell5.innerText = port_status;
    newCell6.innerText = rx;
    newCell7.innerText = tx;
    newCell8.innerText = cpu;
    newCell9.innerText = memory;
    newCell10.innerText = disk;
    newCell11.appendChild(btn_blue);
    newCell12.appendChild(btn_red);

    btn_blue.addEventListener("click", () => popupOpen(result, 'on', 'power'));
    btn_red.addEventListener("click", () => popupOpen(result, 'off', 'power'));
}

function rankCreate(result) {
    let system = result.system;
    let ip = result.ip;
    let server_name = result.server_name;
    let port = result.port;
    let cpu = result.cpu;
    let memory = result.memory;
    let disk = result.disk;
    let port_status = result.port_status;
    let rx = result.RX;
    let tx = result.TX;
    let cpu_rank = result.cpu_rank;
    let memory_rank = result.memory_rank;
    let disk_rank = result.disk_rank;
    console.log("서버 정보(rankcreate) : " + system, ip, server_name, port, cpu, memory, disk, port_status, rx, tx, cpu_rank, memory_rank, disk_rank);
}

function tableRowSpan(table_id) {
    let rows = table_id.getElementsByTagName("tr");
    let i = 0;
    let j = 0;

    // tr만큼 루프돌면서 컬럼값 접근
    for (j = 1; j < rows.length; j++) {
        let cells1 = rows[i].getElementsByTagName("td");
        let cells2 = rows[j].getElementsByTagName("td");
        let ip_span_check = false;
        let name_span_check = false;

        for (let r = 0; r < cells1.length; r++) {
            if (r === 0 || r === 1 || r === 2) {
                i = j;
                if (cells1[r].innerHTML === cells2[r].innerHTML) {
                    i = j;
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
                }
            }
        }
    }
}

function ranking_create(table_id) {
    let rows = table_id.getElementsByTagName("tr");
    let i = 0;
    let j = 0;

    // for (j = 1; j < rows.length; j++) {
    //     let cells1 = rows[i].getElementsByTagName("td");
    //     let cells2 = rows[j].getElementsByTagName("td");
    //     console.log(cells1, cells2);
    //
    //     for (let r = 0; r < cells1.length; r++) {
    //         i = j;
    //         if (r === 7) {
    //             let cell1 = cells1[r].innerHTML;
    //             let cell2 = cells2[r].innerHTML;
    //             cell1 = parseFloat(cell1.substring(cell1.lastIndex - 1));
    //             cell2 = parseFloat(cell2.substring(cell2.lastIndex - 1));
    //             if(cell1 >= cell2) {
    //             }
    //         } else if(r === 8) {
    //
    //         }
    //     }
    // }
}

function popupOpen(data, power, process) {
    if (process === 'power') {
        popup.className = open;

        const btn_blue = document.createElement("button");
        const btn_red = document.createElement("button");
        const span_blue = document.createElement("span");
        const span_red = document.createElement("span");

        popupbody.replaceChildren();
        h1.style.padding = "0 30px 20px 50px";
        btn_blue.className = "btn blue";
        btn_red.className = "btn red";
        let datamap = new Map();
        data.power = power;

        for (let key of Object.keys(data)) {
            const value = data[key];
            datamap.set(key, value);
        }

        if (power === "on") {
            h1.textContent = datamap.get("server_name") + "서버를 구동 하시겠습니까?";
            span_blue.textContent = "확인";
            span_red.textContent = "취소";
            btn_blue.appendChild(span_blue);
            btn_red.appendChild(span_red);
        } else if (power === "off") {
            h1.textContent = datamap.get("server_name") + "서버를 종료 하시겠습니까?";
            span_blue.textContent = "확인";
            span_red.textContent = "취소";
            btn_blue.appendChild(span_blue);
            btn_red.appendChild(span_red);
        }

        popupbody.appendChild(h1);
        popupbody.appendChild(btn_blue);
        popupbody.appendChild(btn_red);
        btn_blue.addEventListener("click", () => power_result(data, power));
        btn_red.addEventListener("click", () => popupClose('power'));
    } else if (process === 'plus') {
        const popup_wrapper_plus = document.querySelector(".popup_wrapper.plus");
        const btn_plus_blue = document.querySelector(".popup_body.plus > .btn.blue");
        const btn_plus_red = document.querySelector(".popup_body.plus > .btn.red");
        popup_wrapper_plus.className = "popup_wrapper open plus";

        btn_plus_blue.addEventListener("click", () => server_management('plus'));
        btn_plus_red.addEventListener("click", () => popupClose('plus'));
    } else if (process === 'delete') {
        const popup_wrapper_delete = document.querySelector(".popup_wrapper.delete");
        const btn_delete_blue = document.querySelector(".popup_body.delete > .btn.blue");
        const btn_delete_red = document.querySelector(".popup_body.delete > .btn.red");
        popup_wrapper_delete.className = "popup_wrapper open delete";

        btn_delete_blue.addEventListener("click", () => server_management('delete'));
        btn_delete_red.addEventListener("click", () => popupClose('delete'));
    } else if (process === 'progress') {
        const popup_wrapper_progress = document.querySelector(".popup_progress_back");
        popup_wrapper_progress.className = "popup_progress_back open";
    } else if (process === 'userlist') {
        const popup_wrapper_userlist = document.querySelector(".popup_wrapper.userlist");
        const btn_delete_blue = document.querySelector(".popup_body.userlist > .btn.blue");
        const btn_delete_red = document.querySelector(".popup_body.userlist > .btn.red");
        popup_wrapper_userlist.className = "popup_wrapper open userlist";

        const table = document.getElementById('user_list');
        const rowList = table.rows;

        for (let i = 0; i < rowList.length; i++) {
            let row = rowList[i];
            const btnred = row.querySelector('.btn.red');
            if (btnred !== null) {
                let tdsNum = row.childElementCount;

                let data = {};
                for (let j = 0; j < (tdsNum - 1); j++) {
                    let row_value = row.cells[j].innerHTML;
                    switch (j) {
                        case 1 :
                            data.user_id = row_value;
                            break;
                        case 2:
                            data.user_auth = row_value;
                            break;
                        case 3:
                            data.user_tel = row_value;
                            break;
                    }
                }

                btnred.addEventListener("click", () => popupOpen(data, null, "user_edit"));
            }
        }
        btn_delete_blue.addEventListener("click", () => popupOpen(null, null, "user_plus"));
        btn_delete_red.addEventListener("click", () => popupClose('userlist'));
    } else if (process === 'user_edit') {
        const popup_wrapper_edit = document.querySelector(".popup_wrapper.user_edit");
        const btn_edit_blue = document.querySelector(".popup_body.user_edit > .button > .btn.blue");
        const btn_edit_red = document.querySelector(".popup_body.user_edit > .button > .btn.red");
        const btn_edit_select = document.getElementById('user_edit_auth');
        const user_id = document.getElementById("user_edit_id");
        const user_tel = document.getElementById("user_edit_tel");
        select_value = data.user_auth;
        btn_edit_select.textContent = data.user_auth;
        user_id.value = data.user_id;
        user_tel.value = data.user_tel;
        popup_wrapper_edit.className = "popup_wrapper open user_edit blur";
        select_function('user_edit');

        btn_edit_blue.addEventListener("click", () => user_management('user_edit'));
        btn_edit_red.addEventListener("click", () => popupClose('user_edit'));
    } else if (process === 'user_plus') {
        const popup_wrapper_user_plus = document.querySelector(".popup_wrapper.user_plus");
        const btn_user_plus_blue = document.querySelector(".popup_body.user_plus > .btn.blue");
        const btn_user_plus_red = document.querySelector(".popup_body.user_plus > .btn.red");
        const user_plus_id = document.getElementById('user_plus_id');
        const user_plus_pw = document.getElementById('user_plus_password');
        const user_plus_auth = document.getElementById('user_plus_auth');
        const user_plus_tel = document.getElementById('user_plus_tel');
        popup_wrapper_user_plus.className = "popup_wrapper open user_plus";
        user_plus_auth.innerText = "사용자 권한";
        user_plus_auth.style.color = "#D3D3D3";
        select_function('user_plus');
        popup_user_plus.style.height = "356px";
        user_plus_id.style.width = "258px";
        user_plus_id.value = "";
        ok_img.style.display = "none";
        id_check_btn.style.display = "block";
        id_check_plus.style.display = "none";
        pw_check_plus.style.display = "none";
        auth_check_plus.style.display = "none";
        tel_check_plus.style.display = "none";
        id_check_plus_result = undefined;
        user_plus_id.disabled = false;
        user_plus_pw.value = "";
        user_plus_tel.value = "";

        btn_user_plus_blue.addEventListener("click", () => user_management('user_plus'));
        btn_user_plus_red.addEventListener("click", () => popupClose('user_plus'));
    }
}

function popupClose(process) {
    if (process === 'power') {
        popup.className = "popup_wrapper close";
        popupbody.replaceChildren();
    } else if (process === 'plus') {
        const popup_plus = document.querySelector(".popup_wrapper.plus");
        popup_plus.className = "popup_wrapper close plus";
    } else if (process === 'delete') {
        const popup_plus = document.querySelector(".popup_wrapper.delete");
        popup_plus.className = "popup_wrapper close delete";
    } else if (process === 'progress') {
        const popup_wrapper_progress = document.querySelector(".popup_progress_back");
        popup_wrapper_progress.className = "popup_progress_back close";
    } else if (process === 'userlist') {
        const popup_wrapper_userlist = document.querySelector(".popup_wrapper.userlist");
        popup_wrapper_userlist.className = "popup_wrapper close userlist";
    } else if (process === 'user_edit') {
        const popup_wrapper_auth = document.querySelector(".popup_wrapper.user_edit");
        popup_wrapper_auth.className = "popup_wrapper close user_edit";
        select_value = undefined;
    } else if (process === 'user_plus') {
        const popup_user_plus = document.querySelector(".popup_wrapper.user_plus");
        popup_user_plus.className = "popup_wrapper close user_plus";
        select_value = undefined;
    }
}

function power_result(data, power) {
    popupbody.replaceChildren();
    h1.style.padding = "30px 30px 30px 30px";
    let datamap = new Map();
    httpRequest = new XMLHttpRequest();

    for (let key of Object.keys(data)) {
        const value = data[key];
        datamap.set(key, value);
    }

    httpRequest.onreadystatechange = () => {
        popupOpen(null, null, "progress");
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let res = httpRequest.response;
                if (res.result === "ok") {
                    if (power === "on") {
                        h1.textContent = datamap.get("server_name") + "서버를 구동 하였습니다.";
                    } else if (power === "off") {
                        h1.textContent = datamap.get("server_name") + "서버를 종료 하였습니다.";
                    }
                    popupbody.appendChild(h1);
                    popupClose("progress");
                } else if (res.result === "reason") {
                    if (power === "on") {
                        h1.textContent = datamap.get("server_name") + "서버가 이미 작동중입니다.";
                    } else if (power === "off") {
                        h1.textContent = datamap.get("server_name") + "서버가 이미 종료되있습니다.";
                    }
                    popupbody.appendChild(h1);
                    popupClose("progress");
                } else {
                    if (power === "on") {
                        h1.textContent = datamap.get("server_name") + "서버 구동에 실패했습니다.";
                    } else if (power === "off") {
                        h1.textContent = datamap.get("server_name") + "서버 종료에 실패했습니다.";
                    }
                    popupbody.appendChild(h1);
                    popupClose("progress");
                }
            } else {
                if (power === "on") {
                    h1.textContent = datamap.get("server_name") + "서버 구동에 실패했습니다.";
                } else if (power === "off") {
                    h1.textContent = datamap.get("server_name") + "서버 종료에 실패했습니다.";
                }
                popupbody.appendChild(h1);
                popupClose("progress");
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

function server_Delete() {
    console.log("Delete");
}

function select_function(process) {
    const btn_select = document.getElementById(process + "_auth");
    const list_select = document.getElementById(process + "-member");
    const popup_wrapper_select = document.querySelector(".popup_wrapper." + process);
    const popup_head_select = document.querySelector(".popup_head." + process);
    const popup_body_select = document.querySelector(".popup_" + process);

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
    popup_head_select.clickHandler = (event) => {
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
    const response = await fetch('/resource/manual/manual.docx');
    const file = await response.blob();
    const downloadUrl = window.URL.createObjectURL(file); // 해당 file을 가리키는 url 생성
    const aElement = document.createElement('a');

    document.body.appendChild(aElement);
    aElement.download = 'KevinLAB 서버 모니터링 시스템 메뉴얼'; // a tag에 download 속성을 줘서 클릭할 때 다운로드가 일어날 수 있도록 하기
    aElement.href = downloadUrl; // href에 url 달아주기

    aElement.click(); // 코드 상으로 클릭을 해줘서 다운로드를 트리거

    document.body.removeChild(aElement); // cleanup - 쓰임을 다한 a 태그 삭제
    window.URL.revokeObjectURL(downloadUrl); // cleanup - 쓰임을 다한 url 객체 삭제
}

function id_check() {
    const put_id = document.getElementById('user_plus_id');
    let id = put_id.value;
    console.log(id);
    if (!(id === undefined || id === "")) {
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
                    console.log("check : " + check);
                    if (check === "ok") {
                        id_check_btn.style.display = "none";
                        user_plus_id.style.width = "277px";
                        user_plus_id.disabled = true;
                        ok_img.style.display = "block";
                        id_check_plus.style.display = "none";

                        id_check_plus_result = true;
                    } else if (check === "error") {
                        id_check_plus.innerText = "중복된 ID 확인중에 오류가 발생했습니다."
                        id_check_plus.style.display = "block";
                        popup_user_plus.style.height = "auto";
                        popup_user_plus.style.overflow = 'hidden';

                        id_check_plus_result = false;
                    } else if (check === "nok") {
                        id_check_plus.style.innerText = "중복된 ID가 존재합니다. 다른 ID를 입력해주세요.";
                        id_check_plus.style.display = "block";
                        popup_user_plus.style.height = "auto";
                        popup_user_plus.style.overflow = 'hidden';

                        id_check_plus_result = false;
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
        popup_user_plus.style.height = "auto";
        popup_user_plus.style.overflow = 'hidden';

        id_check_plus_result = false;
    }
}

function pw_check(pw, process) {
    const popup = document.querySelector(".popup_"+process);
    const user_id = document.getElementById(process+'_id');
    const pw_p = document.getElementById('pw_check_'+process);
    let id = user_id.value;
    let checkNumber = pw.search(/[0-9]/g);
    let checkEnglish = pw.search(/[a-z]/ig);

    if (pw !== "" || pw !== undefined) {
        if(!/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/.test(pw)){
            pw_p.innerText= `숫자+영문자+특수문자 조합으로 8~20자리 사용해야 합니다.`;
            pw_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        }else if(checkNumber <0 || checkEnglish <0){
            pw_p.innerText= "숫자와 영문자를 혼용하여야 합니다.";
            pw_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        }else if(/(\w)\1\1\1/.test(pw)){
            pw_p.innerText= "같은 문자를 4번 이상 사용하실 수 없습니다.";
            pw_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        }else if(pw.search(id) > -1){
            pw_p.innerText= "비밀번호에 아이디가 포함되었습니다.";
            pw_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        }else {
            popup.style.height = "356px";
            pw_p.style.display = 'none';
            return true;
        }
    } else {
        pw_p.innerText = "PW를 입력하지 않았습니다. 패스워드를 입력해주세요.";
        pw_p.style.display = 'block';
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';
        return false;
    }
}

function auth_check(auth, process) {
    const popup = document.querySelector(".popup_"+process);
    const auth_p = document.getElementById('auth_check_'+process);
    if (auth === "사용자 권한") {
        auth_p.style.display = 'block';
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';

        return false;
    } else {
        auth_p.style.display = 'none';
        popup.style.height = "356px";

        return true;
    }
}

function tel_check(tel, process) {
    let regexTel = /^(6553[0-5]|655[0-2]\d|65[0-4]\d{2}|6[0-4]\d{3}|5\d{4}|[0-9]\d{0,3})$/;
    const popup = document.querySelector(".popup_"+process);
    const tel_p = document.getElementById('tel_check_'+process);

    if (!regexTel.test(tel)) {
        tel_p.style.display = 'block';
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';
        return false;
    } else {
        tel_p.style.display = 'none';
        popup.style.height = "356px";

        return true;
    }
}

function ip_check(ip, process) {
    let regexIP = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    const popup = document.querySelector(".popup_"+process);
    const ip_p = document.getElementById('ip_check_'+process);

    if(ip !== "" || ip !== undefined) {
        if (!regexIP.test(ip)) {
            ip_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        } else {
            ip_p.style.display = 'none';
            popup.style.height = "356px";

            return true;
        }
    } else {
        ip_p.innerText = 'IP를 입력하셔야됩니다.'
        ip_p.style.display = 'block';
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';

        return false;
    }
}

function port_check(port, process) {
    let regexPort = /(6553[0-5]|655[0-2]\d|65[0-4]\d{2}|6[0-4]\d{3}|5\d{4}|[0-9]\d{0,3})/;
    const popup = document.querySelector(".popup_"+process);
    const port_p = document.getElementById('port_check_'+process);

    if(port !== "" || port !== undefined) {
        if (!regexPort.test(port)) {
            port_p.style.display = 'block';
            popup.style.height = "auto";
            popup.style.overflow = 'hidden';
            return false;
        } else {
            port_p.style.display = 'none';
            popup.style.height = "356px";

            return true;
        }
    } else {
        port_p.innerText = '포트번호를 입력하셔야됩니다.';
        port_p.style.display = 'block';
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';

        return false;
    }
}

function system_check(system, process) {
    const popup = document.querySelector(".popup_"+process);
    const system_p = document.getElementById('system_check_'+process);

    if(system === "" || system === undefined) {
        system_p.innerText = "시스템이 입력되지 않았습니다."
        system_p.style.display = "block";
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';

        return false;
    } else {
        system_p.style.display = 'none';
        popup.style.height = "356px";

        return true;
    }
}

function name_check(name, process) {
    const popup = document.querySelector(".popup_"+process);
    const name_p = document.getElementById('name_check_'+process);

    if(name === "" || name === undefined) {
        name_p.innerText = "서버 이름이 입력되지 않았습니다."
        name_p.style.display = "block";
        popup.style.height = "auto";
        popup.style.overflow = 'hidden';

        return false;
    } else {
        name_p.style.display = 'none';
        popup.style.height = "356px";

        return true;
    }
}

function user_management(process) {
    const user_id = document.getElementById(process + '_id');
    const user_pw = document.getElementById(process + '_password');
    const user_auth = document.getElementById(process + '_auth');
    const user_tel = document.getElementById( process +'_tel');
    let id = user_id.value;
    let pw = user_pw.value;
    let auth = user_auth.innerText;
    let tel = user_tel.value;
    if (id_check_plus_result && pw_check(pw, process) && auth_check(auth, process) && tel_check(tel, process)) {
        let data = {}
        data.id = id;
        data.pw = pw;
        data.auth = auth;
        data.tel = tel;

        httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = () => {
            popupOpen(null, null, "progress");
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                popupClose("progress");
                if (httpRequest.status === 200) {
                    console.log("user_management");
                } else {
                    console.log("request error");
                }
            }
        }

        httpRequest.open('POST', "/"+process, true);
        httpRequest.responseType = "json";
        httpRequest.setRequestHeader('Content-Type', 'application/json');
        httpRequest.setRequestHeader(header, token);
        httpRequest.send(JSON.stringify(data));
    } else if(id_check_plus_result === undefined) {
        id_check_plus.innerText = "ID가 입력되지 않았습니다."
        id_check_plus.style.display = "block";
        popup_user_plus.style.height = "auto";
        popup_user_plus.style.overflow = 'hidden';

        id_check_plus_result = false;
    }
}

function server_management(process) {
    const server_system = document.getElementById('system_'+process);
    const server_ip = document.getElementById('ip_'+process);
    const server_name = document.getElementById('server_name_'+process);
    const server_port = document.getElementById('server_port_'+process);
    let system = server_system.value;
    let ip = server_ip.value;
    let name = server_name.value;
    let port = server_port.value;

    if (system_check(system, process) && ip_check(ip, process) && name_check(name, process) && port_check(port, process)) {
        let data = {}
        data.system = system;
        data.ip = ip;
        data.name = name;
        data.port = port;

        httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = () => {
            popupOpen(null, null, "progress");
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                popupClose("progress");
                if (httpRequest.status === 200) {
                    console.log("server_ajax");
                } else {
                    console.log("request error");
                }
            }
        }

        httpRequest.open('POST', "/" + process, true);
        httpRequest.responseType = "json";
        httpRequest.setRequestHeader('Content-Type', 'application/json');
        httpRequest.setRequestHeader(header, token);
        httpRequest.send(JSON.stringify(data));
    }
}