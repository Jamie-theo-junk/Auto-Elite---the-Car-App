package com.harmless.autoelitekotlin.viewModel

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harmless.autoelitekotlin.model.Car
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.utils.Constants


const val TAG = "CarViewModel"

class CarViewModel {

    private val carList = mutableListOf<Car>()

    interface CarsCallback {
        fun onDataLoaded(cars: List<Car>)
        fun onCancelled(error: DatabaseError)
    }

    fun setCars(callback: CarsCallback) {
        val carsReference = FirebaseDatabase.getInstance().getReference("cars")

        carsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                carList.clear()
                for (carSnapshot in snapshot.children) {
                    val car = carSnapshot.getValue(Car::class.java)
                    car?.let {
                        // Default mileage to 0 if null
                        if (it.mileage == null) it.mileage = 0
                        carList.add(it)
                    }
                }
                callback.onDataLoaded(carList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onCancelled(error)
            }
        })
    }
//        fun filterCars(allCars: List<Car>): List<Car> {
//        // If nothing selected, return all cars
//        if (SelectedValues.carBrandsSelected.isEmpty() && SelectedValues.carModelsSelected.isEmpty() && SelectedValues.carVariantsSelected.isEmpty()) {
//            return allCars
//        }
//
//        return allCars.filter { car ->
//            val brandMatch = SelectedValues.carBrandsSelected.contains(car.brand)
//            val modelMatch = SelectedValues.carModelsSelected.contains(Pair(car.brand, car.model))
//            val variantMatch = SelectedValues.carVariantsSelected.contains(Triple(car.brand, car.model, car.variant))
//
//            brandMatch || modelMatch || variantMatch
//        }
//    }
    fun minMileage(selectedMileage: String): Int {
        val constants = Constants()
        return when (selectedMileage) {
            constants.mileage[1] -> 0
            constants.mileage[2] -> 10_000
            constants.mileage[3] -> 50_000
            constants.mileage[4] -> 100_000
            constants.mileage[5] -> 150_000
            constants.mileage[6] -> 200_000
            else -> Int.MIN_VALUE
        }
    }

    fun maxMileage(selectedMileage: String): Int {
        val constants = Constants()
        return when (selectedMileage) {
            constants.mileage[1] -> 9_999
            constants.mileage[2] -> 49_999
            constants.mileage[3] -> 99_999
            constants.mileage[4] -> 149_999
            constants.mileage[5] -> 199_999
            constants.mileage[6] -> 10_000_000
            else -> Int.MAX_VALUE
        }
    }
    fun setSelectedCars(
        yearSelected: List<Int>,
        colorSelected: List<String>,
        driveTrainSelected: String?,
        locationSelected: List<String>,
        minMileageSelected: Int? = null,
        maxMileageSelected: Int? = null,
        minPriceSelected: Int? = null,
        maxPriceSelected: Int? = null,
        transmissionSelected: String?,
        callback: (List<Car>) -> Unit
    ) {
        setCars(object : CarsCallback {
            override fun onDataLoaded(cars: List<Car>) {

                val selBrands = SelectedValues.carBrandsSelected
                val selModels = SelectedValues.carModelsSelected
                val selVariants = SelectedValues.carVariantsSelected

                val filtered = cars.filter { car ->

                    // --- Brand filtering ---
                    val brandMatch =
                        if (selBrands.isEmpty()) true
                        else selBrands.contains(car.brand)

                    // --- Model filtering ---
                    val modelMatch =
                        if (selModels.isEmpty()) true
                        else selModels.contains(Pair(car.brand, car.model))

                    // --- Variant filtering ---
                    val variantMatch =
                        if (selVariants.isEmpty()) true
                        else selVariants.contains(Triple(car.brand, car.model, car.variant))

                    // Only accept cars that match ALL non-empty lists
                    val categoryMatches = brandMatch && modelMatch && variantMatch


                    // --- Color filtering ---
                    val colorMatches =
                        if (colorSelected.isEmpty()) true
                        else colorSelected.contains(car.color)

                    // --- Type ---
                    val typeMatches =
                        if (driveTrainSelected.isNullOrEmpty() || driveTrainSelected == "Type") true
                        else car.wheelDrive == driveTrainSelected

                    // --- Location ---
                    val locationMatches =
                        if (locationSelected.isEmpty()) true
                        else locationSelected.contains(car.location)

                    // --- Mileage ---
                    val mileage = car.mileage ?: 0
                    val mileageMatches =
                        (minMileageSelected ?: Int.MIN_VALUE) <= mileage &&
                                (maxMileageSelected ?: Int.MAX_VALUE) >= mileage

                    // --- Price ---
                    val price = car.price ?: 0.0
                    val priceMatches =
                        (minPriceSelected?.toDouble() ?: Double.NEGATIVE_INFINITY) <= price &&
                                (maxPriceSelected?.toDouble() ?: Double.POSITIVE_INFINITY) >= price

                    // --- Transmission ---
                    val transmissionMatches =
                        if (transmissionSelected.isNullOrEmpty()) true
                        else car.transmission == transmissionSelected

                    // --- Year ---
                    val yearMatches =
                        if (yearSelected.isEmpty()) true
                                else yearSelected.contains(car.year)


                    categoryMatches &&
                            colorMatches &&
                            typeMatches &&
                            locationMatches &&
                            mileageMatches &&
                            priceMatches &&
                            transmissionMatches &&
                            yearMatches
                }.sortedByDescending { it.year }

                callback(filtered)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }






    private fun sortYear() {
        SelectedValues.selectedYear.sort()
    }
}