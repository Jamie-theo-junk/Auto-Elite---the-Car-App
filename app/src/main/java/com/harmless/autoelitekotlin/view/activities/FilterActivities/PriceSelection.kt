package com.harmless.autoelitekotlin.view.activities.FilterActivities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityPriceSelectionBinding

import com.harmless.autoelitekotlin.viewModel.PriceSelectionViewModel

class PriceSelection : AppCompatActivity() {

    private lateinit var binding: ActivityPriceSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPriceSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        init()
    }

//    private fun init() {
//        val priceRecycler = binding.PriceRecyclerView
//
//        val priceList = listOf(
//            "0km",
//            "10000km",
//            "50000km",
//            "100000km",
//            "150000km",
//            "200000km",
//            "250000km",
//            "300000km",
//            "350000km",
//            "400000km",
//            "more than 450000km"
//        )
//                priceRecycler.setHasFixedSize(true)
//                priceRecycler.layoutManager  = LinearLayoutManager(applicationContext)
//                val adapterPrice = PriceSelectionRecyclerAdapter(priceList)
//                priceRecycler.adapter = adapterPrice
//
//    }
}