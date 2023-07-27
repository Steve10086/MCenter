package com.astune.mcenter.object.Link;

import com.astune.mcenter.utils.enums.LinkType;

public class NewLink implements Link {
    private final String name;
    private final int parentId;
    private final LinkType type = LinkType.NEW_LINK;

    public NewLink(int parentId, String name){
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
    public LinkType getType() {
        return type;
    }

    @Override
    public String getInfo() {
        return "";
    }
}
