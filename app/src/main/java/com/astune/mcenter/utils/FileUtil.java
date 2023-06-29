package com.astune.mcenter.utils;

import android.content.Context;

import java.io.File;

public class FileUtil {
    public static File getFile(String dir,  String filename){
        return new File(dir, filename);
    }

}
