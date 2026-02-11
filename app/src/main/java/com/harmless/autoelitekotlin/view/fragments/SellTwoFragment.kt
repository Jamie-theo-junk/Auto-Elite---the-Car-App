package com.harmless.autoelitekotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.Api
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.FragmentSellOneBinding
import com.harmless.autoelitekotlin.databinding.FragmentSellTwoBinding
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.activities.MainActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellCarActivity
import com.harmless.autoelitekotlin.viewModel.SellCarViewModel
import com.harmless.autoelitekotlin.viewModel.SellCarViewModel.SellSession.selectedTransmission


class SellTwoFragment : Fragment() {
    private var _binding: FragmentSellTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var newOrUsedSpinner:Spinner
    private lateinit var provinceSpinner:Spinner

    private lateinit var fuelSpinner:Spinner

    private lateinit var mileageText: TextView
    private lateinit var priceText: TextView
    private lateinit var descriptionText: TextView

    private lateinit var backButton: CardView
    private lateinit var continueButton: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setUpListeners()
        navigationButtons()
    }

    private fun initViews(){
        newOrUsedSpinner = binding.sellCarNewUsedCardview
        provinceSpinner = binding.sellCarProvinceCardview
        fuelSpinner = binding.sellCarFuelTypeCardview

        mileageText = binding.sellCarMileageCardview
        priceText = binding.sellCarPriceCardview
        descriptionText = binding.sellCarDescriptionSpinner

        backButton = binding.sellOneBackButton
        continueButton = binding.sellOneContinueButton
    }

    private fun setUpListeners(){

        mileageText.addTextChangedListener { editable ->
            SellCarViewModel.SellSession.selectedMileage = editable.toString().toIntOrNull() ?: 0

        }
        descriptionText.addTextChangedListener { editable ->
            SellCarViewModel.SellSession.selectedDescription = editable.toString()

        }
        priceText.addTextChangedListener { editable ->
            SellCarViewModel.SellSession.selectedPrice = editable.toString().toIntOrNull() ?: 0

        }

        //spinners
        val newOrUsedArray = Constants().newOrUsed

        val newOrUsedAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            newOrUsedArray
        )
        newOrUsedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        newOrUsedSpinner.adapter = newOrUsedAdapter

        newOrUsedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedNewOrUsed = newOrUsedArray[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }
        val provinceArray = Constants().provinces

        val provinceAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            provinceArray
        )
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        provinceSpinner.adapter = provinceAdapter

        provinceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedProvince = provinceArray[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }

        val fuelTypeArray = Constants().fuelType

        val fuelTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            fuelTypeArray
        )
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fuelSpinner.adapter = fuelTypeAdapter

        fuelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedFuelType = fuelTypeArray[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }

    }
    private fun navigationButtons(){
        backButton.setOnClickListener {
            (requireActivity() as SellCarActivity).goToPreviousPage()
        }

        continueButton.setOnClickListener {
            (requireActivity() as SellCarActivity).goToNextPage()
        }
    }
    companion object {
        private const val TAG = "SellTwoFragment"
    }
}