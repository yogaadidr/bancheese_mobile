package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.DialogInterface
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anaserastudio.bancheesemobile.DaftarCabang
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.model.Cabang
import kotlin.collections.ArrayList


class ListCabang(
    private val cabang: ArrayList<Cabang>,
    private val mContext: Context,
    private val daftarCabang: DaftarCabang
) :

    RecyclerView.Adapter<ListCabang.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_cabang, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListCabang.ViewHolder, position: Int) {
        val lap = cabang[position]
        holder.text_nama.text = lap.NAMA_CABANG
        holder.text_detail.text = lap.ALAMAT
        holder.layout.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setTitle("Pilih Outlet")
                .setMessage("Pilih outlet ini?")
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        daftarCabang.selectCabang(lap.ID_CABANG)
                    })
                .setNegativeButton(android.R.string.no, null).show()
        }
    }


    override fun getItemCount(): Int {
        return cabang.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: ConstraintLayout = view.findViewById(R.id.layout_detail_cabang) as ConstraintLayout
        var text_nama: TextView = view.findViewById(R.id.text_detail_cabang_nama) as TextView
        var text_detail: TextView = view.findViewById(R.id.text_detail_cabang_alamat) as TextView

    }

}
