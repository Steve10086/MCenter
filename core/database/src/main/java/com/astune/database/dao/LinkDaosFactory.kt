package com.astune.database.dao

import com.astune.database.Link.Link
import com.astune.database.MCenterDB
import com.astune.model.LinkType
import javax.inject.Inject


/**
 * get corresponding Dao by the giving linkType
 */
class LinkDaoFactory @Inject constructor(
    private val database: MCenterDB
){
    fun create(type: LinkType): LinkDao<Link> = when(type){
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