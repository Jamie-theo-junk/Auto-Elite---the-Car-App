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
class CarFragment : Fragment(){

    companion object {
        private const val TAG = "CAR_FRAGMENT"
    }

    private val viewModel = CarViewModel()
    private lateinit var carBrandTxt: TextView
    private lateinit var mileageStr: String

    private lateinit var mileageSpinner:Spinner
    private lateinit var minPriceSpinner:Spinner
    private lateinit var maxPriceSpinner:Spinner
    private lateinit var typeSpinner :Spinner

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
         mileageSpinner = view.findViewById<Spinner>(R.id.mileageBrandSpinner)
         minPriceSpinner = view.findViewById<Spinner>(R.id.minPriceSpinner)
         maxPriceSpinner = view.findViewById<Spinner>(R.id.maxPriceSpinner)
         typeSpinner = view.findViewById<Spinner>(R.id.typeBrandSpinner)
        val yearRelative = view.findViewById<RelativeLayout>(R.id.yearBrandSpinner)
        val searchBtn = view.findViewById<Button>(R.id.SearchBtn)
        val showMoreTxt = view.findViewById<TextView>(R.id.showMoreId)
        val resetBtn = view.findViewById<TextView>(R.id.resetId)
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
//        mileageStr = SelectedValues.selectedMileage ?: constants.mileage.first()
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

        resetBtn.setOnClickListener {
                // --- Reset SelectedValues ---
               SelectedValues.clear()
            SelectedValues.init(requireContext())
                // --- Reset UI components ---
                carBrandTxt.text = "Makes & Models"

                // Reset all spinners to first index (position 0)
                typeSpinner?.setSelection(0)
                mileageSpinner?.setSelection(0)
                minPriceSpinner?.setSelection(0)
                maxPriceSpinner?.setSelection(0)

                Toast.makeText(requireContext(), "Filters reset", Toast.LENGTH_SHORT).show()

        }

        // --- Search button ---
        searchBtn.setOnClickListener {
            Log.d(TAG, "init: Year:Selected Year: ${SelectedValues.selectedYear}")
            viewModel.setSelectedCars(
                SelectedValues.selectedYear,
                SelectedValues.selectedColor,
                SelectedValues.selectedDriveTrain,
                SelectedValues.selectedProvince,
                SelectedValues.selectedMinMileage,
                SelectedValues.selectedMaxMileage,
                parsePriceStringToInt(SelectedValues.selectedMinPrice),
                parsePriceStringToInt(SelectedValues.selectedMaxPrice),
                SelectedValues.selectedTransmission,
                        SelectedValues.isNewOrUsed,
                SelectedValues.selectedFuelType
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

    override fun onResume() {
        super.onResume()

        // Update car brand text
        carBrandTxt.text = if (SelectedValues.carBrandsSelected.isNotEmpty()) {
            SelectedValues.carBrandsSelected.joinToString(", ")
        } else {
            "Makes & Models"
        }

        // Sync spinners
        typeSpinner?.let {
            val idx = Constants().driveTrain.indexOf(SelectedValues.selectedDriveTrain)
            if (idx >= 0) it.setSelection(idx)
        }

        mileageSpinner?.let {
            val idx = Constants().mileage.indexOf(SelectedValues.selectedMileage)
            if (idx >= 0) it.setSelection(idx)
        }

        minPriceSpinner?.let {
            val idx = Constants().minPrices.indexOf(SelectedValues.selectedMinPrice)
            if (idx >= 0) it.setSelection(idx)
        }

        maxPriceSpinner?.let {
            val idx = Constants().maxPrices.indexOf(SelectedValues.selectedMaxPrice)
            if (idx >= 0) it.setSelection(idx)
        }
    }
}