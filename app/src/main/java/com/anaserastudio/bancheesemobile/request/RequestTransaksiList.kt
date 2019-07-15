package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject


abstract class RequestTransaksiList(val context : Context, private val id_user: String, private val id_cabang : String){

    fun execute(){
        val url = context.getString(R.string.web_api_url).toString()+"transaksiList"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .addBodyParameter("id_user",id_user)
            .addBodyParameter("id_cabang",id_cabang)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestTransaksiList.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestTransaksiList.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}