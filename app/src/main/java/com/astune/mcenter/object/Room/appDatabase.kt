package com.astune.mcenter.`object`.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astune.mcenter.`object`.Dao.DeviceDao

@Database(entities = [Device::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}