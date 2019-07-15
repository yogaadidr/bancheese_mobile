package com.anaserastudio.bancheesemobile.adapter

import android.app.Dialog
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.TransaksiActivity
import com.anaserastudio.bancheesemobile.model.Transaksi
import com.anaserastudio.bancheesemobile.tool.StringControl


class ListOrderDetail(
    private val transaksi: ArrayList<Transaksi>,
    private val mContext: Context,
    private val inflater: LayoutInflater,
    private val transaksiActivity: TransaksiActivity?
) :

    RecyclerView.Adapter<ListOrderDetail.ViewHolder>() {

    lateinit var dialog : Dialog
    var dialogView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_order, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sc = StringControl()

        val transaksi = transaksi
        val value = transaksi[position]

        holder.namaMenu.text = value.NAMA_MENU
        val harga = sc.getNumberFormat(StringControl().roundedNumber(value.HARGA.toString()))
        holder.detail.text = "${value.QTY} x ${harga}"
        val subtotal = value.QTY * StringControl().roundedNumber(value.HARGA.toString())
        holder.subTotal.text = sc.getNumberFormat(subtotal)

    }

    override fun getItemCount(): Int {
        return transaksi.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var namaMenu: TextView = view.findViewById(R.id.order_nama) as TextView
        var subTotal: TextView = view.findViewById(R.id.order_subtotal) as TextView
        var detail: TextView = view.findViewById(R.id.order_detail) as TextView
        var container: ConstraintLayout = view.findViewById(R.id.container) as ConstraintLayout

    }
}
