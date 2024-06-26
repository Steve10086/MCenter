package com.astune.database.dao

import androidx.room.*
import com.astune.database.Device
import com.astune.database.SSHLink
import com.astune.database.WebLink
import com.astune.database.ZeroTier


@Dao
interface DeviceDao {
    @Query("Select * from device where id = :id")
    fun getDevice(id:Int):Device

    @Query("Select * from device where ip = :ip")
    fun getDevice(ip:String):Device

    @Query("Select last_delay from device where id = :id")
    fun getDeviceDelay(id:Int):String

    @Query("Select * from device")
    fun getAll():List<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: Device)

    @Delete
    suspend fun delete(device: Device)

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
    suspend fun insert(device: Device)

    @Delete
    suspend fun delete(device: Device)
}


@Dao
interface WebLinkDao : LinkDao<WebLink> {
    @Query("Select * from weblink")
    override fun getAll():List<WebLink>

    @Query("Select * from weblink where parent = :parentId")
    override fun getByDevice(parentId: Int):List<WebLink>

    @Query("Select * from sshlink where id = :id")
    override fun get(id: Int):WebLink

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(link: WebLink)

    @Update
    override suspend fun update(link: WebLink)

    @Delete
    override suspend fun delete(link: WebLink)

    @Query("DELETE FROM weblink WHERE parent = :parent")
    override suspend fun deleteByParent(parent: Int)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'WebLink'")
    override suspend fun resetPrimaryKey()
}

@Dao
interface SSHLinkDao: LinkDao<SSHLink> {
    @Query("Select * from SSHLink")
    override fun getAll():List<SSHLink>

    @Query("Select * from sshlink where parent = :parentId")
    override fun getByDevice(parentId: Int):List<SSHLink>

    @Query("Select * from sshlink where id = :id")
    override fun get(id: Int):SSHLink

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(link: SSHLink)
    @Update
    override suspend fun update(link: SSHLink)

    @Delete
    override suspend fun delete(link: SSHLink)

    @Query("DELETE FROM sshlink WHERE parent = :parent")
    override suspend fun deleteByParent(parent: Int)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'SSHLink'")
    override suspend fun resetPrimaryKey()
}
