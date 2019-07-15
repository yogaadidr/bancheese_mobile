package com.anaserastudio.bancheesemobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestStok
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_tambah_stok.*
import org.json.JSONObject
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Exception


class TambahStok : BaseActivity() {

    val jenis = arrayOf("Pemasukan","Pengeluaran")
    val kode = arrayOf("DEBET","KREDIT")
    var id = ""
    private var isEdited = false
    var selectedIdBahan = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_stok)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_close_black_18dp)

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 1002){
            selectedIdBahan = data!!.extras!!.getInt("id_bahan")
            text_bahan_baku_stok.setText(data.extras!!.getString("nama")!!)
            text_satuan_stok.text = data.extras!!.getString("satuan")!!
        }
    }

    private fun init(){
        btn_pilih_bahan.setOnClickListener {
            val intent = Intent(this,DaftarNamaBahan::class.java)
            startActivityForResult(intent,1001)
        }

        val adapter = ArrayAdapter(this@TambahStok,android.R.layout.simple_dropdown_item_1line,jenis)
        spin_jenis_stok.adapter = adapter
        spin_jenis_stok.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if(jenis[position].toLowerCase() == "pemasukan"){
                    layout_harga.visibility = ConstraintLayout.VISIBLE
                }else{
                    layout_harga.visibility = ConstraintLayout.GONE

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }


        if(intent.hasExtra("menu") && intent!!.extras!!.getString("menu") == "ubah"){
            supportActionBar!!.title = "Ubah Stok"
            edit_harga_stok.setText(intent!!.extras!!.getString("harga"))
            edit_qty_stok.setText(intent!!.extras!!.getInt("qty").toString())
            text_bahan_baku_stok.setText(intent!!.extras!!.getString("nama"))
            text_satuan_stok.text = intent!!.extras!!.getString("satuan")

            selectedIdBahan = intent!!.extras!!.getInt("id_bahan")

            id = intent!!.extras!!.getInt("id").toString()

            isEdited = true
            spin_jenis_stok.isClickable = false
            spin_jenis_stok.isFocusable = false
            spin_jenis_stok.isEnabled = false
            val jenis = intent!!.extras!!.getString("jenis")
            if (jenis == "debet"){
                spin_jenis_stok.setSelection(0)
            }else{
                spin_jenis_stok.setSelection(1)
            }

        }else{
            supportActionBar!!.title = "Tambah Stok"
        }

        btn_simpan.setOnClickListener {
            val qty = edit_qty_stok.text.toString().toInt()

            if(qty == 0 || selectedIdBahan == -1){
                setToast("Periksa kembali masukan anda")
            }else{
                if (isEdited){
                    editStok()
                }else{
                    addStok()
                }
            }
        }

        btn_batal.setOnClickListener {
            onBackPressed()
        }
    }

    private fun addStok(){
        var harga = 0
        if (kode[spin_jenis_stok.selectedItemPosition].toLowerCase() == "debet"){
            harga = try{
                edit_harga_stok.text.toString().toInt()
            }catch (ex : Exception){
                0
            }
        }

        object : RequestStok(this@TambahStok,
            null,
            kode[spin_jenis_stok.selectedItemPosition].toLowerCase(),
            selectedIdBahan,
            pref.getString(getString(R.string.key_cabang_login),null)!!,
            pref.getString(getString(R.string.key_id_login),null)!!,
            edit_qty_stok.text.toString().toInt(),
            harga){

            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    setToast("Data berhasil ditambah")
                    val returnIntent = Intent()
                    setResult(RESPONSE_ORDER, returnIntent)
                    finish()
                }else{
                    setToast(resp.MESSAGE)
                }
            }
            override fun onError(anError: ANError) {
                setToast(anError.message!!)

            }

        }.add()
    }

    private fun editStok(){
        var harga = 0
        if (kode[spin_jenis_stok.selectedItemPosition].toLowerCase() == "debet"){
            harga = try{
                edit_harga_stok.text.toString().toInt()
            }catch (ex : Exception){
                0
            }
        }
        object : RequestStok(this@TambahStok,
            id,
            kode[spin_jenis_stok.selectedItemPosition].toLowerCase(),
            selectedIdBahan,
            pref.getString(getString(R.string.key_cabang_login),null)!!,
            pref.getString(getString(R.string.key_id_login),null)!!,
            edit_qty_stok.text.toString().toInt(),
            harga){

            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    setToast("Data berhasil diubah")
                    val returnIntent = Intent()
                    setResult(RESPONSE_ORDER, returnIntent)
                    finish()
                }else{
                    setToast("Gagal mengubah data")
                }
            }
            override fun onError(anError: ANError) {
            }

        }.edit()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }
}
