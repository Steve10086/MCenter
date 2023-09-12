package com.astune.model;

import java.util.ArrayList;
import java.util.List;

public enum LinkType {WEB_LINK("WebLink"), SSH_LINK("SSHLink"), NEW_LINK("NewLink"),EMPTY_LINK("");
    private final String name;

    LinkType(String name){
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

    public static List<LinkType> getApplicableList(){
        ArrayList<LinkType> results = new ArrayList<>();
        for(LinkType value : values()){
            if(value != NEW_LINK && value != EMPTY_LINK){
                results.add(value);
            }
        }
        return results;
    }

    public static List<String> getNames(List<LinkType> value){
        ArrayList<String> results = new ArrayList<>();
        for (LinkType linkType : value) {
            results.add(linkType.getName());
        }
        return results;
    }

    public static LinkType getFromName(String name){
        return LinkType.valueOf(name);
    }
}
