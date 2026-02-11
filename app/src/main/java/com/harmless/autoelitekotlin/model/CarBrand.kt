package com.harmless.autoelitekotlin.model

import com.google.firebase.database.Exclude

data class CarBrand(
    val brand: String = "",
    val models: MutableList<CarModel> = mutableListOf()
) {
    @get:Exclude
    var isExpandable: Boolean = false

    @get:Exclude
    var isChecked: Boolean = false

    @get:Exclude
    var isCheckedInitialized: Boolean = false
}

data class CarModel(
    val name: String = "",
    val variants: MutableList<CarVariant> = mutableListOf()
) {
    @get:Exclude
    var isExpandable: Boolean = false

    @get:Exclude
    var isChecked: Boolean = false

    @get:Exclude
    var isCheckedInitialized: Boolean = false
}

data class CarVariant(
    val name: String = ""
) {
    @get:Exclude
    var isChecked: Boolean = false

    @get:Exclude
    var isCheckedInitialized: Boolean = false
}