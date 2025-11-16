package com.harmless.autoelitekotlin.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityShowMoreBinding
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.activities.FilterActivities.ColorSelection
import com.harmless.autoelitekotlin.view.activities.FilterActivities.MakeAndModel
import com.harmless.autoelitekotlin.view.activities.FilterActivities.PriceSelection
import com.harmless.autoelitekotlin.view.activities.FilterActivities.ProvinceSelection
import com.harmless.autoelitekotlin.view.activities.FilterActivities.YearSelection
import com.harmless.autoelitekotlin.view.adapters.SpinnerAdapter
import com.harmless.autoelitekotlin.viewModel.CarViewModel

class ShowMore : AppCompatActivity() {


private lateinit var binding: ActivityShowMoreBinding
    private val TAG = "ShowMore"
private var viewModel = CarViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShowMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun init(){
        //init the spinner values
        val carTypeSpinner = findViewById<Spinner>(R.id.TypeBtn)
        val newOrUsedSpinner = findViewById<Spinner>(R.id.NewOrUsedBtn)
        val mileageSpinner = findViewById<Spinner>(R.id.MileageBtn)
        val fuelTypeSpinner = findViewById<Spinner>(R.id.FuelTypeBtn)
        val maxPriceSpinner = binding.maxPriceSpinner
        val minPriceSpinner = binding.minPriceSpinner

        //init the buttons
        val apply = findViewById<Button>(R.id.applyBtn)
        val back = findViewById<Button>(R.id.backBtn)

        //init the relative values
        val carBrandRel = findViewById<RelativeLayout>(R.id.carBrandBtn)
        val yearRel = findViewById<RelativeLayout>(R.id.YearBtn)
        val provinceRel = findViewById<RelativeLayout>(R.id.LocationBtn)
        val colourRel = findViewById<RelativeLayout>(R.id.ColourBtn)

        val constant = Constants()

        //for fuel spinner
        val fuel = SelectedValues.selectedFuelType
        setupSpinner(fuelTypeSpinner, constant.fuelType, fuel) { selected ->
            SelectedValues.selectedFuelType = selected

        }
        //for mileage spinner
        val mileage = SelectedValues.selectedMileage
        setupSpinner(mileageSpinner, constant.mileage, mileage) { selected ->
            SelectedValues.selectedMileage = selected
            if (selected.contains("more", true) || selected.contains("400")) {
                SelectedValues.selectedMinMileage = parseMileageStringToInt(selected) ?: 0
                SelectedValues.selectedMaxMileage = 10_000_000
            } else {
                SelectedValues.selectedMinMileage = viewModel.minMileage(selected)
                SelectedValues.selectedMaxMileage = viewModel.maxMileage(selected)
            }
        }
        //for New Used spinner

        val newUsed = SelectedValues.isNewOrUsed
        setupSpinner(newOrUsedSpinner, constant.newOrUsed, newUsed) { selected ->
            SelectedValues.isNewOrUsed = selected

        }

        val type = SelectedValues.selectedDriveTrain
        setupSpinner(carTypeSpinner, constant.driveTrain, type) { selected ->
            SelectedValues.selectedDriveTrain = selected

        }

        val minPrice = SelectedValues.selectedMinPrice
        setupSpinner(minPriceSpinner, constant.minPrices, minPrice) { selectedStr ->
            SelectedValues.selectedMinPrice = selectedStr
            val minInt = parsePriceStringToInt(selectedStr)
            val maxInt = parsePriceStringToInt(SelectedValues.selectedMaxPrice)
            if (minInt != null && maxInt != null && minInt > maxInt) {
                SelectedValues.selectedMaxPrice = selectedStr
                val idx = constant.maxPrices.indexOf(selectedStr)
                if (idx >= 0) maxPriceSpinner.setSelection(idx)
                Toast.makeText(this, "Max price adjusted to match selected min price.", Toast.LENGTH_SHORT).show()
            }
        }

        val maxPrice = SelectedValues.selectedMaxPrice
        // --- Max price spinner ---
        setupSpinner(maxPriceSpinner, constant.maxPrices, maxPrice) { selectedStr ->
            SelectedValues.selectedMaxPrice = selectedStr
            val maxInt = parsePriceStringToInt(selectedStr)
            val minInt = parsePriceStringToInt(SelectedValues.selectedMinPrice)
            if (minInt != null && maxInt != null && maxInt < minInt) {
                SelectedValues.selectedMinPrice = selectedStr
                val idx = constant.minPrices.indexOf(selectedStr)
                if (idx >= 0) minPriceSpinner.setSelection(idx)
                Toast.makeText(this, "Min price adjusted to not exceed max price.", Toast.LENGTH_SHORT).show()
            }
        }



        carBrandRel.setOnClickListener{
            val toCarBrand = Intent(applicationContext,  MakeAndModel::class.java)
            startActivity(toCarBrand)
        }
        yearRel.setOnClickListener{
            val toYear = Intent(applicationContext, YearSelection::class.java)
            startActivity(toYear)
        }
        provinceRel.setOnClickListener{
            val toProvince = Intent(applicationContext, ProvinceSelection::class.java)
            startActivity(toProvince)
        }
        colourRel.setOnClickListener{
            val toColor = Intent(applicationContext, ColorSelection::class.java)
            startActivity(toColor)
        }

        apply.setOnClickListener{
            finish()
        }
        back.setOnClickListener{
            finish()
        }

    }
    private fun setupSpinner(spinner: Spinner, items: List<String>, selectedItem: String?, onSelect: (String) -> Unit) {
        val adapter = SpinnerAdapter(this, items)
        spinner.adapter = adapter
        selectedItem?.let {
            val idx = items.indexOf(it)
            if (idx >= 0) spinner.setSelection(idx)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onSelect(items[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun parsePriceStringToInt(priceStr: String?): Int? {
        return priceStr?.replace(Regex("[^0-9]"), "")?.takeIf { it.isNotEmpty() }?.toInt()
    }

    private fun parseMileageStringToInt(mileageStr: String?): Int? {
        return mileageStr?.replace(Regex("[^0-9]"), "")?.takeIf { it.isNotEmpty() }?.toInt()
    }
}