package com.harmless.autoelitekotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DatabaseError
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.model.Car
import com.harmless.autoelitekotlin.view.activities.FilterActivities.MakeAndModel
import com.harmless.autoelitekotlin.view.activities.ShowMore
import com.harmless.autoelitekotlin.view.activities.VehicleListActivity
import com.harmless.autoelitekotlin.view.activities.FilterActivities.YearSelection
import com.harmless.autoelitekotlin.view.adapters.SpinnerAdapter
import com.harmless.autoelitekotlin.viewModel.CarViewModel
class CarFragment : Fragment(), CarViewModel.CarsCallback {

    companion object {
        private const val TAG = "CAR_FRAGMENT"
    }

    private val viewModel = CarViewModel()
    private lateinit var carBrandTxt: TextView

    private fun parsePriceStringToInt(priceStr: String?): Int? {
        return priceStr?.replace(Regex("[^0-9]"), "")?.takeIf { it.isNotEmpty() }?.toInt()
    }

    private fun parseMileageStringToInt(mileageStr: String?): Int? {
        return mileageStr?.replace(Regex("[^0-9]"), "")?.takeIf { it.isNotEmpty() }?.toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_car, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val carBrandBtn = view.findViewById<ConstraintLayout>(R.id.carBrandBtn)
        val mileageSpinner = view.findViewById<Spinner>(R.id.mileageBrandSpinner)
        val minPriceSpinner = view.findViewById<Spinner>(R.id.minPriceSpinner)
        val maxPriceSpinner = view.findViewById<Spinner>(R.id.maxPriceSpinner)
        val typeSpinner = view.findViewById<Spinner>(R.id.typeBrandSpinner)
        val yearRelative = view.findViewById<RelativeLayout>(R.id.yearBrandSpinner)
        val searchBtn = view.findViewById<Button>(R.id.SearchBtn)
        val showMoreTxt = view.findViewById<TextView>(R.id.showMoreId)
        carBrandTxt = view.findViewById(R.id.CarBrandTxt)

        val constants = Constants()

        // --- Navigation ---
        carBrandBtn.setOnClickListener { startActivity(Intent(view.context, MakeAndModel::class.java)) }
        yearRelative.setOnClickListener { startActivity(Intent(view.context, YearSelection::class.java)) }
        showMoreTxt.setOnClickListener { startActivity(Intent(view.context, ShowMore::class.java)) }

        // --- Drive train spinner ---
        setupSpinner(typeSpinner, constants.driveTrain, SelectedValues.selectedDriveTrain) { selected ->
            SelectedValues.selectedDriveTrain = selected
        }

        // --- Mileage spinner ---
        setupSpinner(mileageSpinner, constants.mileage, SelectedValues.selectedMileage) { selected ->
            SelectedValues.selectedMileage = selected
            if (selected.contains("more", true) || selected.contains("400")) {
                SelectedValues.selectedMinMileage = parseMileageStringToInt(selected) ?: 0
                SelectedValues.selectedMaxMileage = 10_000_000
            } else {
                SelectedValues.selectedMinMileage = viewModel.minMileage(selected)
                SelectedValues.selectedMaxMileage = viewModel.maxMileage(selected)
            }
        }

        // --- Min price spinner ---
        setupSpinner(minPriceSpinner, constants.minPrices, SelectedValues.selectedMinPrice) { selectedStr ->
            SelectedValues.selectedMinPrice = selectedStr
            val minInt = parsePriceStringToInt(selectedStr)
            val maxInt = parsePriceStringToInt(SelectedValues.selectedMaxPrice)
            if (minInt != null && maxInt != null && minInt > maxInt) {
                SelectedValues.selectedMaxPrice = selectedStr
                val idx = constants.maxPrices.indexOf(selectedStr)
                if (idx >= 0) maxPriceSpinner.setSelection(idx)
                Toast.makeText(view.context, "Max price adjusted to match selected min price.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Max price spinner ---
        setupSpinner(maxPriceSpinner, constants.maxPrices, SelectedValues.selectedMaxPrice) { selectedStr ->
            SelectedValues.selectedMaxPrice = selectedStr
            val maxInt = parsePriceStringToInt(selectedStr)
            val minInt = parsePriceStringToInt(SelectedValues.selectedMinPrice)
            if (minInt != null && maxInt != null && maxInt < minInt) {
                SelectedValues.selectedMinPrice = selectedStr
                val idx = constants.minPrices.indexOf(selectedStr)
                if (idx >= 0) minPriceSpinner.setSelection(idx)
                Toast.makeText(view.context, "Min price adjusted to not exceed max price.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Search button ---
        searchBtn.setOnClickListener {

            viewModel.setSelectedCars(
                SelectedValues.selectedYear,
                SelectedValues.selectedColor,
                SelectedValues.selectedDriveTrain,
                SelectedValues.selectedProvince,
                SelectedValues.selectedMinMileage,
                SelectedValues.selectedMaxMileage,
                parsePriceStringToInt(SelectedValues.selectedMinPrice),
                parsePriceStringToInt(SelectedValues.selectedMaxPrice),
                SelectedValues.selectedTransmission
            ) { cars ->

                if (cars.isEmpty()) {
                    Toast.makeText(requireContext(), "No cars found", Toast.LENGTH_SHORT).show()
                    return@setSelectedCars
                }


                val intent = Intent(requireContext(), VehicleListActivity::class.java)
                intent.putExtra("selectedCars", ArrayList(cars))
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

    }

    // --- Generic spinner setup helper ---
    private fun setupSpinner(spinner: Spinner, items: List<String>, selectedItem: String?, onSelect: (String) -> Unit) {
        val adapter = SpinnerAdapter(requireContext(), items)
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


    override fun onDataLoaded(cars: List<Car>) {

    }

    override fun onCancelled(error: DatabaseError) {
        Toast.makeText(requireContext(), "Error loading cars: ${error.message}", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
//        val displayCars = if (SelectedValues.carBrandsSelected.isNotEmpty()) {
//            SelectedValues.carBrandsSelected.joinToString(" ") {
//                if (it.models.isNullOrEmpty()) it.brand else "${it.brand}[${it.models.size}]"
//            }
//        } else {
//            "Makes & Models"
//        }
//
//        carBrandTxt.text = displayCars



    }
}