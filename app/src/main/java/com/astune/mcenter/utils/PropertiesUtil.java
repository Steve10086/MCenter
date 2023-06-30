package com.astune.mcenter.utils;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.room.util.FileUtil;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PropertiesUtil {

    //get mapping of key value and corresponding data
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


    public static void copyFileFAssets(Context context, String name, String newPath) throws IOException {
        Files.copy(context.getAssets().open(name), Paths.get(newPath));
    }
}
