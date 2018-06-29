package com.demo.websocket.util;

import com.demo.websocket.bean.MessageInfo;
import com.google.gson.Gson;

public class Result {

    private String nickname;
    private String message;

    public Result() {
    }

    public Result(MessageInfo messageInfo) {
        this.nickname = messageInfo.getNickname();
        this.message = messageInfo.getMessage();
    }

    public Result(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String ok(MessageInfo messageInfo) {
        return new Gson().toJson(new Result(messageInfo));
    }
}
