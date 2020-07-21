package com.scb.loanforms

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

class CheckBoxRvAdapter(
    cxt: Context,
    dataList: ArrayList<String>
) : RecyclerView.Adapter<CheckBoxRvAdapter.CheckBoxViewHolder>() {
    private var selectedPostion: Int = -1
    private var ctxt: Context? = null
    private var dataList: ArrayList<String>

    init {
        this.ctxt = cxt
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckBoxViewHolder {
        val view = LayoutInflater.from(ctxt).inflate(R.layout.item_checkbox, parent, false)
        return CheckBoxViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CheckBoxViewHolder, position: Int) {
        holder.checkBox.text = dataList[position]
        holder.checkBox.isChecked = selectedPostion == position

        holder.itemView.setOnClickListener {
            selectedPostion = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    public fun getSelectedValue(): String {
        return dataList[selectedPostion]
    }

    class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }
}