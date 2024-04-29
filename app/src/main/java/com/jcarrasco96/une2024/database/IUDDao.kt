package com.jcarrasco96.une2024.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface IUDDao<T> {

    @Insert
    fun insert(t: T)

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)

}