package com.jcarrasco96.une2024.utils

import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket

object NetworkUtils {

    const val HOST = "192.168.0.188"
    const val PORT = 80

    fun isNetworkAvailable(): Boolean {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                if (networkInterface.isUp && !networkInterface.isLoopback) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isHostReachable(host: String = HOST, port: Int = PORT, timeoutMs: Int = 2000): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(host, port), timeoutMs)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }


}