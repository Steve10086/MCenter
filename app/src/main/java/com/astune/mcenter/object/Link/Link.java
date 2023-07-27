package com.astune.mcenter.object.Link;

import com.astune.mcenter.utils.enums.LinkType;

public interface Link {
    String getName();

    int getParentId();

    LinkType getType();

    String getInfo();
}
