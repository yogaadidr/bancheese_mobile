package com.anaserastudio.bancheesemobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.*
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anaserastudio.bancheesemobile.adapter.ListLaporanBahan
import com.anaserastudio.bancheesemobile.model.DetailBahanBaku
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestBahanBaku
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daftar_bahan_baku.*
import org.json.JSONObject

class DaftarBahanBaku : BaseActivity() {

    lateinit var recyclerView: RecyclerView
    private var mAdapter: ListLaporanBahan? = null
    private var laporanBahanBaku = ArrayList<DetailBahanBaku>()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_bahan_baku)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Daftar Bahan Baku"
    }

    override fun onResume() {
        super.onResume()
        init()

    }

    private fun init(){
        laporanBahanBaku = ArrayList()
        initAdapter()
        getData()

        btn_tambah_stok.setOnClickListener {
            val intent = Intent(this@DaftarBahanBaku, TambahStok::class.java)
            startActivityForResult(intent,REQUEST_ORDER)
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_bahan)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            init()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESPONSE_ORDER){
           // onResume()
        }
    }

    private fun getData(){
        val idCabang = pref.getString(getString(R.string.key_cabang_login),null)
        object : RequestBahanBaku(this@DaftarBahanBaku, idCabang!!){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val arr = resp.DATA.asJsonArray
                    for(tr in arr){
                        val head = Gson().fromJson(tr,DetailBahanBaku::class.java)
                        laporanBahanBaku.add(head)
                    }
                    mAdapter!!.notifyDataSetChanged()

                }else{

                }
                swipeRefreshLayout.isRefreshing = false
                loading_bahan.visibility = ProgressBar.GONE
            }

            override fun onError(anError: ANError) {
                swipeRefreshLayout.isRefreshing = false
                loading_bahan.visibility = ProgressBar.GONE
            }

        }.execute()
    }

    private fun initAdapter(){
        recyclerView = findViewById(R.id.recyclerView)
        mAdapter = ListLaporanBahan(laporanBahanBaku,this@DaftarBahanBaku)
        val mLayoutManager = LinearLayoutManager(this@DaftarBahanBaku)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter
        recyclerView.removeItemDecoration(
            DividerItemDecoration(
                this@DaftarBahanBaku,
                LinearLayoutManager.VERTICAL
            )
        )

        mAdapter!!.notifyDataSetChanged()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        this.finish()
        return true
    }

}
