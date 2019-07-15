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
import com.anaserastudio.bancheesemobile.model.Menu
import com.anaserastudio.bancheesemobile.tool.StringControl


class ListOrder(
    private val transaksi: ArrayList<Menu>,
    private val mContext: Context,
    private val inflater: LayoutInflater,
    private val transaksiActivity: TransaksiActivity?
) :

    RecyclerView.Adapter<ListOrder.ViewHolder>() {

    lateinit var dialog : Dialog
    var dialogView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_order, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListOrder.ViewHolder, position: Int) {
        val sc = StringControl()

        val transaksi = transaksi
        val menu = transaksi[position]
        val value = menu.transaksi

        holder.namaMenu.text = menu.NAMA_MENU
        val harga = sc.getNumberFormat(StringControl().roundedNumber(menu.HARGA.toString()))
        holder.detail.text = "${value.QTY} x ${harga}"
        val subtotal = value.QTY * StringControl().roundedNumber(menu.HARGA.toString())
        holder.subTotal.text = sc.getNumberFormat(subtotal)

        if(transaksiActivity!=null){
            holder.container.setOnClickListener {
                showDialog(position)
            }
        }
    }

    private fun showDialog(position: Int) {
        dialog = Dialog(mContext,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        dialogView = inflater.inflate(R.layout.dialog_edit_order, null)
        dialog.setContentView(dialogView!!)
        dialog.setCancelable(true)
        dialog.setTitle("Ubah pesanan")


        val btnSimpan = dialogView!!.findViewById<Button>(R.id.btn_simpan)
        val btnPlus = dialogView!!.findViewById<Button>(R.id.btn_plus)
        val btnMinus = dialogView!!.findViewById<Button>(R.id.btn_minus)
        val btnHapus = dialogView!!.findViewById<Button>(R.id.btn_hapus)
        val textMenu = dialogView!!.findViewById<TextView>(R.id.text_nama)
        val textQty = dialogView!!.findViewById<TextView>(R.id.text_qty)
        val textHarga = dialogView!!.findViewById<TextView>(R.id.text_harga)

        val menu = transaksi[position]
        val trans = menu.transaksi

        textHarga.text = StringControl().getNumberFormat(StringControl().roundedNumber(menu.HARGA.toString()))
        textQty.text = trans.QTY.toString()
        textMenu.text = menu.NAMA_MENU

        btnPlus.setOnClickListener {
            trans.QTY++
            textQty.text = trans.QTY.toString()
        }
        btnMinus.setOnClickListener {
            if(trans.QTY > 0){
                trans.QTY--
                textQty.text = trans.QTY.toString()
            }

        }
        btnHapus.setOnClickListener {
            transaksi.removeAt(position)
            transaksiActivity!!.countTotal(transaksi)
            this.notifyDataSetChanged()
            dialog.dismiss()
        }

        btnSimpan.setOnClickListener {
            if(trans.QTY == 0){
                transaksi.removeAt(position)
            }else{
                trans.HARGA = trans.QTY * StringControl().roundedNumber(menu.HARGA.toString())
                menu.transaksi = trans
                transaksi[position] = menu
            }

            transaksiActivity!!.countTotal(transaksi)

            this.notifyDataSetChanged()

            dialog.dismiss()
        }

        dialog.show()
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
