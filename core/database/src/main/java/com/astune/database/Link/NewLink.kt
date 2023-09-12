package com.astune.database.Link

import com.astune.model.LinkType

/**
 * the Link Insertion btn
 */
class NewLink(override val name: String, override val parent: Int, override val id: Int = -1) : Link {
    override val type = LinkType.NEW_LINK
    override val info: String
        get() = ""
}

class EmptyLink(override val name: String = "", override val parent: Int = 0, override val id: Int = 0) : Link {
    override val type = LinkType.EMPTY_LINK
    override val info: String
        get() = ""
}
