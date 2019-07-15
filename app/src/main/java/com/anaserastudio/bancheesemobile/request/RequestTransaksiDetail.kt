package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject


abstract class RequestTransaksiDetail(val context : Context, private val id_transaksi: String){

    fun execute(){
        val url = context.getString(R.string.web_api_url).toString()+"apptransaksiDetail/"+id_transaksi
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestTransaksiDetail.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestTransaksiDetail.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}