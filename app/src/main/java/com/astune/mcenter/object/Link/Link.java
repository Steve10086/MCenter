package com.astune.mcenter.object.Link;

import com.astune.mcenter.object.Room.Device;

public class Link {
    private Device parent;

    private String type;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParent(Device parent) {
        this.parent = parent;
    }

    public Device getParent() {
        return parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
