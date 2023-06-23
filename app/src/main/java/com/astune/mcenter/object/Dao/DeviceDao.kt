package com.astune.mcenter.`object`.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.astune.mcenter.`object`.Room.Device

@Dao
interface DeviceDao {
    @Query("Select * from device")
    fun getAll():List<Device>

    @Insert
    fun insert(device: Device)

    @Delete
    fun delete(device: Device)
}