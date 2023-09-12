package com.astune.data.utils

import java.io.File

/**
 * this method is copied from web, used in error catching blocks to reset the file structure
 */
object FileUtil {

    /**
     * delete file, can ba file or dir
     *
     * @return true if success，else false
     */
    fun delete(fileName: String): Boolean {
        val file = File(fileName)
        return if (!file.exists()) {
            false
        } else {
            if (file.isFile) deleteFile(fileName) else deleteDirectory(
                fileName
            )
        }
    }

    /**
     * delete single file
     *
     * @return true if success，else false
     */
    fun deleteFile(fileName: String): Boolean {
        val file = File(fileName)
        // if is file, delete it
        return if (file.exists() && file.isFile) {
            file.delete()
        } else {
            false
        }
    }

    /**
     * delete dir
     *
     *
     * @return true if success，else false
     */
    fun deleteDirectory(dir: String): Boolean {
        // add separator automatically if needed
        var pathname = dir
        if (!pathname.endsWith(File.separator)) pathname = pathname + File.separator
        val dirFile = File(pathname)
        // exit if not a dir or not exist
        if (!dirFile.exists() || !dirFile.isDirectory) {
            return false
        }
        var flag = true
        // delete all files & dirs under dir
        val files = dirFile.listFiles()
        if (files != null) {
            for (i in files.indices) {
                // delete files
                if (files[i].isFile) {
                    flag = deleteFile(files[i].absolutePath)
                    if (!flag) break
                } else if (files[i].isDirectory) {
                    flag = deleteDirectory(
                        files[i]
                            .absolutePath
                    )
                    if (!flag) break
                }
            }
        }
        if (!flag) {
            println("fail！")
            return false
        }
        // 删除当前目录
        return if (dirFile.delete()) {
            println("delete" + pathname + "success！")
            true
        } else {
            false
        }
    }
}
