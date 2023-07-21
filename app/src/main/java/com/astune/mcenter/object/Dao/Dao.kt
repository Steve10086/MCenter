package com.astune.mcenter.`object`.Dao

import androidx.room.*
import com.astune.mcenter.`object`.Room.Device
import com.astune.mcenter.`object`.Room.SSHLink
import com.astune.mcenter.`object`.Room.WebLink
import com.astune.mcenter.`object`.Room.ZeroTier
import io.reactivex.rxjava3.core.Completable


@Dao
interface DeviceDao {
    @Query("Select * from device")
    fun getAll():List<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: Device): Completable

    @Delete
    fun delete(device: Device): Completable

    @Query("Delete from device")
    fun deleteAll()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'Device'")
    fun resetPrimaryKey()
}

@Dao
interface ZerotierDao {
    @Query("Select * from zerotier")
    fun getAll():List<ZeroTier>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(device: Device): Completable

    @Delete
    fun delete(device: Device): Completable
}


@Dao
interface WebLinkDao :LinkDao<WebLink>{
    @Query("Select * from weblink")
    override fun getAll():List<WebLink>

    @Query("Select * from weblink where parent = :parentId")
    override fun getByDevice(parentId: Int):List<WebLink>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(link: WebLink): Completable

    @Update
    override fun update(link: WebLink): Completable

    @Delete
    override fun delete(link: WebLink): Completable

    @Query("DELETE FROM sqlite_sequence WHERE name = 'WebLink'")
    override fun resetPrimaryKey(): Completable
}

@Dao
interface SSHLinkDao: LinkDao<SSHLink>{
    @Query("Select * from SSHLink")
    override fun getAll():List<SSHLink>

    @Query("Select * from sshlink where parent = :parentId")
    override fun getByDevice(parentId: Int):List<SSHLink>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(link: SSHLink): Completable

    @Update
    override fun update(link: SSHLink): Completable

    @Delete
    override fun delete(link: SSHLink): Completable

    @Query("DELETE FROM sqlite_sequence WHERE name = 'SSHLink'")
    override fun resetPrimaryKey(): Completable
}
