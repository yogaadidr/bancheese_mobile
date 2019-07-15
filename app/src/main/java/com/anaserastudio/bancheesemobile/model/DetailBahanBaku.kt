package com.anaserastudio.bancheesemobile.model

class DetailBahanBaku(var id_cabang : Int,
    var nama_cabang : String,
    var id_bahan : Int,
    var nama_bahan : String,
    var debet : Int,
    var kredit : Int,
    var tgl_transaksi : String,
    var harga : Any,
    var total_saldo : Int,
    var saldo_awal : Int,
    var id_debet : Int, var id_kredit : Int
)