package com.app.dairy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    var dairyItems: List<DairyItem>? = null
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.rv_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        dairyItems?.let {
            it[position].let { item ->
                holder.cowNo.text = "Cow No: ${item.cowNo}"
                holder.gender.text = item.gender
                holder.peakYield.text = "Peak Yield: ${item.peakYield}"
                holder.calvingDate.text = "Calving Date: ${item.calvingDate}"

                holder.itemView.setOnClickListener {
                    listener?.onClicked(item)
                }
            }
        }
    }

    fun updateList(items: List<DairyItem>) {
        dairyItems = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dairyItems?.size ?: 0
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gender: TextView = itemView.findViewById(R.id.gender)
        val cowNo: TextView = itemView.findViewById(R.id.cowNo)
        val peakYield: TextView = itemView.findViewById(R.id.peakYield)
        val calvingDate: TextView = itemView.findViewById(R.id.calvingDate)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        listener = clickListener
    }

    interface OnItemClickListener {
        fun onClicked(dairyItem: DairyItem)
    }
}