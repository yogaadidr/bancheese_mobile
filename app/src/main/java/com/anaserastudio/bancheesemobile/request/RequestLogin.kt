package com.anaserastudio.bancheesemobile.request

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject
import com.androidnetworking.common.Priority

abstract class RequestLogin(val context : Context, private val username : String, private val password : String){

    @SuppressLint("HardwareIds")
    fun execute(){
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val url = context.getString(R.string.web_api_url).toString()+"loginMobile"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .addBodyParameter("username",username)
            .addBodyParameter("password",password)
            .addBodyParameter("id_device",androidId)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestLogin.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestLogin.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}
