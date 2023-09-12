package com.astune.data.respository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.astune.data.utils.FileUtil
import com.astune.datastore.UserDataSource
import com.astune.model.Environment
import com.astune.model.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDataSource: UserDataSource
){
    val userData = userDataSource.userData

    fun getAvatar():Bitmap{
        return try {
            BitmapFactory.decodeFile(context.filesDir.toString() + Environment.AVATAR_PATH)
        }catch (e:NullPointerException){
            try {
                BitmapFactory.decodeStream(context.assets.open("avatar.jpg"))
            }catch (e:FileNotFoundException){
                throw e // todo: handle
            }
        }
    }


    suspend fun getUserData():UserInfo{
        val info: UserInfo = userDataSource.userData.first()
        info.avatar = getAvatar()
        return info
    }

    suspend fun setUserData(userInfo: UserInfo){
        userDataSource.setUserData(userInfo)
    }

    suspend fun setTheme(theme:String){
        userDataSource.setTheme(theme)
    }

    fun setAvatar(uri:Uri):Boolean{
        if (File(context.filesDir.toString() + Environment.AVATAR_PATH).isFile
            || File(context.filesDir.toString() + "/img").mkdirs()
        ) {
            val avatarInputStream = context.contentResolver.openInputStream(uri)
            val avatarOutputStream =
                Files.newOutputStream(Paths.get(context.filesDir.toString() + Environment.AVATAR_PATH))
            BitmapFactory.decodeStream(avatarInputStream).compress(Bitmap.CompressFormat.JPEG, 100, avatarOutputStream)
            avatarInputStream!!.close()
            avatarOutputStream.close()
        } else {
            FileUtil.delete(context.filesDir.toString() + "/img")
        }
        return false
    }


}