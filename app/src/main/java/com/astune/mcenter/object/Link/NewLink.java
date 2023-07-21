package com.astune.mcenter.object.Link;

import com.astune.mcenter.utils.enums.LinkType;

public class NewLink implements Link {
    private String name;
    private int id;
    private int parentId;
    private final String type = LinkType.NEW_LINK;

    public NewLink(int id, int parentId, String name){
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getParentId() {
        return parentId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
