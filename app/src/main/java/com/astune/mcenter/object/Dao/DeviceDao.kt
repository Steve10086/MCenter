package com.astune.mcenter.`object`.Dao

import androidx.room.*
import com.astune.mcenter.`object`.Room.Device
import com.astune.mcenter.`object`.Room.WebLink
import io.reactivex.rxjava3.core.Completable

@Dao
interface DeviceDao {
    @Query("Select * from device")
    fun getAll():List<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: Device): Completable

    @Delete
    fun delete(device: Device)
}

@Dao
interface WebLinkDao {
    @Query("Select * from weblink")
    fun getAll():List<WebLink>

    @Insert
    suspend fun insert(weblink: WebLink)

    @Update
    suspend fun update(weblink: WebLink)

    @Delete
    suspend fun delete(weblink: WebLink)
}