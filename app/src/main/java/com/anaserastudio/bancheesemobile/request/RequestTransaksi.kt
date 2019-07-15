package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.anaserastudio.bancheesemobile.model.TransaksiHeader
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject


abstract class RequestTransaksi(val context : Context){

    fun execute(trxHeader: TransaksiHeader){
        val obj = JSONObject(Gson().toJson(trxHeader))
        val url = context.getString(R.string.web_api_url).toString()+"addTransaksi"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .setContentType("application/json; charset=utf-8")
            .addJSONObjectBody(obj)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestTransaksi.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestTransaksi.onError(anError!!)
                }
            })
    }

    fun setVoid(idTransaksi : String){
        val url = context.getString(R.string.web_api_url).toString()+"transaksi/"+idTransaksi+"/void"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .setContentType("application/json; charset=utf-8")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestTransaksi.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestTransaksi.onError(anError!!)
                }
            })
    }

    fun deleteTransaksi(idTransaksi : String){
        val url = context.getString(R.string.web_api_url).toString()+"transaksi/"+idTransaksi
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.delete(url)
            .setPriority(Priority.MEDIUM)
            .setContentType("application/json; charset=utf-8")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestTransaksi.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestTransaksi.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}