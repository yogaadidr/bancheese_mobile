package com.anaserastudio.bancheesemobile.adapter

import android.content.Context
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anaserastudio.bancheesemobile.R
import kotlin.collections.ArrayList


class ListTextArrow(
    private val text: ArrayList<String>,
    private val target: ArrayList<Class<*>?>,
    private val mContext: Context
) :

    RecyclerView.Adapter<ListTextArrow.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_text_arrow, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListTextArrow.ViewHolder, position: Int) {
        holder.text.text = text[position]
        holder.layout.setOnClickListener {
            val intent = Intent(mContext, target[position])
            mContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return text.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: ConstraintLayout = view.findViewById(R.id.layout) as ConstraintLayout
        var text: TextView = view.findViewById(R.id.text_list) as TextView

    }

}
