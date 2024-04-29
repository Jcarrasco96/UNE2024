package com.jcarrasco96.une2024.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jcarrasco96.une2024.utils.Utils

@Entity(tableName = "registry")
data class Registry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val read: Long,
    @ColumnInfo(name = "last_read")
    val lastRead: Long,
    val date: String = Utils.dateTime(),
    var official: Boolean = false
) {

    fun consume(): Long = read - lastRead

}