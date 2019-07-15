package com.anaserastudio.bancheesemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaserastudio.bancheesemobile.model.BahanBaku
import com.anaserastudio.bancheesemobile.model.Response
import com.anaserastudio.bancheesemobile.request.RequestDaftarBahanBaku
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_daftar_nama_bahan.*
import org.json.JSONObject

class DaftarNamaBahan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var listDataBahan = ArrayList<BahanBaku>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_nama_bahan)
        init()
    }


    private fun init(){
        recyclerView = findViewById(R.id.recycler_view)
        viewManager = LinearLayoutManager(this)
        viewAdapter = AdapterBahan(listDataBahan,this@DaftarNamaBahan)
        val mLayoutManager = LinearLayoutManager(this@DaftarNamaBahan)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = viewAdapter
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                this@DaftarNamaBahan,
                LinearLayoutManager.VERTICAL
            )
        )

        getBahan()

    }

    private fun getBahan(){
        progressBar4.visibility = ProgressBar.VISIBLE

        object : RequestDaftarBahanBaku(this@DaftarNamaBahan){
            override fun onResponse(response: JSONObject) {
                super.onResponse(response)
                val resp = Gson().fromJson(response.toString(), Response::class.java)
                if(resp.CODE == 200){
                    val arr = resp.DATA.asJsonArray
                    for(tr in arr){
                        listDataBahan.add(Gson().fromJson(tr, BahanBaku::class.java))
                    }
                    viewAdapter.notifyDataSetChanged()
                }else{

                }
                progressBar4.visibility = ProgressBar.GONE
            }

            override fun onError(anError: ANError) {
                progressBar4.visibility = ProgressBar.GONE

            }

        }.execute()
    }

    fun selectBahan(bahan : BahanBaku){
        val intent = Intent()
        intent.putExtra("id_bahan", bahan.ID_BAHAN)
        intent.putExtra("satuan", bahan.SATUAN)
        intent.putExtra("nama", bahan.NAMA_BAHAN)
        setResult(1002,intent)
        finish()
    }

    class AdapterBahan(
        private val bahan: ArrayList<BahanBaku>,
        private val activity: DaftarNamaBahan
    ) :

        RecyclerView.Adapter<AdapterBahan.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_text_arrow, parent, false)

            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AdapterBahan.ViewHolder, position: Int) {
            val lap = bahan[position]
            holder.text_list.text = lap.NAMA_BAHAN
            holder.layout.setOnClickListener {
                activity.selectBahan(bahan[position])
            }
        }


        override fun getItemCount(): Int {
            return bahan.size
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var layout: ConstraintLayout = view.findViewById(R.id.layout) as ConstraintLayout
            var text_list: TextView = view.findViewById(R.id.text_list) as TextView

        }

    }

}
