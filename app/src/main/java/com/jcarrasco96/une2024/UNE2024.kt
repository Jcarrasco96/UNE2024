package com.jcarrasco96.une2024

import android.app.Application
import androidx.room.Room
import com.jcarrasco96.une2024.database.AppDatabase
import com.jcarrasco96.une2024.utils.Preferences

class UNE2024 : Application() {

    companion object {
        lateinit var db: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "une2024"
        ).allowMainThreadQueries().build()

        Preferences.init(this)
    }

}