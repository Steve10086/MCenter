package com.astune.mcenter.utils.enums;

import java.util.ArrayList;
import java.util.List;

public enum LinkType {WEB_LINK("WebLink", 0), SSH_LINK("SSHLink", 1), NEW_LINK("NewLink", 3);
    private final String name;

    LinkType(String name, int index){
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static String[] getNames() {
        List<String> names = new ArrayList<>();
        for (LinkType linkType : values()){
            names.add(linkType.getName());
        }
        return names.toArray(new String[0]);
    }

    public static LinkType getFromName(String name){
        return LinkType.valueOf(name);
    }
}
