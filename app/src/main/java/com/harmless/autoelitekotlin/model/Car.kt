package com.harmless.autoelitekotlin.model

import android.os.Parcel
import android.os.Parcelable

data class Car(
    var BodyType: String,
    var IsNew: String,
    var brand: String,
    var color: String,
    var images: List<String>,
    var location: String,
    var mileage: Int,
    var model: String,
    var price: Double,
    var provinces: String,
    var transmission: String,
    var type: String,
    var year: Int,
    var wheelDrive: String,
    var variant: String,
    var description: String,
    var fuelType:String,
    var user: User
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(User::class.java.classLoader) ?: User()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(BodyType)
        dest.writeString(IsNew)
        dest.writeString(brand)
        dest.writeString(color)
        dest.writeStringList(images)
        dest.writeString(location)
        dest.writeInt(mileage)
        dest.writeString(model)
        dest.writeDouble(price)
        dest.writeString(provinces)
        dest.writeString(transmission)
        dest.writeString(type)
        dest.writeInt(year)
        dest.writeString(wheelDrive)
        dest.writeString(variant)
        dest.writeString(description)
        dest.writeString(fuelType)
        dest.writeParcelable(user, flags)
    }

    override fun describeContents() = 0

    constructor() : this(
        "",
        "",
        "",
        "",
        emptyList(),
        "",
        0,
        "",
        0.0,
        "",
        "",
        "",
        0,
        "",
        "",
        "",
        "",
        User()
    )

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel) = Car(parcel)
        override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)
    }
}
