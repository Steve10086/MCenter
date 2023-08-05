package com.astune.database.Link

import com.astune.model.LinkType

interface Link {
    val name: String
    val parent: Int
    val type: LinkType
    val info: String
}
