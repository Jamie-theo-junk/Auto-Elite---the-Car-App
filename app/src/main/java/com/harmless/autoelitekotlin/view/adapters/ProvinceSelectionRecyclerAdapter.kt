package com.harmless.autoelitekotlin.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R

class ProvinceSelectionRecyclerAdapter(private val items: List<String>):
    RecyclerView.Adapter<ProvinceSelectionRecyclerAdapter.ItemViewHolder>(){

    companion object{
        private val TAG = "ProvinceSelectionRecyclerAdapter"
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_items_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val province = items[position]

        holder.checkbox.setOnCheckedChangeListener(null)

        holder.checkbox.isChecked = SelectedValues.selectedProvince.contains(province)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val list = SelectedValues.selectedProvince

            if (isChecked) {
                if (!list.contains(province)) {
                    list.add(province)
                    SelectedValues.selectedProvince = list
                    Log.d("TAG", "Added year: ${SelectedValues.selectedProvince}")
                }
            } else {
                list.remove(province)
                SelectedValues.selectedProvince = list
                Log.d("TAG", "Removed year: ${SelectedValues.selectedProvince}")
            }
        }

        holder.provinceName.text = province


        }



    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val provinceName = itemView.findViewById<TextView>(R.id.nameTxt)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)

    }
}