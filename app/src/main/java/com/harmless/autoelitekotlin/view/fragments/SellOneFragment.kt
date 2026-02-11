package com.harmless.autoelitekotlin.view.fragments

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.harmless.autoelitekotlin.databinding.FragmentSellOneBinding
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.activities.MainActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellCarActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellSelectBodyTypeActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellSelectBrandActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellSelectModelActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellSelectVariantActivity
import com.harmless.autoelitekotlin.view.activities.SellActivities.SellSelectYear
import com.harmless.autoelitekotlin.view.adapters.SpinnerAdapter
import com.harmless.autoelitekotlin.viewModel.SellCarViewModel


class SellOneFragment : Fragment() {

    private var _binding: FragmentSellOneBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandButton: ConstraintLayout
    private lateinit var modelButton: ConstraintLayout
    private lateinit var variantButton: ConstraintLayout
    private lateinit var yearButton: ConstraintLayout
    private lateinit var bodyButton: ConstraintLayout

    private lateinit var brandTxt: TextView
    private lateinit var modelTxt: TextView
    private lateinit var variantTxt: TextView
    private lateinit var yearTxt: TextView
    private lateinit var bodyTxt: TextView

    private lateinit var colorSpinner: Spinner
    private lateinit var transmissionSpinner:Spinner
    private lateinit var WheelDriveSpinner:Spinner

    //navigation buttons

    private lateinit var backButton: CardView
    private lateinit var continueButton:CardView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        populateSavedValues()
        populateSpinners()
        navigationButtons()
    }

    private fun initViews() {
        brandTxt = binding.carBrand
        modelTxt = binding.carModel
        variantTxt = binding.carVariant
        yearTxt = binding.carYear
        bodyTxt = binding.carBody

        brandButton = binding.sellCarBrandCardview
        modelButton = binding.sellCarModelCardview
        variantButton = binding.sellCarVariantCardview
        yearButton = binding.sellCarYearCardview
        bodyButton = binding.sellCarBodyCardview

        colorSpinner = binding.sellCarColorSpinner
        transmissionSpinner = binding.sellCarTransmissionCardview
        WheelDriveSpinner = binding.sellCarWheelCardview

        backButton = binding.sellOneBackButton
        continueButton = binding.sellOneContinueButton
    }

    private fun setupListeners() {
        brandButton.setOnClickListener {
            startActivity(Intent(requireContext(), SellSelectBrandActivity::class.java))
        }

        modelButton.setOnClickListener {
            // Must select brand first
            if (SellCarViewModel.SellSession.selectedBrand == null) {
                Toast.makeText(requireContext(), "Please select a car brand first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(requireContext(), SellSelectModelActivity::class.java))
        }

        variantButton.setOnClickListener {
            // Must select brand and model first
            if (SellCarViewModel.SellSession.selectedBrand == null ||
                SellCarViewModel.SellSession.selectedModel == null) {
                Toast.makeText(requireContext(), "Please select a brand and model first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(requireContext(), SellSelectVariantActivity::class.java))
        }

        yearButton.setOnClickListener {

            startActivity(Intent(requireContext(), SellSelectYear::class.java))
        }

        bodyButton.setOnClickListener {

            startActivity(Intent(requireContext(), SellSelectBodyTypeActivity::class.java))
        }



    }

    private fun populateSpinners(){
        val carColors = Constants().color

        val colorAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            carColors
        )
        colorAdapter.setDropDownViewResource(R.layout.simple_spinner_item)

        colorSpinner.adapter = colorAdapter

        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedColor = carColors[position]
                Log.d(TAG, "onItemSelected: ${SellCarViewModel.SellSession.selectedColor}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }

        val transmissionColors = Constants().transmission

        val transmissionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            transmissionColors
        )
        transmissionAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        transmissionSpinner.adapter = transmissionAdapter

        transmissionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedTransmission = transmissionColors[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }

        val carWheelDrive = Constants().driveTrain

        val wheelDriveAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            carWheelDrive
        )
        wheelDriveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        WheelDriveSpinner.adapter = wheelDriveAdapter

        WheelDriveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                SellCarViewModel.SellSession.selectedWheelDrive = carWheelDrive[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional
            }
        }


    }
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


    private fun navigationButtons(){
        backButton.setOnClickListener {
            SellCarViewModel.SellSession.reset()

            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        continueButton.setOnClickListener {
            (requireActivity() as SellCarActivity).goToNextPage()
        }
    }


    private fun populateSavedValues() {

        val brand = SellCarViewModel.SellSession.selectedBrand?.brand ?: "Select Brand"
        val model = SellCarViewModel.SellSession.selectedModel?.name ?: "Select Model"
        val variant = SellCarViewModel.SellSession.selectedVariant ?: "Select Variant"
        val year = SellCarViewModel.SellSession.selectedYear?.toString() ?: "Select Year"
        val body = SellCarViewModel.SellSession.selectedBodyType?: "Select Body"

        brandTxt.text = brand
        modelTxt.text = model
        variantTxt.text = variant
        yearTxt.text = year
        bodyTxt.text = body



    }

    override fun onResume() {
        super.onResume()

        populateSavedValues()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SellOneFragment"
    }
}
