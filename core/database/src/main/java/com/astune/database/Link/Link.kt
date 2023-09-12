package com.astune.database.Link

import com.astune.model.LinkType

interface Link {
    val id:Int
    val name: String
    val parent: Int
    val type: LinkType
    val info: String
}
