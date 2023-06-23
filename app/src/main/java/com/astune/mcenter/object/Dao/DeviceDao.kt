package com.astune.mcenter.`object`.Dao

import androidx.room.*
import com.astune.mcenter.`object`.Room.Device
import com.astune.mcenter.`object`.Room.WebLink

@Dao
interface DeviceDao {
    @Query("Select * from device")
    fun getAll():List<Device>

    @Insert
    fun insert(device: Device)

    @Update
    fun update(device: Device)

    @Delete
    fun delete(device: Device)
}

@Dao
interface WebLinkDao {
    @Query("Select * from weblink")
    fun getAll():List<WebLink>

    @Insert
    fun insert(weblink: WebLink)

    @Update
    fun update(weblink: WebLink)

    @Delete
    fun delete(weblink: WebLink)
}