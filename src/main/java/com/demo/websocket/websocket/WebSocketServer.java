package com.demo.websocket.websocket;

import com.demo.websocket.websocket.common.Request;
import com.demo.websocket.websocket.notify.EventManager;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{deviceCode}")
@Component
public class WebSocketServer {

    // 微信端连接数
    private static int wxClientCount = 0;
    // 总连接数
    private static int onlineCount = 0;
    // 微信端webSocket客户端对象
    private static ConcurrentHashMap<String, WebSocketServer> wxClients = new ConcurrentHashMap<>();
    // 大屏幕端webSocket客户端对象
    private static WebSocketServer bigScreenClient;
    // 控制端webSocket客户端对象
    private static WebSocketServer controllerClient;
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    // 唯一id
    private String uuid;
    // 设备类型

    private static Gson gson = new Gson();

    // 微信端
    private static final int WX = 1;
    // 大屏端
    private static final int BIG_SCREEN = 2;
    // 控制端
    private static final int CONTROLLER = 3;

    // 当前设备
    private int device;


    /**
     * 成功建立连接调用方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("deviceCode") int deviceCode) {

        this.session = session;
        this.uuid = UUID.randomUUID().toString();

        switch (deviceCode) {
            case WX:
                wxClients.put(uuid, this);
                addWxClientCount();
                this.device = WX;
                break;
            case BIG_SCREEN:
                bigScreenClient = this;
                this.device = BIG_SCREEN;
                break;
            case CONTROLLER:
                controllerClient = this;
                this.device = CONTROLLER;
                break;
            default:
                // TODO: 未知设备
        }

        addOnlineCount();
        System.out.println("客户端：[" + uuid + "]加入连接，当前共有" + getWxClientCount() + "个连接。");
    }

    /**
     * 接受客户端消息调用
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void OnMessage(Session session, String message) {
        Request request = gson.fromJson(message, Request.class);
        EventManager.getInstance().notifyEvent(this, request);
    }

    /**
     * 连接关闭调用
     */
    @OnClose
    public void onClose() {
        removeClient();
        System.out.println("客户端：[" + uuid + "]断开连接，当前共有" + getWxClientCount() + "个连接。");
    }

    /**
     * 连接发生异常调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        removeClient();
        System.out.println("与客户端：[" + uuid + "]之间的连接发生异常，当前共有" + getWxClientCount() + "个连接。");
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

        wxClients.forEach((uuid, ws) -> {
            try {
                ws.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeClient() {
        switch (this.device) {
            case WX:
                wxClients.remove(uuid);
                subWxClientCount();
                subOnlineCount();
                break;
            case BIG_SCREEN:
                bigScreenClient = null;
                subOnlineCount();
                break;
            case CONTROLLER:
                controllerClient = null;
                subOnlineCount();
                break;
        }
    }

    /**
     * 获取wx端当前连接数
     *
     * @return
     */
    public static synchronized int getWxClientCount() {
        return wxClientCount;
    }

    /**
     * wx端连接数+1
     */
    public static synchronized void addWxClientCount() {
        WebSocketServer.wxClientCount++;
    }

    /**
     * wx端连接数-1
     */
    public static synchronized void subWxClientCount() {
        WebSocketServer.wxClientCount--;
    }

    /**
     * 获取当前总连接数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return wxClientCount;
    }

    /**
     * 总连接数+1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.wxClientCount++;
    }

    /**
     * 总连接数-1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.wxClientCount--;
    }

    /**
     * 清理无效连接
     */
    public static synchronized void clearInvalidConnect() {
        if (wxClients != null) {
            wxClients.forEach((key, ws) -> {
                if (!ws.session.isOpen()) {
                    ws.removeClient();
                }
            });
        }
        if (!controllerClient.session.isOpen()) {
            controllerClient = null;
        }

        if (!bigScreenClient.session.isOpen()) {
            bigScreenClient = null;
        }
    }
}
