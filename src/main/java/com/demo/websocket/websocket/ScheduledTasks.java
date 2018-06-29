package com.demo.websocket.websocket;

import com.demo.websocket.bean.MessageInfo;
import com.demo.websocket.util.Result;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 间隔5s执行
    @Scheduled(fixedRate = 5000)
    public void reOnlineCount() {
        MessageInfo info = new MessageInfo("System", "当前时间"+dateFormat.format(new Date())+",在线人数：" + WebSocketServer.getOnlineCount());
        System.out.println(new Gson().toJson(info));
        WebSocketServer.massTexting(Result.ok(info));
    }
}
