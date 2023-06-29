package com.astune.mcenter.`object`.Room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true) val id:Int,

    @ColumnInfo (name = "name") var name: String,

    @ColumnInfo (name = "ip") var ip: String,

    @ColumnInfo (name = "last_online") var lastOnline: String?
){


    @Ignore var isOnLine: Boolean = false
}

@Entity
data class WebLink(

    @PrimaryKey(autoGenerate = true) val id:Int,

    @ColumnInfo val parent:Int,

    @ColumnInfo val name: String,

    @ColumnInfo val address: String,
)
