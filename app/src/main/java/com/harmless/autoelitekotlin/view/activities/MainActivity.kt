package com.harmless.autoelitekotlin.view.activities

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.harmless.autoelitekotlin.R
import com.harmless.autoelitekotlin.model.User
import com.harmless.autoelitekotlin.view.adapters.ViewPagerAdapter

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var navigationView: NavigationView
    private lateinit var navigationButton: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SelectedValues.init(this)
        init()
    }

    fun init(){

        drawerLayout = findViewById(R.id.my_drawer_layout)
        navigationView = findViewById(R.id.navigation_menu)
        navigationButton = findViewById(R.id.NavButton)
        setupDrawer()
        setFragment()
        loadUserProfile()

    }
    private fun setupDrawer() {
        navigationButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Handle menu item clicks
        navigationView.setNavigationItemSelectedListener { item ->
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

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun loadUserProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val uid = currentUser.uid

        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                user?.let {

                    val imageView = findViewById<ImageView>(R.id.main_login_image)
                    Glide.with(this)
                        .load(it.profileImageUrl)
                        .placeholder(R.drawable.logo)
                        .into(imageView)


                    val nameTextView = findViewById<TextView>(R.id.LoginUsersName)
                    nameTextView.text = it.name
                }
            } else {

                Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load user profile: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFragment(){
        tabLayout = findViewById(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)
        updateTabStyle(0)
    }

    private fun updateTabStyle(position:Int?){
        var tab: TabLayout.Tab? = tabLayout.getTabAt(position!!)

        if(tab != null){
            var color:Int = getFragmentThemeColor(position)
            tabLayout.setSelectedTabIndicatorColor(color)
        }
    }

    private fun getFragmentThemeColor(position:Int):Int{
        when(position){
            0->{
                return resources.getColor(R.color.car_screen_color)
            }
            1->{
                return resources.getColor(R.color.motorbike_screen_color)
            }
            2->{
                return resources.getColor(R.color.bike_screen_color)
            }
            3->{
                return resources.getColor(R.color.accessories_screen_color)
            }
            else ->{
                return resources.getColor(R.color.black, theme)
            }
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        var tabPosition = tab?.position
        updateTabStyle(tabPosition)
        val TabLayout =
            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
        val tabTextView = TabLayout.getChildAt(1) as TextView
        tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)

        tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        val TabLayout =
            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(tab!!.position) as LinearLayout
        val tabTextView = TabLayout.getChildAt(1) as TextView
        tabTextView.setTypeface(null, Typeface.NORMAL)
        tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}