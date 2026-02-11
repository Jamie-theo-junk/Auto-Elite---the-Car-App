package com.harmless.autoelitekotlin.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.harmless.autoelitekotlin.*

class SpinnerAdapter(
    context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, R.layout.custom_spinner, items) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent, R.layout.custom_spinner)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent, R.layout.spinner_dropdown_item)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup, layoutId: Int): View {
        val view = convertView ?: inflater.inflate(layoutId, parent, false)
        val textView = view.findViewById<TextView>(
            if (layoutId == R.layout.custom_spinner) R.id.text1 else R.id.dropdown_item_text
        )
        textView.text = items[position]
        return view
    }
}
