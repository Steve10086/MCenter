package com.astune.database

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.astune.database.Link.Link
import com.astune.model.LinkType

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true) val id:Int,

    @ColumnInfo (name = "name") var name: String,

    @ColumnInfo (name = "ip") var ip: String,

    @ColumnInfo (name = "last_online") var lastOnline: String? = null,

    @ColumnInfo (name = "last_delay") var latestDelay: String? = null,

    ) {
    @get:Ignore
    var delay by mutableStateOf("")

    @get:Ignore
    var loading by mutableStateOf(false)

    override fun toString(): String {
        return ("[id: $id, name: $name, ip: $ip, lastOnline: $lastOnline, latestDelay: $latestDelay], deLay: $delay")
    }

}

@Entity
data class ZeroTier(

    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo (name = "uid") val uid: Int,

    @ColumnInfo (name = "name") val name: String?
)

@Entity
data class WebLink(
    @PrimaryKey(autoGenerate = true) override val id:Int,

    @ColumnInfo override val name: String,

    @ColumnInfo override val parent: Int,

    @ColumnInfo(name = "address")override val info: String,
) : Link {
    @Ignore
    override val type: LinkType = LinkType.WEB_LINK
}

@Entity
data class SSHLink(

    @PrimaryKey(autoGenerate = true) override val id:Int,

    @ColumnInfo override val name: String,

    @ColumnInfo override val parent: Int,

    @ColumnInfo(name = "address")override val info: String,
) : Link {
    @Ignore
    override val type: LinkType = LinkType.SSH_LINK
}
