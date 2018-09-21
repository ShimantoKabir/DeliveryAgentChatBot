package com.example.maask.deliveryagentchatbot.PojoClass;

public class Notification {

    public String from;
    public String type;
    public String deviceToken;
    public String userName;

    public Notification() {}

    public Notification(String from, String type, String deviceToken, String userName) {
        this.from = from;
        this.type = type;
        this.deviceToken = deviceToken;
        this.userName = userName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
