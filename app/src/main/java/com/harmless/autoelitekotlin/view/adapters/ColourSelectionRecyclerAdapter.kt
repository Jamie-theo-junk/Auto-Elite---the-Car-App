package com.harmless.autoelitekotlin.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R


class ColourSelectionRecyclerAdapter(private val items: List<String>):
    RecyclerView.Adapter<ColourSelectionRecyclerAdapter.ItemViewHolder>(){

    companion object{
        private val TAG = "ColourSelectionRecyclerAdapter"
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_items_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val color = items[position]

        holder.checkbox.setOnCheckedChangeListener(null)

        holder.checkbox.isChecked = SelectedValues.selectedColor.contains(color)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            val list = SelectedValues.selectedColor

            if (isChecked) {
                if (!list.contains(color)) {
                    list.add(color)
                    SelectedValues.selectedColor = list
                    Log.d("TAG", "Added year: ${SelectedValues.selectedColor}")
                }
            } else {
                list.remove(color)
                SelectedValues.selectedColor = list
                Log.d("TAG", "Removed year: ${SelectedValues.selectedProvince}")
            }
        }

        holder.colorName.text = color

    }


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorName = itemView.findViewById<TextView>(R.id.nameTxt)
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)

    }
}