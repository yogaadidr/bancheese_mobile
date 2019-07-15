package com.anaserastudio.bancheesemobile.request

import android.content.Context
import com.anaserastudio.bancheesemobile.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.anaserastudio.bancheesemobile.tool.NetworkingTool
import org.json.JSONObject
import com.androidnetworking.common.Priority

abstract class RequestStok{
    var context : Context
    private var id : String? = null
    private var jenis : String = ""
    private var idBahan : Int = 0
    private var idCabang: String = ""
    private var idUser : String = ""
    private var qty : Int = 0
    private var harga : Int = 0

    constructor(context : Context,
                id : String?,
                jenis : String,
                idBahan : Int,
                idCabang: String,
                idUser : String,
                qty : Int,
                harga : Int){
        this.context = context
        this.id = id
        this.jenis = jenis
        this.idBahan = idBahan
        this.idCabang = idCabang
        this.idUser = idUser
        this.qty = qty
        this.harga = harga
    }

    constructor(context: Context,id : String, jenis : String){
        this.context = context
        this.id = id
        this.jenis = jenis

    }

    fun add(){
        val url = context.getString(R.string.web_api_url).toString()+jenis
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .addBodyParameter("id_bahan",idBahan.toString())
            .addBodyParameter("id_cabang",idCabang)
            .addBodyParameter("id_user",idUser)
            .addBodyParameter("qty",qty.toString())
            .addBodyParameter("harga",harga.toString())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestStok.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestStok.onError(anError!!)
                }
            })
    }

    fun edit(){

        var param = "id_debet"
        if (jenis=="kredit"){
            param = "id_kredit"
        }

        val url = context.getString(R.string.web_api_url).toString()+jenis+"/"+id!!
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.post(url)
            .setPriority(Priority.MEDIUM)
            .addBodyParameter(param,id)
            .addBodyParameter("id_bahan",idBahan.toString())
            .addBodyParameter("id_cabang",idCabang)
            .addBodyParameter("id_user",idUser)
            .addBodyParameter("qty",qty.toString())
            .addBodyParameter("harga",harga.toString())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestStok.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestStok.onError(anError!!)
                }
            })
    }

    fun delete(){
        val url = context.getString(R.string.web_api_url).toString()+jenis+"/"+id!!
        AndroidNetworking.initialize(context, NetworkingTool().defaultTimeOut())
        AndroidNetworking.enableLogging()
        AndroidNetworking.delete(url)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    this@RequestStok.onResponse(response!!)
                }
                override fun onError(anError: ANError?) {
                    this@RequestStok.onError(anError!!)
                }
            })
    }

    open fun onResponse(response : JSONObject){

    }

    abstract fun onError(anError: ANError)
}
