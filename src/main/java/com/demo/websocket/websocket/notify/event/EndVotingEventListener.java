package com.demo.websocket.websocket.notify.event;

import com.demo.websocket.websocket.WebSocketServer;
import com.demo.websocket.websocket.common.EventCode;
import com.demo.websocket.websocket.notify.IEventListener;

public class EndVotingEventListener implements IEventListener {
    @Override
    public void fire(WebSocketServer ws,String data) {

    }

    @Override
    public int getCode() {
        return EventCode.END_VOTING.getCode();
    }
}
