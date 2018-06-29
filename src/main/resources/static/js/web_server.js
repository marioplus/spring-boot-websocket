const WebSocket = require("ws");
const uuid = require("node-uuid");
const port = 8080;
const wss = new WebSocket.Server({port: port});

var clients = [];
var clientIndex = 1;


wss.on("connection", function (ws) {
    addClient(ws);
    ws.on("message", (data) => wsSend(data));
    ws.on("close", (data)=>console.log(data));
});


function addClient(ws) {
    var client_uuid = uuid.v4();
    clients.push({"id": client_uuid, "ws": ws});
    console.log('client [%s] connect', client_uuid);
}

function wsSend(data) {
    var dataObj = JSON.parse(data);
    console.log("%s:%s", dataObj.nickname, dataObj.message);
    clients.forEach((client) => {
        if (client.ws.readyState == WebSocket.OPEN) {
            client.ws.send(data);
        }
    });
}

console.log('Listening on %d...', port);
