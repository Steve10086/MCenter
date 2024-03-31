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
        }catch (_:Exception){
            try {
                setAvatar(BitmapFactory.decodeStream(context.assets.open("avatar.png")))
                getAvatar()
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

    fun setAvatar(uri:Uri) : Bitmap{
        val avatarInputStream = context.contentResolver.openInputStream(uri)
        return setAvatar(BitmapFactory.decodeStream(avatarInputStream))
    }


    fun setAvatar(avatar:Bitmap) : Bitmap {
        if (File(context.filesDir.toString() + Environment.AVATAR_PATH).isFile
            || File(context.filesDir.toString() + "/img").mkdirs()
        ) {
            val avatarOutputStream =
                Files.newOutputStream(Paths.get(context.filesDir.toString() + Environment.AVATAR_PATH))
            val newSize = if(avatar.width > avatar.height) avatar.height else avatar.width
            var newAvatar = Bitmap.createBitmap(
                avatar,
                (avatar.width - newSize) / 2,
                (avatar.height - newSize) / 2,
                newSize,
                newSize,
            )
            if(newSize > 1024){
                newAvatar = Bitmap.createScaledBitmap(
                    newAvatar,
                    1024,
                    1024,
                    true
                )
            }

            newAvatar.compress(Bitmap.CompressFormat.JPEG, 100, avatarOutputStream)
            avatarOutputStream.close()

            return newAvatar
        } else {
            FileUtil.delete(context.filesDir.toString() + "/img")
            return setAvatar(avatar)
        }
    }


}