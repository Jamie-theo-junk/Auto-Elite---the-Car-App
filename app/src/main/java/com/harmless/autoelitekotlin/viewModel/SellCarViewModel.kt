package com.harmless.autoelitekotlin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harmless.autoelitekotlin.model.Car
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.CarModel
import com.harmless.autoelitekotlin.model.CarVariant
import com.harmless.autoelitekotlin.model.User

class SellCarViewModel : ViewModel() {
    private val carBrands = mutableListOf<CarBrand>()
    private val _sellCarObject = MutableLiveData(
        Car(
            BodyType = "",
            IsNew = "",
            brand = "",
            color = "",
            images = emptyList(),
            location = "",
            mileage = 0,
            model = "",
            price = 0.0,
            provinces = "",
            transmission = "",
            type = "",
            year = 0,
            wheelDrive = "",
            variant = "",
            description = "",
            fuelType = "",
            user = User(
                uid = "",
                name = "",
                email = "",
                profileImageUrl = ""
            )
        )
    )

    val sellCarObject: LiveData<Car> = _sellCarObject

    // ----------------- Firebase Brand Loading -----------------
    fun getCarBrand(callback: CarBrandCallback) {
        val carsRef = FirebaseDatabase.getInstance().getReference("carRecycler")
        carsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                carBrands.clear()
                for (carSnapshot in snapshot.children) {
                    val carBrand = carSnapshot.getValue(CarBrand::class.java)
                    if (carBrand != null) carBrands.add(carBrand)
                }
                callback.onCarBrandLoaded(carBrands)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onCancelled(error)
            }
        })
    }

    fun getCarModels(carBrand: CarBrand): List<CarModel> = carBrand.models

    fun getCarVariants(carModel: CarModel): List<String> {
        return carModel.variants.map { it.name }
    }

    // ----------------- Update Pages -----------------


    // ----------------- Selected State -----------------
    object SellSession {
        var selectedBrand: CarBrand? = null
        var selectedModel: CarModel? = null
        var selectedVariant: String? = null
        var selectedFuelType: String? = null

        var selectedYear: Int? = null

        var selectedColor: String? = null

        var selectedTransmission: String? = null

        var selectedBodyType: String? = null

        var selectedWheelDrive: String? = null

        var selectedPrice:Int? = null
        var selectedNewOrUsed: String? = null
        var selectedMileage:Int? = null
        var selectedDescription:String? = null
        var selectedProvince:String? = null





        fun reset() {
            selectedBrand = null
            selectedModel = null
            selectedVariant = null

        }
    }
}