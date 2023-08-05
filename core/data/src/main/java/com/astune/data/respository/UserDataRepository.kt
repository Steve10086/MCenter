package com.astune.data.respository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.astune.data.utils.FileUtil
import com.astune.data.utils.PropertiesUtil
import com.astune.model.Environment
import com.astune.model.Properties
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    @ApplicationContext private val context: Context
){

    fun getAvatar():Bitmap{
        return try {
            BitmapFactory.decodeFile(context.filesDir.toString() + Environment.AVATAR_PATH)
        }catch (e:FileNotFoundException){
            try {
                BitmapFactory.decodeStream(context.assets.open("avatar.jpg"))
            }catch (e:FileNotFoundException){
                throw e // todo: handle
            }
        }
    }

    fun getUserData():Map<String, String>{
        try {
          return PropertiesUtil.getProperty(
                    context.filesDir.toString() + Environment.SETTING_PROPERTIES,
                    Properties.EMAIL,
                    Properties.THEME,
                    Properties.ZEROTIER,
                    Properties.PASSWORD
                ).toMap()
        } catch (e: FileNotFoundException) {
            Log.w("FileIO", "property not found")
            return try {
                File(context.filesDir.toString() + Environment.SETTING_PROPERTIES).deleteOnExit()
                Files.copy(
                    context.assets.open("userSetting.properties"),
                    Paths.get(context.filesDir.toString() + Environment.SETTING_PROPERTIES)
                )
                Log.i("FileIO", "property copied")
                getUserData()
            } catch (ex: IOException) {
                FileUtil.delete(context.filesDir.toString() + "/properties")
                getUserData()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun setAvatar(avatar:Bitmap):Boolean{

        return false
    }


}