package com.astune.data.utils;

import java.io.File;

/**
 * this method is copied from web, used in error catching blocks to reset the file structure
 */
public class FileUtil {
    public static File getFile(String dir,  String filename){
        return new File(dir, filename);
    }

    /**
     * delete file, can ba file or dir
     *
     * @return true if success，else false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * delete single file
     *
     * @return true if success，else false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * delete dir
     *

     * @return true if success，else false
     */
    public static boolean deleteDirectory(String dir) {
        // add separator automatically if needed
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // exit if not a dir or not exist
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // delete all files & dirs under dir
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // delete files
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // delete dirs
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("fail！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("delete" + dir + "success！");
            return true;
        } else {
            return false;
        }
    }
}
