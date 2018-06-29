package com.demo.websocket.websocket;

import com.demo.websocket.bean.MessageInfo;
import com.demo.websocket.util.Result;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全map，用来存放每个客户端对应的MyWebSocket对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketServers = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    // 唯一id
    private String uuid;

    private static Gson gson = new Gson();

    /**
     * 成功建立连接调用方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.uuid = UUID.randomUUID().toString();

        webSocketServers.put(uuid, this);
        addOnlineCount();
        System.out.println("客户端：[" + uuid + "]加入连接，当前共有" + getOnlineCount() + "个连接。");
    }

    /**
     * 接受客户端消息调用
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void OnMessage(Session session, String message) {
        MessageInfo msgInfo = gson.fromJson(message, MessageInfo.class);
        System.out.println(msgInfo.getNickname() + ":" + msgInfo.getMessage());

        // 群发信息
        massTexting(Result.ok(msgInfo));
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose() {
        webSocketServers.remove(this);
        subOnlineCount();
        System.out.println("客户端：[" + uuid + "]断开连接，当前共有" + getOnlineCount() + "个连接。");
    }

    /**
     * 连接发生异常调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        subOnlineCount();
        System.out.println("与客户端：[" + uuid + "]之间的连接发生异常，当前共有" + getOnlineCount() + "个连接。");
        error.printStackTrace();
    }

    /**
     * 服务器推动消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public static void massTexting(String message) {

        webSocketServers.forEach((uuid, ws) -> {
            try {
                ws.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取当前连接数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 连接数+1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 连接数-1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
