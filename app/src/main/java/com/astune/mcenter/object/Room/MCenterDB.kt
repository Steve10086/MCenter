package com.astune.mcenter.`object`.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.astune.mcenter.`object`.Dao.*
import com.astune.mcenter.`object`.Link.Link
import com.astune.mcenter.utils.enums.LinkType

@Database(entities = [Device::class, WebLink::class, SSHLink::class, ZeroTier::class], version = 1, exportSchema = true)
abstract class MCenterDB: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun webLinkDao(): WebLinkDao
    abstract fun sshLinkDao(): SSHLinkDao
    abstract fun zerotierDao(): ZerotierDao

    companion object{
        @Volatile private var db: MCenterDB? = null

        fun getDB(): MCenterDB?{
            return db
        }

        fun buildDB(context: Context){
            if (null == db) {
                Thread {
                    db = databaseBuilder(
                        context.applicationContext,
                        MCenterDB::class.java, "MCenterDB"
                    ).build()
                }.start()
            }
        }
    }

    /**
     * get corresponding Dao by the giving linkType
     */
    fun<T:Link> getCorrespondingLinkDao(type: LinkType): LinkDao<T> {
        return when(type){
            LinkType.WEB_LINK -> {
                webLinkDao() as LinkDao<T>
            }

            LinkType.SSH_LINK -> {
                sshLinkDao() as LinkDao<T>
            }

            else -> {
                throw IllegalArgumentException("unavailable linktype!")
            }
        }
    }
}