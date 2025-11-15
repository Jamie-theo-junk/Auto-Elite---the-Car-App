package com.harmless.autoelitekotlin.view.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.CarModel
import com.harmless.autoelitekotlin.viewModel.MakeAndModelViewModel

class SelectedVariantNestedRecyclerAdapter(
    private val brand: CarBrand,
    private val model: CarModel,
    private val viewModel: MakeAndModelViewModel
) : RecyclerView.Adapter<SelectedVariantNestedRecyclerAdapter.VariantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.variants_items_card, parent, false)
        return VariantViewHolder(view)
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        val variantName = model.variants[position]

        holder.variantTxt.text = variantName.name
        holder.variantCheckBox.setOnCheckedChangeListener(null)
        if (!variantName.isCheckedInitialized) {
            holder.variantCheckBox.isChecked = viewModel.isVariantSelected(brand.brand, model.name, variantName.name)
            variantName.isCheckedInitialized = true
        } else {
            holder.variantCheckBox.isChecked = variantName.isChecked
        }

        holder.variantCheckBox.setOnCheckedChangeListener { _, isChecked ->
            variantName.isChecked = isChecked
            viewModel.toggleVariantSelection(brand, model, variantName.name, isChecked)
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int = model.variants.size

    inner class VariantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val variantCheckBox: CheckBox = itemView.findViewById(R.id.variantCheckbox)
        val variantTxt: TextView = itemView.findViewById(R.id.variantTxt)
    }
}
