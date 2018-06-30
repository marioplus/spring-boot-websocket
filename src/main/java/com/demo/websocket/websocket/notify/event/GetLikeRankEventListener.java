package com.demo.websocket.websocket.notify.event;

import com.demo.websocket.websocket.WebSocketServer;
import com.demo.websocket.websocket.common.EventCode;
import com.demo.websocket.websocket.notify.IEventListener;

public class GetLikeRankEventListener implements IEventListener {

    private Runnable scheduledTask = () -> {

    };

    @Override
    public void fire(WebSocketServer ws,String data) {

    }


    @Override
    public int getCode() {
        return EventCode.GET_LIKES_RANK.getCode();
    }

}
