package com.astune.database

import android.os.Bundle
import androidx.core.os.bundleOf
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

    @ColumnInfo (name = "last_online") var lastOnline: String?


) {
    @Ignore
    var isOnLine: Boolean = false

    override fun toString(): String {
        return ("[id: $id, name: $name, ip: $ip, lastOnline: $lastOnline], isOnline: $isOnLine")
    }

    fun toBundle(): Bundle {
        return bundleOf(
            Pair("id", id),
            Pair("name", name),
            Pair("ip", ip),
            Pair("lasOnline", lastOnline),
            Pair("isOnline", isOnLine)
        )
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
    @PrimaryKey(autoGenerate = true) val id:Int,

    @ColumnInfo val address: String,

    @ColumnInfo override val name: String,

    @ColumnInfo override val parent: Int,

    @ColumnInfo(name = "address")override val info: String,
) : Link {
    @Ignore
    override val type: LinkType = LinkType.WEB_LINK
}

@Entity
data class SSHLink(

    @PrimaryKey(autoGenerate = true) val id:Int,

    @ColumnInfo val address: String,

    @ColumnInfo override val name: String,

    @ColumnInfo override val parent: Int,

    @ColumnInfo(name = "address")override val info: String,
) : Link {
    @Ignore
    override val type: LinkType = LinkType.SSH_LINK
}
