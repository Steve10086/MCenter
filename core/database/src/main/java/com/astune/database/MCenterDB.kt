package com.astune.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.astune.database.Link.Link
import com.astune.mcenter.`object`.Dao.*
import com.astune.model.LinkType

@Database(entities = [Device::class, WebLink::class, SSHLink::class, ZeroTier::class], version = 1, exportSchema = false)
abstract class MCenterDB: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun webLinkDao(): WebLinkDao
    abstract fun sshLinkDao(): SSHLinkDao
    abstract fun zerotierDao(): ZerotierDao

}