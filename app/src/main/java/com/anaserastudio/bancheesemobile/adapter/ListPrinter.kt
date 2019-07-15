package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.anaserastudio.bancheesemobile.R
import com.anaserastudio.bancheesemobile.model.Printer

class ListPrinter(
    context: Context,
    var namaPrinter: ArrayList<String>,
    var idPrinter: ArrayList<String>,
    var nomorPrinter: ArrayList<Int>
) : ArrayAdapter<String>(context,R.layout.list_printer, namaPrinter) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.list_printer, parent, false)
        val stringA = row.findViewById(R.id.nomor) as TextView
        val stringB = row.findViewById(R.id.namaPrinter) as TextView
        val stringC = row.findViewById(R.id.idPrinter) as TextView
        val stringD = row.findViewById(R.id.keterangan) as TextView
        stringA.text = nomorPrinter[position].toString()
        stringB.text = namaPrinter[position]
        stringC.text = idPrinter[position]
        stringD.text = ""
        return row
    }
}