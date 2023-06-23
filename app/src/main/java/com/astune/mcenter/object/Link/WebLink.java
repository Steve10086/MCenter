package com.astune.mcenter.object.Link;

import com.astune.mcenter.object.Device;

public class WebLink extends Link{
    private String address;

    public WebLink(String name, Device parent, String address){
        this.setName(name);
        this.setParent(parent);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
