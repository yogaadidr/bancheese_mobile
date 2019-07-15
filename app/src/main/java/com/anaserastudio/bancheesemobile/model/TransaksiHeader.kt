package com.anaserastudio.bancheesemobile.model

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class TransaksiHeader : Serializable{
    var ID_TRANSAKSI: String
    lateinit var ID_USER : String
    lateinit var ID_CABANG : String
    lateinit var TGL_TRANSAKSI : Date
    var STATUS : String
    lateinit var METODE_PEMBAYARAN : String
    var BAYAR : Int = 0
    lateinit var ID_TRANSAKSI_OLD : String
    var listTransaksi = ArrayList<Menu>()
    var QTY : Int = 0
    lateinit var NOMINAL : Any

    constructor(id_transaksi: String, id_user : String, id_cabang : String, tgl_transaksi : Date, status : String, bayar : Int){
        this.ID_TRANSAKSI = id_transaksi
        this.ID_USER = id_user
        this.ID_CABANG = id_cabang
        this.TGL_TRANSAKSI = tgl_transaksi
        this.STATUS = status
        this.BAYAR = bayar

    }
    constructor(id_transaksi: String, tgl_transaksi : Date, status : String, qty : Int,nominal : Int){
        this.ID_TRANSAKSI = id_transaksi
        this.TGL_TRANSAKSI = tgl_transaksi
        this.STATUS = status
        this.QTY = qty
        this.NOMINAL = nominal

    }
    constructor(id_transaksi: String, id_user : String, id_cabang : String, tgl_transaksi : Date, status : String, bayar : Int,listTransaksi : ArrayList<Menu>){
        this.ID_TRANSAKSI = id_transaksi
        this.ID_USER = id_user
        this.ID_CABANG = id_cabang
        this.TGL_TRANSAKSI = tgl_transaksi
        this.STATUS = status
        this.BAYAR = bayar
        this.listTransaksi = listTransaksi
    }
    constructor(id_transaksi: String, id_user : String, id_cabang : String, status : String, bayar : Int,listTransaksi : ArrayList<Menu>){
        this.ID_TRANSAKSI = id_transaksi
        this.ID_USER = id_user
        this.ID_CABANG = id_cabang
        this.STATUS = status
        this.BAYAR = bayar
        this.listTransaksi = listTransaksi
    }
}