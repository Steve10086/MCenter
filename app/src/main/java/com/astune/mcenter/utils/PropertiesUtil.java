package com.astune.mcenter.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Property;
import androidx.room.util.FileUtil;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PropertiesUtil {

    /**
     * Get property from file in the dir
     * @param dir filepath
     * @param propertyKeys values needs from the property
     * @return map containing key and the value
     * @throws IOException when file not exist
     */
    public static Map<String, String> getProperty(String dir, String... propertyKeys) throws IOException {
        Map<String, String> result = new HashMap<>();

        FileReader reader = new FileReader(dir);
        Properties p = new Properties();
        p.load(reader);

        for (String key : propertyKeys) {
            result.put(key, p.getProperty(key));
        }

        p.clear();
        reader.close();

        return result;

    }

    /**
     * Set property onto file in the dir
     * @param dir filepath
     * @param settingMap key-value pairs that will be restored
     * @throws IOException when file not exist
     */
    public static void setProperty(String dir, Map<String, String> settingMap) throws IOException {
        Properties p = new Properties();
        FileWriter writer = new FileWriter(dir);

        for (String key: settingMap.keySet()){
            try {
                p.setProperty(key, settingMap.get(key));
            }catch (NullPointerException e){
                settingMap.put(key, "");
                p.setProperty(key, settingMap.get(key));
            }
        }

        p.store(writer, null);

        writer.close();
        p.clear();
    }


    public static void copyFileFromAssets(Context context, String name, String newPath) throws IOException {
        Files.copy(context.getAssets().open(name), Paths.get(newPath));
    }
}
