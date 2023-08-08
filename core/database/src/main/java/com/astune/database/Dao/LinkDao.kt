package com.astune.database.Dao

import com.astune.database.Link.Link
import com.astune.model.LinkType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

interface LinkDao<T: Link> {
    fun getAll():List<T>

    fun getByDevice(parentId: Int):List<T>

    suspend fun insert(link: T)

    suspend fun update(link: T)

    suspend fun delete(link: T)

    suspend fun resetPrimaryKey()
}


class LinkDaos @AssistedInject constructor(
    @Assisted private val type:LinkType
)