package com.harmless.autoelitekotlin.view.activities.FilterActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityProvinceSelectionBinding
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.adapters.ProvinceSelectionRecyclerAdapter
import com.harmless.autoelitekotlin.view.adapters.YearSelectionRecyclerAdapter
import com.harmless.autoelitekotlin.viewModel.YearSelectionViewModel

class ProvinceSelection : AppCompatActivity() {
    private lateinit var binding: ActivityProvinceSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProvinceSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        val provinceRecycler = binding.provinceRecyclerView


        provinceRecycler.setHasFixedSize(true)
        provinceRecycler.layoutManager  = LinearLayoutManager(applicationContext)

                val constant = Constants()
                val adapterProvince= ProvinceSelectionRecyclerAdapter(constant.provinces)
        provinceRecycler.adapter = adapterProvince

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