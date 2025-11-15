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
import com.harmless.autoelitekotlin.databinding.ActivityYearSelectionBinding
import com.harmless.autoelitekotlin.view.adapters.YearSelectionRecyclerAdapter
import com.harmless.autoelitekotlin.viewModel.YearSelectionViewModel

class YearSelection : AppCompatActivity() {
    private lateinit var binding: ActivityYearSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYearSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init() {
        val yearRecycler = binding.YearsRecyclerView

        val yearSelection = YearSelectionViewModel()

        yearSelection.loadYears(object : YearSelectionViewModel.YearsCallback {
            override fun onYearsLoaded(years: List<Int>) {
                yearRecycler.setHasFixedSize(true)
                yearRecycler.layoutManager  = LinearLayoutManager(applicationContext)
                val adapterYear = YearSelectionRecyclerAdapter(years)
                yearRecycler.adapter = adapterYear
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            }
        })

        val backBtn = binding.backBtn
        backBtn.setOnClickListener {
            finish()
        }
        val applyBtn = binding.applyBtn
        applyBtn.setOnClickListener {

        }
    }
}