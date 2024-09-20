package com.fpoly.shoes_app.framework.domain.model.profile.address

import android.os.Parcel
import android.os.Parcelable

data class AddressDetail(
    val name: String,
    val address: String,
    val lat: Double,
    val long: Double,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeDouble(lat)
        parcel.writeDouble(long)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<AddressDetail> {
        override fun createFromParcel(parcel: Parcel): AddressDetail {
            return AddressDetail(parcel)
        }

        override fun newArray(size: Int): Array<AddressDetail?> {
            return arrayOfNulls(size)
        }
    }
}