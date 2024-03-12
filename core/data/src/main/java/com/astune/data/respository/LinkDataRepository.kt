package com.astune.data.respository

import com.astune.common.Dispatcher
import com.astune.common.Dispatchers.IO
import com.astune.database.Link.Link
import com.astune.database.dao.LinkDaoFactory
import com.astune.model.LinkType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LinkDataRepository @Inject constructor(
    private val linkDaoFactory: LinkDaoFactory,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getLinkList(parentId:Int): Flow<List<Link>> =
        flow {
            for(linkType in LinkType.values()){
                if (linkType != LinkType.NEW_LINK && linkType != LinkType.EMPTY_LINK){
                    emit(linkDaoFactory.create(linkType).getByDevice(parentId))
                }
            }
        }.flowOn(ioDispatcher)

    fun getLink(id:Int, linkType: LinkType): Flow<Link> = flow {
        emit(linkDaoFactory.create(linkType).get(id))
    }

    suspend fun insertLink(link: Link) = linkDaoFactory.create(link.type).insert(link)

    suspend fun deleteLink(link: Link) = linkDaoFactory.create(link.type).delete(link)

    suspend fun deleteParent(parent:Int){
        for (type in LinkType.getApplicableList()){
            linkDaoFactory.create(type).deleteByParent(parent)
        }
    }

}