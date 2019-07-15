package com.anaserastudio.bancheesemobile.model

import java.io.Serializable
import java.sql.Date

class Transaksi : Serializable{
    var ID_TRANSAKSI_DETAIL: String? = null
    var ID_TRANSAKSI: String
    var ID_MENU_DETAIL: String
    var HARGA: Any
    var NAMA_MENU: String
    var QTY: Int = 0
    var DISKON: Double = 0.0
    var BAYAR: Int= 0
    lateinit var STATUS: String
    lateinit var NAMA_USER: String
    lateinit var TGL_TRANSAKSI : Date

    constructor(ID_TRANSAKSI_DETAIL: String?, ID_TRANSAKSI: String, ID_MENU_DETAIL: String, HARGA: Any,
        NAMA_MENU: String, QTY: Int, DISKON: Double){
        this.ID_TRANSAKSI = ID_TRANSAKSI
        this.ID_TRANSAKSI_DETAIL = ID_TRANSAKSI_DETAIL
        this.ID_MENU_DETAIL = ID_MENU_DETAIL
        this.HARGA = HARGA
        this.NAMA_MENU = NAMA_MENU
        this.QTY = QTY
        this.DISKON = DISKON
    }
    constructor(ID_TRANSAKSI_DETAIL: String?, ID_TRANSAKSI: String, ID_MENU_DETAIL: String, HARGA: Any,
                NAMA_MENU: String, QTY: Int, DISKON: Double, BAYAR : Int,STATUS : String, NAMA_USER : String, TGL_TRANSAKSI : Date){
        this.ID_TRANSAKSI = ID_TRANSAKSI
        this.ID_TRANSAKSI_DETAIL = ID_TRANSAKSI_DETAIL
        this.ID_MENU_DETAIL = ID_MENU_DETAIL
        this.HARGA = HARGA
        this.NAMA_MENU = NAMA_MENU
        this.QTY = QTY
        this.DISKON = DISKON
        this.BAYAR = BAYAR
        this.STATUS = STATUS
        this.NAMA_USER = NAMA_USER
        this.TGL_TRANSAKSI = TGL_TRANSAKSI
    }
}