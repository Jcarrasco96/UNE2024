package com.jcarrasco96.une2024.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jcarrasco96.une2024.models.Meter
import com.jcarrasco96.une2024.models.Registry

@Database(entities = [Meter::class, Registry::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun meterDao(): MeterDao

    abstract fun registryDao(): RegistryDao

}