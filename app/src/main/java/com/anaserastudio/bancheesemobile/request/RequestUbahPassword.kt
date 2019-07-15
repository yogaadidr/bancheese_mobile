package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject


abstract class RequestUbahPassword(val context : Context, private val idUSer: String, private val password : String,
                                   private val passwordBaru: String, private val ulangPassword : String){

    fun execute(){
        val url = context.getString(R.string.web_api_url).toString()+"changePassword"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .addBodyParameter("id_user",idUSer)
            .addBodyParameter("password",password)
            .addBodyParameter("password_baru",passwordBaru)
            .addBodyParameter("ulang_password",ulangPassword)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestUbahPassword.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestUbahPassword.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}
