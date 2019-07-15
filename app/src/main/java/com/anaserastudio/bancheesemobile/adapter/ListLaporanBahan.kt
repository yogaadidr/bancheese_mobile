package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anaserastudio.bancheesemobile.DetailBahanBakuActivity
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.model.DetailBahanBaku
import com.anaserastudio.bancheesemobile.model.LaporanBahanBaku
import kotlin.collections.ArrayList


class ListLaporanBahan(
    private val laporan: ArrayList<DetailBahanBaku>,
    private val mContext: Context
) :

    RecyclerView.Adapter<ListLaporanBahan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_laporan_bahan_baku, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListLaporanBahan.ViewHolder, position: Int) {
        val lap = laporan[position]
        holder.text_nama.text = lap.nama_bahan
        holder.text_detail.text = "${lap.total_saldo}"
        holder.layout.setOnClickListener {
            val intent = Intent(mContext, DetailBahanBakuActivity::class.java)
            intent.putExtra("nama_bahan",lap.nama_bahan)
            intent.putExtra("debet",lap.debet)
            intent.putExtra("kredit",lap.kredit)
            intent.putExtra("id_bahan",lap.id_bahan)
            intent.putExtra("id_cabang",lap.id_cabang)
            intent.putExtra("saldo_awal",lap.saldo_awal)
            mContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return laporan.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: ConstraintLayout = view.findViewById(R.id.layout_lap_bahan) as ConstraintLayout
        var text_nama: TextView = view.findViewById(R.id.text_lap_bahan_nama) as TextView
        var text_detail: TextView = view.findViewById(R.id.text_lap_bahan_detail) as TextView

    }

}
