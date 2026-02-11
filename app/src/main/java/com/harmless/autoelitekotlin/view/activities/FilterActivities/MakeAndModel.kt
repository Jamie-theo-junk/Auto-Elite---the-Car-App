package com.harmless.autoelitekotlin.view.activities.FilterActivities

import SelectedValues.carBrandsSelected
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.view.adapters.SelectBrandRecyclerAdapter
import com.harmless.autoelitekotlin.viewModel.CarBrandCallback
import com.harmless.autoelitekotlin.viewModel.MakeAndModelViewModel

class MakeAndModel : AppCompatActivity() {

    private lateinit var viewModel: MakeAndModelViewModel
    private val TAG = "MakeAndModel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_and_model)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()

    }

    private fun init(){
        val backBtn = findViewById<AppCompatButton>(R.id.backBtn)
        val applyBtn = findViewById<AppCompatButton>(R.id.applyBtn)
        val selectedCarRecycler = findViewById<RecyclerView>(R.id.Car_selection_recyclerview)
        selectedCarRecycler.setHasFixedSize(true)
        selectedCarRecycler.layoutManager = LinearLayoutManager(this)

        viewModel = MakeAndModelViewModel()

        viewModel.getCarBrand(object : CarBrandCallback {
            override fun onCarBrandLoaded(carBrands: List<CarBrand>) {


                val adapter = SelectBrandRecyclerAdapter(
                    carBrands,
                    viewModel
                    )


                selectedCarRecycler.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



        backBtn.setOnClickListener {
            finish()
        }
        applyBtn.setOnClickListener {
            viewModel.saveCar()

            for (brands in SelectedValues.carBrandsSelected){
                Log.d(TAG, "init: Brands $brands")
            }
            for (brands in SelectedValues.carModelsSelected){
                Log.d(TAG, "init:Models $brands")
            }
            for (brands in SelectedValues.carVariantsSelected){
            Log.d(TAG, "init:Variants $brands")
            }
            finish();
        }
    }
}