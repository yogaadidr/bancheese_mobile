package com.anaserastudio.bancheesemobile.tool

import android.annotation.SuppressLint
import android.os.StrictMode
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class NetworkingTool {

//    private val TAG = "NetworkingTool"

    @SuppressLint("ObsoleteSdkInt")
    fun defaultTimeOut(): OkHttpClient {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        return OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }


}