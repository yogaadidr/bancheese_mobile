package com.anaserastudio.bancheesemobile.request

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject
import com.androidnetworking.common.Priority
import android.provider.Settings.Secure
import com.anaserastudio.bancheesemobile.DaftarTransaksiActivity
import com.anaserastudio.bancheesemobile.model.Response
import com.google.gson.Gson

abstract class RequestCabang(val context : Context){

    @SuppressLint("HardwareIds")
    fun execute(){
        val androidId = Secure.getString(context.contentResolver,Secure.ANDROID_ID)
        val url = context.getString(R.string.web_api_url).toString()+"daftarCabang/all"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(url)
            .addQueryParameter("id_device",androidId)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestCabang.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestCabang.onError(anError!!)
                }
            })
    }

    fun getMyCabang(){
        val androidId = Secure.getString(context.contentResolver,Secure.ANDROID_ID)
        val url = context.getString(R.string.web_api_url).toString()+"daftarCabang/store"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(url)
            .addQueryParameter("id_device",androidId)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestCabang.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestCabang.onError(anError!!)
                }
            })
    }

    @SuppressLint("HardwareIds")
    fun hapusAkses(idCabang : Int){
        val androidId = Secure.getString(context.contentResolver,Secure.ANDROID_ID)
        val url = context.getString(R.string.web_api_url).toString()+"daftarCabang/"+idCabang
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.delete(url)
            .addBodyParameter("id_device",androidId)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestCabang.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestCabang.onError(anError!!)
                }
            })
    }

    @SuppressLint("HardwareIds")
    fun addCabang(idCabang : Int){
        val androidId = Secure.getString(context.contentResolver,Secure.ANDROID_ID)
        val url = context.getString(R.string.web_api_url).toString()+"daftarCabang/"+idCabang
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .addBodyParameter("id_device",androidId)
            .addBodyParameter("device_name",Build.MODEL)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestCabang.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestCabang.onError(anError!!)
                }
            })
    }

    fun getLatestVersion(){
        val url = context.getString(R.string.web_api_url).toString()+"getLatestVersion"
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestCabang.onResponse(response!!)
                    val resp = Gson().fromJson(response.toString(), Response::class.java)
                    if (resp.CODE == 200) {
                        val data= resp.DATA.asJsonObject
                        val pref = context.getSharedPreferences(context.getString(R.string.pref_string),0)
                        val edit = pref.edit()
                        edit.putString(context.getString(R.string.app_latest_version),data.get("VERSION").toString())
                        edit.apply()
                        edit.commit()
                    }
                }
                override fun onError(anError: ANError?) {
                    this@RequestCabang.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}
