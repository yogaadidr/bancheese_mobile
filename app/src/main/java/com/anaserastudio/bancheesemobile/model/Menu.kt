package com.anaserastudio.bancheesemobile.model

import java.io.Serializable

class Menu : Serializable{
    var ID_MENU = ""
    var NAMA_MENU = ""
    var HARGA : Any
    var STATUS : String
    var ID_MENU_DETAIL = ""
    var ID_CABANG = ""
    var NAMA_CABANG = ""
    lateinit var transaksi : Transaksi

    constructor(id_menu: String,nama_menu : String, harga : Any, STATUS : String){
        this.ID_MENU_DETAIL = id_menu
        this.NAMA_MENU = nama_menu
        this.HARGA = harga
        this.STATUS = STATUS
    }

    constructor( id_menu: String, nama_menu : String,  harga : Int,  STATUS : String,  transaksi: Transaksi){
        this.ID_MENU_DETAIL = id_menu
        this.NAMA_MENU = nama_menu
        this.HARGA = harga
        this.STATUS = STATUS
        this.transaksi = transaksi
    }
}



//        detail :
//        ID_MENU_DETAIL
//        id_menu
//        ID_CABANG
//        harga_
//        nama_
//        STATUS
