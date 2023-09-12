package com.astune.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum LinkType {WEB_LINK("WebLink", 0), SSH_LINK("SSHLink", 1), NEW_LINK("NewLink", 3),EMPTY_LINK("", -1);
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

    public static List<LinkType> getApplicableList(){
        return Arrays.stream(values()).filter(linkType -> linkType != NEW_LINK && linkType != EMPTY_LINK).toList();
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
