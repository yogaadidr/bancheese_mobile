package com.anaserastudio.bancheesemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import com.anaserastudio.bancheesemobile.adapter.ListCabang
import com.anaserastudio.bancheesemobile.adapter.ListDetailBahanBaku
import com.anaserastudio.bancheesemobile.adapter.ListLaporanBahan
import com.anaserastudio.bancheesemobile.model.Cabang
import com.anaserastudio.bancheesemobile.model.DetailBahanBaku
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestCabang
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daftar_printer.*
import org.json.JSONObject

class DaftarCabang : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListCabang? = null
    private var daftarCabang = ArrayList<Cabang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_cabang)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_close_black_18dp)
        supportActionBar!!.title = "Pilih Outlet"

        init()
    }

    private fun init(){
        daftarCabang = ArrayList()
        initAdapter()
        requestCabang()
    }

    fun selectCabang(idCabang : Int){
        object : RequestCabang(this@DaftarCabang){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val intent = Intent()
                    setResult(1002, intent)
                    setToast("Perangkat ditambahkan")
                    finish()
                }else{
                    setToast("Gagal Menambah perangkat")

                }
            }

            override fun onError(anError: ANError) {
            }

        }.addCabang(idCabang)
    }

    private fun initAdapter(){
        recyclerView = findViewById(R.id.listCabang)
        mAdapter = ListCabang(daftarCabang,this@DaftarCabang,this@DaftarCabang)
        val mLayoutManager = LinearLayoutManager(this@DaftarCabang)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.removeItemDecoration(
            DividerItemDecoration(
                this@DaftarCabang,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun requestCabang(){
        progressBar.visibility = ProgressBar.VISIBLE

        object : RequestCabang(this@DaftarCabang){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val arr = resp.DATA.asJsonArray
                    for(tr in arr){
                        val head = Gson().fromJson(tr,Cabang::class.java)
                        daftarCabang.add(head)
                    }
                    mAdapter!!.notifyDataSetChanged()
                }else{

                }
                progressBar.visibility = ProgressBar.GONE
            }

            override fun onError(anError: ANError) {
                progressBar.visibility = ProgressBar.GONE

            }

        }.execute()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        setResult(1001, returnIntent)
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
