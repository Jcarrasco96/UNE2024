package com.jcarrasco96.une2024.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.GzipSource
import okio.buffer

class GzipInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request = chain.request()
            .newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .build()
        val response = chain.proceed(newRequest)

        return if (isGzipped(response)) {
            unzip(response)
        } else {
            response
        }
    }

    private fun unzip(response: Response): Response {
        response.body?.let {
            val responseBody = GzipSource(it.source())
                .buffer()
                .readUtf8()
                .toResponseBody(it.contentType())
            val strippedHeaders = response
                .headers
                .newBuilder()
                .removeAll("Content-Encoding")
                .removeAll("Content-Length")
                .build()
            return response.newBuilder()
                .headers(strippedHeaders)
                .body(responseBody)
                .message(response.message)
                .build()
        }
        return response
    }

    private fun isGzipped(response: Response) = response.header("Content-Encoding") == "gzip"

}
