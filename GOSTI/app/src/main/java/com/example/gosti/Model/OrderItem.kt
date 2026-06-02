package com.example.gosti.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class OrderItem(
    var id: Long = 0,
    var name: String = "",
    var image: String = "",
    var price: Double = 0.0,
    var quantity: Int = 1
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        name = parcel.readString() ?: "",
        image = parcel.readString() ?: "",
        price = parcel.readDouble(),
        quantity = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem = OrderItem(parcel)
        override fun newArray(size: Int): Array<OrderItem?> = arrayOfNulls(size)
    }
}
