package com.demo.websocket.bean;

public enum EnumDevice {
    WX(1, "微信端"),
    BIG_SCREEN(2, "大屏端"),
    CONTROLLER(3, "控制端");

    private int deviceCode;
    private String description;

    EnumDevice(int deviceCode, String description) {
        this.deviceCode = deviceCode;
        this.description = description;
    }

    public int getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(int deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
