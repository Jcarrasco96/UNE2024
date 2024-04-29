package com.jcarrasco96.une2024.database

import androidx.room.Dao
import androidx.room.Query
import com.jcarrasco96.une2024.models.Meter

@Dao
interface MeterDao: IUDDao<Meter> {

    @Query("SELECT * FROM meter")
    fun all(): List<Meter>

    @Query("SELECT * FROM meter WHERE id = :id")
    fun meter(id: Long): Meter

    @Query("DELETE FROM meter")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT 1 FROM meter WHERE id_meter = :id)")
    fun exists(id: String): Boolean

}