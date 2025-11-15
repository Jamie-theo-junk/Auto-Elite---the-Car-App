package com.harmless.autoelitekotlin.model.utils

data class Constants (

    val mileage: List<String>  = mutableListOf("Mileage", "0-9 999kms", "10 000-49 999kms", "50 000-99 999kms", "100 000-149 999kms", "150 000-199 999kms", "200 000kms+"),
    val driveTrain: List<String> = mutableListOf("Type","(AWD)4×4","(FWD)Front Wheel Drive","(RWD)Rear Wheel Drive"),
    val newOrUsed: List<String> = mutableListOf("New & Used","New","Used"),
    val transmission: List<String> = mutableListOf("Automatic","Manual"),
    val fuelType: List<String> = mutableListOf("Fuel Type","Petrol","Diesel","Electric"),
    val provinces: List<String> = mutableListOf("Eastern Cape","Gauteng","KwaZulu-Natal","Limpopo","Northern Cape","North West","Western Cape"),
    val color : List<String> = mutableListOf("Red","Yellow","Green","Blue","Silver","Black","White","Orange","Pink","Purple","Brown"),
    val bodyTypes: List<String> = mutableListOf(
        "Sedan",
        "Hatchback",
        "SUV",
        "Coupe",
        "Convertible",
        "Bakkie",
        "Minivan",
        "Station Wagon",
        "Crossover"
    ),
    val minPrices:List<String> = listOf(
        "Min",
            "0km",
            "10000km",
            "50000km",
            "100000km",
            "150000km",
            "200000km",
            "250000km",
            "300000km",
            "350000km",
            "400000km",
            "more than 450000km"
        ),
    val maxPrices:List<String> = listOf(
        "Max",
        "10000km",
        "50000km",
        "100000km",
        "150000km",
        "200000km",
        "250000km",
        "300000km",
        "350000km",
        "400000km",
        "more than 450000km"
    )

)


