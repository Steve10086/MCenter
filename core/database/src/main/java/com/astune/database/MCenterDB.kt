package com.astune.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astune.database.dao.DeviceDao
import com.astune.database.dao.SSHLinkDao
import com.astune.database.dao.WebLinkDao
import com.astune.database.dao.ZerotierDao

@Database(entities = [Device::class, WebLink::class, SSHLink::class, ZeroTier::class], version = 1, exportSchema = false)
abstract class MCenterDB: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun webLinkDao(): WebLinkDao
    abstract fun sshLinkDao(): SSHLinkDao
    abstract fun zerotierDao(): ZerotierDao

}