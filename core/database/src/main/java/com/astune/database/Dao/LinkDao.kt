package com.astune.mcenter.`object`.Dao

import com.astune.database.Link.Link

interface LinkDao<T: Link> {
    fun getAll():List<T>

    fun getByDevice(parentId: Int):List<T>

    suspend fun insert(link: T)

    suspend fun update(link: T)

    suspend fun delete(link: T)

    suspend fun resetPrimaryKey()
}