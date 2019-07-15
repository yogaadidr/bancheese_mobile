package com.anaserastudio.bancheesemobile

import android.os.Bundle
import androidx.appcompat.widget.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.anaserastudio.bancheesemobile.adapter.ListDetailBahanBaku
import com.anaserastudio.bancheesemobile.model.DetailBahanBaku
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestDetailBahanBaku
import com.anaserastudio.bancheesemobile.request.RequestStok
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_bahan_baku.*
import org.json.JSONObject
import java.lang.Exception

class DetailBahanBakuActivity : BaseActivity() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: ListDetailBahanBaku? = null
    private var detailBahanBaku = ArrayList<DetailBahanBaku>()
    private lateinit var idBahan : String
    private var debet = 0
    private var kredit = 0
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bahan_baku)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Hari ini"
        try {
            text_nama_bahan.text = intent!!.extras!!.getString("nama_bahan")
            text_pemasukan.text = debet.toString()
            text_pengeluaran.text = kredit.toString()
            text_saldo_awal.text = "Saldo Awal: ${intent!!.extras!!.getInt("saldo_awal")}"
            idBahan = intent!!.extras!!.getInt("id_bahan").toString()
            //intent!!.extras!!.getInt("id_cabang")
        }catch (ex : Exception){
            onBackPressed()
        }
       // init()
    }

    private fun init(){
        detailBahanBaku = ArrayList()
        debet = 0
        kredit = 0
        initAdapter()
        getData()

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_detail_bahan)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            init()
        })
    }

    private fun getData(){
        object : RequestDetailBahanBaku(this@DetailBahanBakuActivity,pref.getString(getString(R.string.key_cabang_login),null)!!,idBahan){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val arr = resp.DATA.asJsonArray
                    for(tr in arr){
                        val head = Gson().fromJson(tr,DetailBahanBaku::class.java)
                        detailBahanBaku.add(head)
                        debet+=head.debet
                        kredit += head.kredit
                    }
                    mAdapter!!.notifyDataSetChanged()
                    if(arr.size() > 0){
                        text_not_found.visibility = TextView.GONE
                    }else{
                        text_not_found.visibility = TextView.VISIBLE
                    }
                    text_pemasukan.text = debet.toString()
                    text_pengeluaran.text = kredit.toString()

                }else{
                    text_not_found.visibility = TextView.VISIBLE

                }
                swipeRefreshLayout.isRefreshing = false
                progress_detail_bahan.visibility = ProgressBar.GONE
            }

            override fun onError(anError: ANError) {
                text_not_found.visibility = TextView.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                progress_detail_bahan.visibility = ProgressBar.GONE
                setToast(anError.message.toString())
            }
        }.execute()
    }

    fun delete(id : String, jenis : String){
        object : RequestStok(this, id,jenis){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                setToast("Data berhasil dihapus")
                init()
            }

            override fun onError(anError: ANError) {
            }

        }.delete()
    }

    private fun initAdapter(){
        detailBahanBaku = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)
        mAdapter = ListDetailBahanBaku(detailBahanBaku,this@DetailBahanBakuActivity, this@DetailBahanBakuActivity)
        val mLayoutManager = LinearLayoutManager(this@DetailBahanBakuActivity)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.isNestedScrollingEnabled = false

        recyclerView!!.removeItemDecoration(
            DividerItemDecoration(
                this@DetailBahanBakuActivity,
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

    override fun onResume() {
        super.onResume()
        init()
    }
}
