package com.harmless.autoelitekotlin.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R


class YearSelectionRecyclerAdapter(private val items: List<Int>):
    RecyclerView.Adapter<YearSelectionRecyclerAdapter.ItemViewHolder>(){

    private val TAG = "YearSelectionRecyclerAd"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_items_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val year = items[position]

        holder.checkbox.setOnCheckedChangeListener(null)

        holder.checkbox.isChecked = SelectedValues.selectedYear.contains(year)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val list = SelectedValues.selectedYear

            if (isChecked) {
                if (!list.contains(year)) {
                    list.add(year)
                    SelectedValues.selectedYear = list
                    Log.d("TAG", "Added year: ${SelectedValues.selectedYear}")
                }
            } else {
                list.remove(year)
                SelectedValues.selectedYear = list
                Log.d("TAG", "Removed year: ${SelectedValues.selectedYear}")
            }
        }

        holder.yearText.text = year.toString()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val yearText = itemView.findViewById<TextView>(R.id.nameTxt)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)

    }
}