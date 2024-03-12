package com.astune.database.dao

import com.astune.model.LinkType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

interface LinkDao<out Link> {
    fun getAll():List<Link>

    fun getByDevice(parentId: Int):List<Link>

    fun get(id:Int):Link

    suspend fun insert(link: @UnsafeVariance Link)

    suspend fun update(link: @UnsafeVariance Link)

    suspend fun delete(link: @UnsafeVariance Link)

    suspend fun deleteByParent(parent: Int)

    suspend fun resetPrimaryKey()
}


class LinkDaos @AssistedInject constructor(
    @Assisted private val type:LinkType
)