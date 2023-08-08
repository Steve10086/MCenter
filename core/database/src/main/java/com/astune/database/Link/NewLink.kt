package com.astune.database.Link

import com.astune.model.LinkType

/**
 * the Link Insertion btn
 */
class NewLink(override val name: String, override val parent: Int) : Link {
    override val type = LinkType.NEW_LINK
    override val info: String
        get() = ""
}
