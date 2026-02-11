package com.harmless.autoelitekotlin.view.activities.FilterActivities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityColorSelectionBinding
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.adapters.ColourSelectionRecyclerAdapter
import com.harmless.autoelitekotlin.view.adapters.ProvinceSelectionRecyclerAdapter

class ColorSelection : AppCompatActivity() {
    private lateinit var binding: ActivityColorSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityColorSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }
    private fun init() {
        val colorRecycler = binding.colorRecyclerView


        colorRecycler.setHasFixedSize(true)
        colorRecycler.layoutManager  = LinearLayoutManager(applicationContext)

        val constant = Constants()
        val adapterColor= ColourSelectionRecyclerAdapter(constant.color)
        colorRecycler.adapter = adapterColor

        val backBtn = binding.backBtn
        backBtn.setOnClickListener {
            finish()
        }
        val applyBtn = binding.applyBtn
        applyBtn.setOnClickListener {
            finish()
        }

    }
    }