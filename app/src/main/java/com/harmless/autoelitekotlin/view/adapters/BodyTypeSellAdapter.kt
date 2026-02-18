package com.harmless.autoelitekotlin.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.CarBrand

class BodyTypeSellAdapter(
    private val bodyTypes: List<String>,
    private val onBodyTypeSelected: (String?) -> Unit
) : RecyclerView.Adapter<BodyTypeSellAdapter.ViewHolder>() {

    private var selectedPosition = -1 // Track the currently selected item

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bodyTypeName: TextView = itemView.findViewById(R.id.bodyTypeName)
        val cardView: CardView = itemView.findViewById(R.id.bodyTypeBackground)
        val carImage: ImageView = itemView.findViewById(R.id.bodyType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.body_type_card, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bodyType = bodyTypes[position]
        holder.bodyTypeName.text = bodyType
        populateImage(bodyType, holder )
        // Change card color depending on whether it's selected
        val context = holder.itemView.context
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.black))
        }

        // When card is clicked
        holder.cardView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position

            // Notify UI to refresh only the changed items
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)

            // Pass selected body type back to the listener
            onBodyTypeSelected(bodyType)
        }
    }

    override fun getItemCount() = bodyTypes.size

    private fun populateImage(category: String, holder: ViewHolder ){
        when(category){
            "Sedan"-> holder.carImage.setImageResource(R.drawable.sedan_car)
            "Hatchback"-> holder.carImage.setImageResource(R.drawable.hatchback_car)
            "SUV"-> holder.carImage.setImageResource(R.drawable.suv_car)
            "Coupe"-> holder.carImage.setImageResource(R.drawable.coupe_car)
            "Convertible"-> holder.carImage.setImageResource(R.drawable.convertable_car)
            "Minivan"-> holder.carImage.setImageResource(R.drawable.van_car)
            "Bakkie"-> holder.carImage.setImageResource(R.drawable.baakie_car)
            "Station Wagon"-> holder.carImage.setImageResource(R.drawable.station_car)
            "Crossover"-> holder.carImage.setImageResource(R.drawable.sedan_car)
        }
    }
}