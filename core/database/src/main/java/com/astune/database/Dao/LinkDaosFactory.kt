package com.astune.database.Dao

import com.astune.database.Link.Link
import com.astune.core.database.MCenterDB
import com.astune.core.database.WebLink
import com.astune.mcenter.core.database.Dao.LinkDao
import com.astune.model.LinkType
import dagger.assisted.AssistedFactory

@AssistedFactory
interface LinkDaosFactory {
    fun create(type: LinkType):LinkDao<out Link>
}

/**
 * get corresponding Dao by the giving linkType
 */
class LinkDaosFactoryImpl(
    private val database:MCenterDB
):LinkDaosFactory{
    override fun create(type: LinkType): LinkDao<out Link> = when(type){
        LinkType.WEB_LINK -> {
            database.webLinkDao()
        }

        LinkType.SSH_LINK -> {
            database.sshLinkDao()
        }

        else -> {
            throw IllegalArgumentException("unavailable link-type!")
        }
    }
}