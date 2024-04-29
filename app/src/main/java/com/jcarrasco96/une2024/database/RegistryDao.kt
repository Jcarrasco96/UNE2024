package com.jcarrasco96.une2024.database

import androidx.room.Dao
import androidx.room.Query
import com.jcarrasco96.une2024.models.Registry

@Dao
interface RegistryDao: IUDDao<Registry> {

    @Query("SELECT * FROM registry")
    fun all(): List<Registry>

    @Query("SELECT * FROM registry WHERE id = :id")
    fun registry(id: Long): Registry

    @Query("DELETE FROM registry")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT 1 FROM registry WHERE id = :id)")
    fun exists(id: Long): Boolean

}