package com.jcarrasco96.une2024.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private const val PREF_NAME = "pref_une2024"

    private const val PREF_READ = "pref_read"
    private const val PREF_NIGHT_MODE = "pref_night_mode"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun read(read: Long) = preferences.edit().putLong(PREF_READ, read).apply()

    fun read(): Long = preferences.getLong(PREF_READ, 0)

    fun nightMode(nightMode: Boolean) = preferences.edit().putBoolean(PREF_NIGHT_MODE, nightMode).apply()

    fun nightMode(): Boolean = preferences.getBoolean(PREF_NIGHT_MODE, false)

}