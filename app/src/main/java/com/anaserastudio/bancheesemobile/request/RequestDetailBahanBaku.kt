package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject
import com.androidnetworking.common.Priority

abstract class RequestDetailBahanBaku(val context : Context, private val idCabang : String, private val idBahan : String){

    fun execute(){
        val url = context.getString(R.string.web_api_url).toString()+"vsaldoHarian/"+idCabang+"/detail/"+idBahan
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestDetailBahanBaku.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestDetailBahanBaku.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}
