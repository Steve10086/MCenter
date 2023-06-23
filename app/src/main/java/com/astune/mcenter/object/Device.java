package com.astune.mcenter.object;

import java.util.Date;

public class Device {
    private String name;
    private String ip;
    private Boolean isOnline;
    private Date lastOnline;

    public Device(String name, String ip){
        this.ip = ip;
        this.name=name;
        this.isOnline = false;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isOnline() {
        return isOnline;
    }

    public void changeStatus(Boolean online) {
        isOnline = online;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }
}
