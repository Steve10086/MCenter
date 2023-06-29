package com.astune.mcenter.utils;

import java.io.*;
import java.util.*;

public class PropertiesUtil {

    //get mapping of key value and corresponding data
    public static Map<String, String> getProperty(String dir, String... propertyKeys) throws IOException {
        Map<String, String> result = new HashMap<>();

        FileReader reader = new FileReader(dir);
        Properties p = new Properties();
        p.load(reader);


        for (String key:propertyKeys){
            result.put(key, p.getProperty(key));
        }


        p.clear();
        reader.close();

        return result;
    }

    public static void setProperty(String dir, Map<String, String> settingMap) throws IOException {
        Properties p = new Properties();
        FileWriter writer = new FileWriter(dir);

        for (String key: settingMap.keySet()){
            p.setProperty(key, settingMap.get(key));
        }

        p.store(writer, null);

        writer.close();
        p.clear();
    }
}
