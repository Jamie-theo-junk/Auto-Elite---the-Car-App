package com.harmless.autoelitekotlin.view.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityCarListingBinding
import com.harmless.autoelitekotlin.model.Car
import com.harmless.autoelitekotlin.view.adapters.ImageSliderAdapter
import com.harmless.autoelitekotlin.view.adapters.ThumbnailAdapter
import com.harmless.autoelitekotlin.view.fragments.FullscreenImageDialog
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class CarListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarListingBinding

    private lateinit var brandTxt : TextView
    private lateinit var modelTxt : TextView
    private lateinit var variantTxt : TextView
    private lateinit var mileageTxt : TextView
    private lateinit var newOrUsedTxt : TextView
    private lateinit var yearTxt : TextView
    private lateinit var descriptionTxt : TextView
    private lateinit var descriptionTxtReadMore:TextView
    private lateinit var priceTxt:TextView

    private lateinit var images : ViewPager2
    private lateinit var financeTxt:TextView
    private lateinit var thumbnailRecycler: RecyclerView


    private lateinit var callBtn : Button
    private lateinit var messageBtn : Button
    private lateinit var arrowBackBtn :Button

    private lateinit var car:Car



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCarListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        car = intent.getParcelableExtra<Car>("car")!!

        initViews()
        populateValues()
        setUpListeners()
    }

    private fun initViews(){
        brandTxt = binding.carListingBrand
        modelTxt = binding.carListingModel
        variantTxt = binding.carListingVariant
        mileageTxt = binding.carListingMileageValue
        newOrUsedTxt = binding.carListingNewUsedValue
        yearTxt = binding.carListingYearValue
        descriptionTxt = binding.listCarDescriptionTxt
        descriptionTxtReadMore = binding.listCarReadMoreTxt
        priceTxt = binding.carListingPrice
        financeTxt = binding.carListingPriceMonthly

        arrowBackBtn = binding.arrowBack

        images = binding.listingCarViewpager

    }

    private fun populateValues(){
        brandTxt.text = car.brand
        modelTxt.text = car.model
        variantTxt.text = car.variant
        mileageTxt.text = car.mileage.toString()

        val format = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        format.maximumFractionDigits = 0
        val formattedPrice = format.format(car.price)

        val years = 5
        val annualInterestRate = 7.0 // change if needed, 0.0 for no interest
        val principal = car.price

        val monthlyRate = annualInterestRate / 12 / 100
        val months = years * 12

        val monthlyPayment = if (monthlyRate == 0.0) {
            principal / months
        } else {
            val factor = (1 + monthlyRate).pow(months)
            principal * monthlyRate * factor / (factor - 1)
        }

        val formattedFinance = format.format(monthlyPayment)
        financeTxt.text = formattedFinance
        priceTxt.text = formattedPrice

        newOrUsedTxt.text = car.IsNew



        yearTxt.text = car.year.toString()

        descriptionTxt.text = car.description
        descriptionTxt.post {
            val lineCount = descriptionTxt.lineCount
            if (lineCount > 4) {

                descriptionTxtReadMore.visibility = View.VISIBLE
                descriptionTxt.maxLines = 4
                descriptionTxt.ellipsize = TextUtils.TruncateAt.END
            } else {
                descriptionTxtReadMore.visibility = View.GONE
            }
        }

        // Handle click for Read More
        descriptionTxtReadMore.setOnClickListener {
            descriptionTxt.maxLines = Integer.MAX_VALUE // remove limit
            descriptionTxt.ellipsize = null
            descriptionTxtReadMore.visibility = View.GONE
        }

        val imageAdapter = ImageSliderAdapter(car.images) { position ->
            FullscreenImageDialog(car.images, position) { newPosition ->
                images.setCurrentItem(newPosition, true) // Update main ViewPager
            }.show(supportFragmentManager, "fullscreen_image")
        }

        images.adapter = imageAdapter
        thumbnailRecycler = binding.listingCarRecycler
        val thumbnailAdapter = ThumbnailAdapter(car.images) { position ->
            images.currentItem = position
        }
        thumbnailRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        thumbnailRecycler.adapter = thumbnailAdapter

        // Sync thumbnails with pager
        images.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                thumbnailAdapter.setSelected(position)
            }
        })






    }
    private fun setUpListeners(){
        arrowBackBtn.setOnClickListener {
            finish()
        }
    }

}