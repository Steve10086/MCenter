package com.astune.mcenter.`object`.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(

    @PrimaryKey val id:Int,

    @ColumnInfo (name = "name") val name: String?,

    @ColumnInfo (name = "ip") val ip: String?

)
