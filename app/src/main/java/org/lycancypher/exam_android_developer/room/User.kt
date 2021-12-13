package org.lycancypher.exam_android_developer.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String?,
    @ColumnInfo(name = "ap_pat") val apPat: String?,
    @ColumnInfo(name = "ap_mat") val apMat: String?,
    @ColumnInfo val phone: String?,
    @ColumnInfo val mail: String?,
    @ColumnInfo val lat: String?,
    @ColumnInfo val longitude: String?,
    @ColumnInfo(name = "usr_pic") val usrPic: String?
)
