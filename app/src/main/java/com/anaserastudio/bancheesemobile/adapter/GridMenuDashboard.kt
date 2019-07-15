package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.anaserastudio.bancheesemobile.*
import com.anaserastudio.bancheesemobile.model.MenuDashboard

class GridMenuDashboard(private val mContext: Context, private val menus: ArrayList<MenuDashboard>) : BaseAdapter() {

    override fun getCount(): Int {
        return menus.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val cv = layoutInflater.inflate(R.layout.grid_menu_dashboard, null)

        val image = cv.findViewById(R.id.image_menu_dashboard) as ImageView
        val title = cv.findViewById(R.id.text_menu_dashboard_nama) as TextView
        val layout = cv.findViewById(R.id.layout_menu_dashboard) as ConstraintLayout

        title.text = menus[position].namaMenu
        image.setImageDrawable(menus[position].gambarMenu)

        layout.setOnClickListener {
            if(menus[position].namaMenu == "Transaksi"){
                val intent = Intent(mContext,TransaksiActivity::class.java)
                mContext.startActivity(intent)
            }else if (menus[position].namaMenu == "Nota"){
                val intent = Intent(mContext, DaftarTransaksiActivity::class.java)
                mContext.startActivity(intent)
            }else if (menus[position].namaMenu == "Bahan Baku"){
                val intent = Intent(mContext, DaftarBahanBaku::class.java)
                mContext.startActivity(intent)
            }else if (menus[position].namaMenu == "Printer"){
                val intent = Intent(mContext, PilihPrinterActivity::class.java)
                mContext.startActivity(intent)
            }else if (menus[position].namaMenu == "Registrasi Device"){
                val intent = Intent(mContext, RegistrasiDevice::class.java)
                mContext.startActivity(intent)
            }
        }

        return cv
    }

}
