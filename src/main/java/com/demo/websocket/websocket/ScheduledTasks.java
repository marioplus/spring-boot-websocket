package com.demo.websocket.websocket;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    // 间隔5s执行
    @Scheduled(fixedRate = 5000)
    public static void clearInvalidConnect() {
        WebSocketServer.clearInvalidConnect();
    }
}
