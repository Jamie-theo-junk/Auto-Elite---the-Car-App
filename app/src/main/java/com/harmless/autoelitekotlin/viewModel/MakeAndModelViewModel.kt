package com.harmless.autoelitekotlin.viewModel

import SelectedValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.harmless.autoelitekotlin.model.CarBrand
import com.harmless.autoelitekotlin.model.CarModel
import com.harmless.autoelitekotlin.model.utils.SelectedCar

interface CarBrandCallback {
    fun onCarBrandLoaded(carBrands: List<CarBrand>)
    fun onCancelled(error: DatabaseError)
}

class MakeAndModelViewModel {
    private var carBrands = mutableListOf<CarBrand>()
    private val selectedBrands = mutableSetOf<String>()
    private val selectedModels = mutableSetOf<Pair<String, String>>()  // brand, model
    private val selectedVariants = mutableSetOf<Triple<String, String, String>>()  // brand, model, variant


    /**
     * Loads all car brands and their models from Firebase.
     * The data is stored under the "carRecycler" path.
     */
    fun getCarBrand(callback: CarBrandCallback) {
        val carsRef = FirebaseDatabase.getInstance().getReference("carRecycler")
        val listener = object : ValueEventListener {
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
        }
        carsRef.addValueEventListener(listener)
    }
    fun isExpandable(carsBrandList: List<CarModel>): Boolean =
        carsBrandList.isNotEmpty()

    fun saveCar() {
        SelectedValues.carBrandsSelected = selectedBrands.toMutableList()
        SelectedValues.carModelsSelected = selectedModels.toMutableList()
        SelectedValues.carVariantsSelected = selectedVariants.toMutableList()
    }
    fun toggleBrandSelection(brand: CarBrand, isChecked: Boolean) {
        if (isChecked) selectedBrands.add(brand.brand)
        else {
            selectedBrands.remove(brand.brand)
            brand.models.forEach { m ->
                selectedModels.remove(Pair(brand.brand, m.name))
                m.variants.forEach { v ->
                    selectedVariants.remove(Triple(brand.brand, m.name, v.name))
                }
            }
        }
    }

    fun toggleModelSelection(brand: CarBrand, model: CarModel, isChecked: Boolean) {
        val key = Pair(brand.brand, model.name)
        if (isChecked) selectedModels.add(key)
        else {
            selectedModels.remove(key)
            model.variants.forEach {
                selectedVariants.remove(Triple(brand.brand, model.name, it.name))
            }
        }
    }

    fun toggleVariantSelection(brand: CarBrand, model: CarModel, variant: String, isChecked: Boolean) {
        val key = Triple(brand.brand, model.name, variant)
        if (isChecked) selectedVariants.add(key)
        else selectedVariants.remove(key)
    }

    fun isBrandSelected(brandName: String) = brandName in SelectedValues.carBrandsSelected
    fun isModelSelected(brand: String, model: String) = SelectedValues.carModelsSelected.contains(Pair(brand, model))
    fun isVariantSelected(brand: String, model: String, variant: String) =
        SelectedValues.carVariantsSelected.contains(Triple(brand, model, variant))
}


