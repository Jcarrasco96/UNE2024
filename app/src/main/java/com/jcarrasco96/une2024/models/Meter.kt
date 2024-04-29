package com.jcarrasco96.une2024.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meter")
data class Meter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "id_meter")
    val idMeter: String,
    val name: String
)