package com.jcarrasco96.une2024.services

import android.content.Context
import com.google.gson.GsonBuilder
import com.jcarrasco96.une2024.interceptors.ConnectVerifierInterceptor
import com.jcarrasco96.une2024.interceptors.GzipInterceptor
import com.jcarrasco96.une2024.models.ApiDataJson
import com.jcarrasco96.une2024.utils.NetworkUtils
import com.jcarrasco96.une2024.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object API {

    private fun service(): Retrofit {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(ConnectVerifierInterceptor())
            addInterceptor(GzipInterceptor())
            addNetworkInterceptor {
                it.proceed(it.request())
            }
//            connectTimeout(10, TimeUnit.SECONDS)
//            readTimeout(10, TimeUnit.SECONDS)
//            writeTimeout(10, TimeUnit.SECONDS)
        }

        val gson = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl("http://${NetworkUtils.HOST}:${NetworkUtils.PORT}/une2024/")
//            .baseUrl("https://une2024.jcarrasco96.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()
    }

    private fun backupService() = service().create(BackupService::class.java)

    suspend fun save(data: ApiDataJson) = backupService().save(data)

    suspend fun view(context: Context) = backupService().view(Utils.androidId(context))

}