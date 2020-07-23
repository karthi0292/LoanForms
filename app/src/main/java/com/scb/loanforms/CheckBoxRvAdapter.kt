package com.scb.loanforms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class CheckBoxRvAdapter(
    cxt: Context,
    dataList: ArrayList<String>,
    checkBoxClickListener: Utils.CheckBoxClickListener
) : RecyclerView.Adapter<CheckBoxRvAdapter.CheckBoxViewHolder>() {
    private var selectedPosition: Int = -1
    private var ctxt: Context? = null
    private var dataList: ArrayList<String>
    private val checkBoxListener:Utils.CheckBoxClickListener

    init {
        this.ctxt = cxt
        this.dataList = dataList
        this.checkBoxListener=checkBoxClickListener
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
        holder.checkBox.isChecked = selectedPosition == position

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            checkBoxListener.setSelectedItem(selectedPosition,dataList[position])
            notifyDataSetChanged()
        }
    }

    fun setSelectedItem(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()
    }

    class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    }
}