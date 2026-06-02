package com.example.gosti.Model

import android.os.Parcel
import android.os.Parcelable

data class CartItems(
    var id: Long? = null,
    var userUid: String,
    var foodName: String,
    var foodPrice: String = "0",
    var foodDescription: String = "",
    var foodImage: String = "",
    var foodQuantity: Int = 1,
    var foodIngredients: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readValue(Long::class.java.classLoader) as? Long,
        userUid = parcel.readString() ?: "",
        foodName = parcel.readString() ?: "",
        foodPrice = parcel.readString().toString(), // Int
        foodDescription = parcel.readString() ?: "",
        foodImage = parcel.readString() ?: "",
        foodQuantity = parcel.readInt(),
        foodIngredients = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(userUid)
        parcel.writeString(foodName)
        parcel.writeString(foodPrice)
        parcel.writeString(foodDescription)
        parcel.writeString(foodImage)
        parcel.writeInt(foodQuantity)
        parcel.writeString(foodIngredients)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CartItems> {
        override fun createFromParcel(parcel: Parcel) = CartItems(parcel)
        override fun newArray(size: Int) = arrayOfNulls<CartItems?>(size)
    }
}
