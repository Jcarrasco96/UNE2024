package com.jcarrasco96.une2024.interceptors

import com.jcarrasco96.une2024.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectVerifierInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isNetworkAvailable()) {
            throw IOException("No Network Available!")
        }

        val request = chain.request()

        return chain.proceed(request)
    }

}