package com.example.gosti.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderDetails(
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("userUid")
    var userUid: String? = null,

    @SerializedName("userName")
    var userName: String? = null,

    @SerializedName("items")
    var items: MutableList<OrderItem> = mutableListOf(),

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("totalPrice")
    var totalPrice: Double? = 0.0,

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,

    @SerializedName("orderAccepted")
    var orderAccepted: Boolean = false,

    @SerializedName("paymentReceived")
    var paymentReceived: Boolean = false,

    @SerializedName("itemPushKey")
    var itemPushKey: String? = null,

    @SerializedName("orderTime")
    var orderTime: Long = System.currentTimeMillis()
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readValue(Long::class.java.classLoader) as? Long,
        userUid = parcel.readString(),
        userName = parcel.readString(),
        items = mutableListOf<OrderItem>().apply {
            parcel.readList(this as List<*>, OrderItem::class.java.classLoader)
        },
        address = parcel.readString(),
        totalPrice = parcel.readValue(Double::class.java.classLoader) as? Double,
        phoneNumber = parcel.readString(),
        orderAccepted = parcel.readByte() != 0.toByte(),
        paymentReceived = parcel.readByte() != 0.toByte(),
        itemPushKey = parcel.readString(),
        orderTime = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeList(items)
        parcel.writeString(address)
        parcel.writeValue(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(orderTime)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails = OrderDetails(parcel)
        override fun newArray(size: Int): Array<OrderDetails?> = arrayOfNulls(size)
    }
}
