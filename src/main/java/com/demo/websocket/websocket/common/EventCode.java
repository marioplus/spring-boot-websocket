package com.demo.websocket.websocket.common;

public enum EventCode {


    HEARTBEAT_CLIENT(0000, "ping"),
    HEARTBEAT_SERVER(0001, "pong"),

    SPEAKER_CHANGE(1000, "更换演讲人"),
    START_VOTING(1001, "开启投票通道"),
    END_VOTING(1002, "结束投票"),
    GET_LIKES_NUMBER(1003, "获取点赞数"),
    GET_LIKES_RANK(1004, "获取点赞排行");

    EventCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
