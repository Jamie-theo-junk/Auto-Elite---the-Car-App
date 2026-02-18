package com.harmless.autoelitekotlin.view.activities.SellActivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivitySellSelectBodyTypeBinding
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.utils.Constants
import com.harmless.autoelitekotlin.view.adapters.BodyTypeSellAdapter
import com.harmless.autoelitekotlin.view.adapters.BrandSellAdapter
import com.harmless.autoelitekotlin.viewModel.CarBrandCallback
import com.harmless.autoelitekotlin.viewModel.SellCarViewModel

class SellSelectBodyTypeActivity : AppCompatActivity() {
    lateinit var binding: ActivitySellSelectBodyTypeBinding

    lateinit var bodyTypeRecycler: RecyclerView
    lateinit var continueButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySellSelectBodyTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

private fun init() {
    bodyTypeRecycler = binding.sellSelectionRecyclerview
    continueButton = binding.applyBtn
    val bodyTypesArray = Constants().bodyTypes
    setupBrandRecycler(bodyTypesArray)
    setupContinueButton()
}

private fun setupBrandRecycler(bodyTypes: List<String>) {

    bodyTypeRecycler.layoutManager = GridLayoutManager(this, 2)
    bodyTypeRecycler.adapter = BodyTypeSellAdapter(bodyTypes) { bodyTypes ->
        SellCarViewModel.SellSession.selectedBodyType = bodyTypes
    }
}

private fun setupContinueButton() {
    continueButton.setOnClickListener {
        val bodyTypes = SellCarViewModel.SellSession.selectedBodyType
        Log.d(TAG, "setupContinueButton: ${SellCarViewModel.SellSession.selectedBrand }")
        Log.d(TAG, "setupContinueButton: $bodyTypes")

        if (bodyTypes != null) {

            val toSellFragment = Intent(this, SellCarActivity::class.java)
            startActivity(toSellFragment)

        } else {
            Toast.makeText(this, "Please select a brand", Toast.LENGTH_SHORT).show()
        }
    }
}

companion object {
    private const val TAG = "SellSelectBodyTypeActivity"
}

}