package com.astune.mcenter.`object`.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.astune.mcenter.`object`.Dao.DeviceDao

@Database(entities = [Device::class], version = 1, exportSchema = true)
abstract class MCenterDB: RoomDatabase() {
    abstract fun deviceDao(): DeviceDao

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
}