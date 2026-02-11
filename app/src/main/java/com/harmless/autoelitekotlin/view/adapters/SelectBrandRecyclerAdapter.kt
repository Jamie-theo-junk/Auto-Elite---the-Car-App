package com.harmless.autoelitekotlin.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.CarModel
import com.harmless.autoelitekotlin.viewModel.MakeAndModelViewModel

class SelectBrandRecyclerAdapter(
    private val brands: List<CarBrand>,
    private val viewModel: MakeAndModelViewModel
) : RecyclerView.Adapter<SelectBrandRecyclerAdapter.BrandViewHolder>() {

    private  val TAG = "SelectBrandRecyclerAdap"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.make_card, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brandItem = brands[position]

        holder.carBrand.text = brandItem.brand

        holder.checkbox.setOnCheckedChangeListener(null)

        // Set checkbox only if it's the first time this item is bound
        if (!brandItem.isCheckedInitialized) {
            holder.checkbox.isChecked = viewModel.isBrandSelected(brandItem.brand)
            viewModel.toggleBrandSelection(brandItem, holder.checkbox.isChecked )
            brandItem.isChecked = viewModel.isBrandSelected(brandItem.brand)
            brandItem.isCheckedInitialized = true
        } else {
            holder.checkbox.isChecked = brandItem.isChecked
        }

        // Expand/collapse layout
        holder.expandableLayout.visibility = if (brandItem.isExpandable) View.VISIBLE else View.GONE
        holder.arrow.rotation = if (brandItem.isExpandable) 180f else 0f

        // Nested RecyclerView
        holder.nestedRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SelectedModelNestedRecyclerAdapter(brandItem,viewModel,this@SelectBrandRecyclerAdapter, position)
        }

        // Expand/collapse click
        val toggleExpand = {
            brandItem.isExpandable = !brandItem.isExpandable
            notifyItemChanged(position)
        }
        holder.arrow.setOnClickListener { toggleExpand() }
        holder.contentLayout.setOnClickListener { toggleExpand() }

        // Checkbox change listener
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            brandItem.isChecked = isChecked
            brandItem.isExpandable = isChecked
            viewModel.toggleBrandSelection(brandItem, isChecked)


            notifyItemChanged(position)
            Log.d(TAG, "Checkbox changed: ${brandItem.brand}, isChecked=$isChecked")
        }
    }

    override fun getItemCount(): Int = brands.size

    inner class BrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentLayout: RelativeLayout = itemView.findViewById(R.id.ListItem)
        val expandableLayout: RelativeLayout = itemView.findViewById(R.id.NestListView)
        val arrow: ImageView = itemView.findViewById(R.id.ChevronDown)
        val carBrand: TextView = itemView.findViewById(R.id.CarBrandTxt)
        val nestedRecycler: RecyclerView = itemView.findViewById(R.id.CarBrandNestedRecylcer)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkVarient)
    }
}

