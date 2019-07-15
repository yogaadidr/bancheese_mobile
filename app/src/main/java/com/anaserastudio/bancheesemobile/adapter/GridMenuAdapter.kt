package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.TransaksiActivity
import com.anaserastudio.bancheesemobile.model.Menu
import com.anaserastudio.bancheesemobile.tool.StringControl


class GridMenuAdapter(private val mContext: Context, private val menus: ArrayList<Menu>, private val transaksiActivity: TransaksiActivity) : BaseAdapter() {

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
        val cv = layoutInflater.inflate(R.layout.grid_menu, null)

        val content = cv.findViewById(R.id.grid_menu_content) as ConstraintLayout
        val title = cv.findViewById(R.id.grid_menu_title) as TextView
        val detail = cv.findViewById(R.id.grid_menu_detail) as TextView
        val menu = menus[position]
        title.text = menu.NAMA_MENU
        detail.text = StringControl().getNumberFormat(StringControl().roundedNumber(menu.HARGA.toString()))

        content.setOnClickListener {
            transaksiActivity.addTransaction(menu)
        }

        return cv
    }

}
