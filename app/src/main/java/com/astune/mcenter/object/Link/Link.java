package com.astune.mcenter.object.Link;

import com.astune.mcenter.object.Room.Device;

public interface Link {
    String getName();

    int getParentId();

    String getType();

    String getInfo();
}
