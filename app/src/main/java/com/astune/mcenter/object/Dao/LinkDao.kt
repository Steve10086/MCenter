package com.astune.mcenter.`object`.Dao

import com.astune.mcenter.`object`.Link.Link
import com.astune.mcenter.`object`.Room.WebLink
import io.reactivex.rxjava3.core.Completable

interface LinkDao<T:Link> {
    fun getAll():List<T>

    fun getByDevice(parentId: Int):List<T>

    fun insert(link: T): Completable

    fun update(link: T): Completable

    fun delete(link: T): Completable

    fun resetPrimaryKey(): Completable
}