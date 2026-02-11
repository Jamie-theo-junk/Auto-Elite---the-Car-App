package com.harmless.autoelitekotlin.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.viewModel.MakeAndModelViewModel



class SelectedModelNestedRecyclerAdapter(
    private val brand: CarBrand,
    private val viewModel: MakeAndModelViewModel,
    private val parentAdapter: SelectBrandRecyclerAdapter,
    private val brandPosition: Int,
) : RecyclerView.Adapter<SelectedModelNestedRecyclerAdapter.ModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.model_items_car, parent, false)
        return ModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val model = brand.models[position]
        holder.modelName.text = model.name
        holder.checkbox.setOnCheckedChangeListener(null)
        // Set checkbox only if it's the first time this item is bound
        if (!model.isCheckedInitialized) {
            val checked = viewModel.isModelSelected(brand.brand, model.name)

            model.isChecked = checked
            holder.checkbox.isChecked = checked
            viewModel.toggleModelSelection(brand, model, holder.checkbox.isChecked)
            model.isCheckedInitialized = true
        } else {
            holder.checkbox.isChecked = model.isChecked
        }

        holder.variantRecycler.visibility = if (model.isExpandable) View.VISIBLE else View.GONE
        holder.variantsText.visibility = if(model.isExpandable) View.VISIBLE else View.GONE
        holder.variantsText.visibility = if(holder.checkbox.isChecked) View.VISIBLE else View.GONE

        holder.variantRecycler.apply { layoutManager = LinearLayoutManager(context)
            adapter = SelectedVariantNestedRecyclerAdapter(
                brand = brand,
                model = model,
                viewModel = viewModel
            ) }
        holder.variantsText.setOnClickListener {
            model.isExpandable = !model.isExpandable
            notifyItemChanged(position)
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            model.isChecked = isChecked
            viewModel.toggleModelSelection(brand, model, isChecked)
            if(!isChecked && model.isExpandable){
                model.isExpandable= !model.isExpandable
            }
            if (isChecked && !brand.isChecked) {
                brand.isChecked = true
                viewModel.toggleBrandSelection(brand, true)

                // Updates parent item (brand)
                parentAdapter.notifyItemChanged(brandPosition)
            }
            notifyItemChanged(position)
        }}


    override fun getItemCount(): Int = brand.models.size

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modelName: TextView = itemView.findViewById(R.id.nameTxt)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val variantsText: TextView = itemView.findViewById(R.id.variantsTxt)
        val expandableLayout: ConstraintLayout = itemView.findViewById(R.id.expandableLayout)
        val variantRecycler: RecyclerView = itemView.findViewById(R.id.variants_recycler)
    }
}

