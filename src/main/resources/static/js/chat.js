const ipAdrr = "ws://localhost:8080/websocket";
let ws;
let nickname;

function onMessage(e) {
    let data = JSON.parse(e.data);
    console.log(`on message:${data}`)
    addMsgToQueues(data);
    scrollBottom();
};

function sendMessage() {
    let msg = document.getElementById('msg').value;
    ws.send(organizeLocalData(msg));
    document.getElementById('msg').value = "";
}

function addMsgToQueues(data) {
    let msgItem = document.createElement("div");
    msgItem.className = "msg-item";

    let avatar = document.createElement("div");
    avatar.className = "avatar";
    avatar.innerText = data.nickname.substr(0, 1);

    let msgContent = document.createElement("div");
    msgContent.className = "msg-content";
    msgContent.innerText = data.message;

    msgItem.appendChild(avatar);
    msgItem.appendChild(msgContent);
    document.getElementById('msg-list').appendChild(msgItem);
}

function organizeLocalData(msg) {
    let data = {};
    data.nickname = nickname;
    data.message = msg;
    console.log(`send message: ${data.toString()}`);
    return JSON.stringify(data);
}

function login() {
    let tmp = document.getElementById('nickname').value;
    if (tmp === "") {
        alert('用户名不能为空！');
        return false;
    }
    nickname = tmp;
    console.log(`nickname:${nickname}`);
    ws = new WebSocket(ipAdrr);
    ws.onmessage = e => onMessage(e);
    document.getElementById('login-box-bg').style.zIndex = -1;
    document.getElementById('login-box-bg').style.background = "#f2f2f2";
}

function scrollBottom() {
    let div = document.getElementById('msg-list');
    div.scrollTop = div.scrollHeight;
}