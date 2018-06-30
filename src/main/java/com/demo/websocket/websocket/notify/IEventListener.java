package com.demo.websocket.websocket.notify;

import com.demo.websocket.websocket.WebSocketServer;

public interface IEventListener{

    void fire(WebSocketServer ws, String data);

    int getCode();
}
