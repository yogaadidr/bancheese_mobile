package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.model.DetailBahanBaku
import com.anaserastudio.bancheesemobile.tool.StringControl
import kotlin.collections.ArrayList
import androidx.appcompat.widget.PopupMenu
import com.anaserastudio.bancheesemobile.DetailBahanBakuActivity
import com.anaserastudio.bancheesemobile.TambahStok

class ListDetailBahanBaku(
    private val detail: ArrayList<DetailBahanBaku>,
    private val mContext: Context,
    private val detailBahanBakuActivity: DetailBahanBakuActivity
) :

    RecyclerView.Adapter<ListDetailBahanBaku.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_detail_bahan_baku, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val det = detail[position]
        holder.text_nama.text = det.nama_bahan
        holder.text_harga.text = StringControl().getNumberFormat(det.harga.toString().replace(".00","").toInt())
        var saldo = ""
        if(det.debet > 0){
            saldo = "+${det.debet}"
        }
        if(det.kredit > 0){
            saldo = "-${det.kredit}"
        }
        holder.text_saldo.text = saldo

        holder.layout.setOnClickListener { vw ->
            val popup = PopupMenu(mContext, vw)
            popup.menuInflater.inflate(R.menu.menu_edit_delete, popup.menu)
            popup.setOnMenuItemClickListener{
                when (it.itemId) {
                    R.id.menu_update -> {
//                archive(item)
                        val intent = Intent(mContext, TambahStok::class.java)
                        intent.putExtra("menu","ubah")
                        intent.putExtra("harga",detail[position].harga.toString().replace(".00",""))
                        intent.putExtra("nama",detail[position].nama_bahan)
                        intent.putExtra("satuan","Pcs")
                        intent.putExtra("id_bahan",detail[position].id_bahan)
                        if(detail[position].id_debet != 0){
                            intent.putExtra("jenis","debet")
                            intent.putExtra("id",detail[position].id_debet)
                            intent.putExtra("qty",detail[position].debet)
                        }else if(detail[position].id_kredit != 0){
                            intent.putExtra("jenis","kredit")
                            intent.putExtra("id",detail[position].id_kredit)
                            intent.putExtra("qty",detail[position].kredit)
                        }
                        mContext.startActivity(intent)
                        true
                    }
                    R.id.menu_delete-> {
                        AlertDialog.Builder(mContext)
                            .setTitle("Hapus")
                            .setMessage("Apakah anda yakin untuk menghapus data ini?")
                            .setPositiveButton(android.R.string.yes,
                                DialogInterface.OnClickListener { dialog, whichButton ->
                                    if(detail[position].id_debet != 0){
                                        detailBahanBakuActivity.delete(detail[position].id_debet.toString(), "debet")
                                    }else if(detail[position].id_kredit != 0){
                                        detailBahanBakuActivity.delete(detail[position].id_kredit.toString(), "kredit")
                                    }
                                })
                            .setNegativeButton(android.R.string.no, null).show()
                        true
                    }
                    else ->  false
                }
            }
            popup.show()
        }
    }



    override fun getItemCount(): Int {
        return detail.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: ConstraintLayout = view.findViewById(R.id.layout_detail_bahan) as ConstraintLayout
        var text_nama: TextView = view.findViewById(R.id.text_detail_bahan_nama) as TextView
        var text_harga: TextView = view.findViewById(R.id.text_detail_bahan_harga) as TextView
        var text_saldo: TextView = view.findViewById(R.id.text_detail_bahan_saldo) as TextView

    }

}
