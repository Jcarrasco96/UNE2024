package com.jcarrasco96.une2024.services

import com.jcarrasco96.une2024.models.ApiDataJson
import com.jcarrasco96.une2024.models.ApiDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BackupService {

    @POST("backup/save")
    suspend fun save(@Body dataJson: ApiDataJson): ApiDataResponse

    @GET("backup/{id}")
    suspend fun view(@Path("id") id: String): ApiDataJson

}