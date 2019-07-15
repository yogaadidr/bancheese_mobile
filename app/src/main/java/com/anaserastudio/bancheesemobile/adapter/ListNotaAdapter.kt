package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.anaserastudio.bancheesemobile.DetailNotaActivity
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.model.TransaksiHeader
import com.anaserastudio.bancheesemobile.tool.StringControl
import java.util.*


class ListNotaAdapter(
    private val transaksi: ArrayList<TransaksiHeader>,
    private val mContext: Context
) :

    RecyclerView.Adapter<ListNotaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_nota, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksiHeader = transaksi[position]
        holder.textNominal.text = StringControl().getNumberFormat(StringControl().roundedNumber(transaksiHeader.NOMINAL.toString()))
        holder.textDate.text = StringControl().getDate(transaksiHeader.TGL_TRANSAKSI)
        holder.textDetail.text = "${transaksiHeader.QTY} items"
        holder.layoutNota.setOnClickListener {
            val intent = Intent(mContext,DetailNotaActivity::class.java)
            intent.putExtra("idTransaksi",transaksiHeader.ID_TRANSAKSI)
            mContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return transaksi.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layoutNota: ConstraintLayout = view.findViewById(R.id.layout_nota) as ConstraintLayout
        var textNominal: TextView = view.findViewById(R.id.text_nota_nominal) as TextView
        var textDetail: TextView = view.findViewById(R.id.text_nota_detail) as TextView
        var textDate: TextView= view.findViewById(R.id.text_nota_date) as TextView

    }

}
