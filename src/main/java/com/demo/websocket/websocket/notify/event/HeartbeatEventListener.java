package com.demo.websocket.websocket.notify.event;

import com.demo.websocket.websocket.WebSocketServer;
import com.demo.websocket.websocket.common.EventCode;
import com.demo.websocket.websocket.notify.IEventListener;
import com.google.gson.JsonObject;

import java.io.IOException;

public class HeartbeatEventListener implements IEventListener {
    @Override
    public void fire(WebSocketServer ws,String data) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code",EventCode.HEARTBEAT_SERVER.getCode());
        jsonObject.addProperty("msg",EventCode.HEARTBEAT_SERVER.getMsg());
        try {
            ws.sendMessage(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getCode() {
        return EventCode.HEARTBEAT_CLIENT.getCode();
    }
}
