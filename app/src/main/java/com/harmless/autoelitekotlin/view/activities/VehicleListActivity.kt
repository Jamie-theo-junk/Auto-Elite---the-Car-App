package com.harmless.autoelitekotlin.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.databinding.ActivityVehicleListBinding
import com.harmless.autoelitekotlin.model.Car
import com.harmless.autoelitekotlin.model.User
import com.harmless.autoelitekotlin.view.adapters.VehicleListReyclerAdapter

class VehicleListActivity : AppCompatActivity() {

    private var cars: MutableList<Car> = mutableListOf()
    private lateinit var binding: ActivityVehicleListBinding
    private lateinit var adapter: VehicleListReyclerAdapter
    private lateinit var sortBySpinner: Spinner
    private lateinit var titleTxt: TextView

    private lateinit var refineBtn : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVehicleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent: Intent = intent
        @Suppress("UNCHECKED_CAST")
        cars = (intent.getSerializableExtra("selectedCars") as? List<Car>)?.toMutableList()
            ?: mutableListOf()

        setupDrawer()
        initRecycler()
        setupSpinner()
        initViews()
        loadUserProfile()
    }

    private fun setupDrawer() {

        // Open drawer when nav button is clicked
        binding.vehicleListNav.setOnClickListener {
            if (binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.myDrawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Handle menu item clicks
        binding.navigationMenu.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_account -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                }
            }

            binding.myDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initViews(){
        titleTxt = binding.vehicleListText
        titleTxt.text = "Search Results(${cars.size})"

        refineBtn = binding.refineButton
        refineBtn.setOnClickListener {
            finish()
        }
    }

    private fun initRecycler() {
        val vehicleRecycler = binding.CarRecyclerView
        vehicleRecycler.setHasFixedSize(true)
        vehicleRecycler.layoutManager = LinearLayoutManager(this)
        adapter = VehicleListReyclerAdapter(cars)
        vehicleRecycler.adapter = adapter
    }
    private fun loadUserProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val uid = currentUser.uid

        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                user?.let {
                    // Load profile image
                    val imageView = findViewById<ImageView>(R.id.vehicle_list_google_image)
                    Glide.with(this)
                        .load(it.profileImageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .into(imageView)
                }
            }
        }.addOnFailureListener {

        }
    }

    private fun setupSpinner() {
        sortBySpinner = binding.vehicleListSort

        ArrayAdapter.createFromResource(
            this,
            R.array.sort_options,
            R.layout.spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortBySpinner.adapter = spinnerAdapter
        }

        sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selected = parent?.getItemAtPosition(position).toString()
                val sortedList = when (selected) {
                    "Sort By Lowest Price" -> cars.sortedBy { it.price }
                    "Sort By Highest Price" -> cars.sortedByDescending { it.price }
                    "Sort By Lowest Date" -> cars.sortedBy { it.year }
                    "Sort By Highest Date" -> cars.sortedByDescending { it.year }
                    else -> cars // "Sort By Default"
                }

                adapter.updateData(sortedList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}