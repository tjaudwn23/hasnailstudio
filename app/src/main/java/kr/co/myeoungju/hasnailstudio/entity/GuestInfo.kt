package kr.co.myeoungju.hasnailstudio.entity

import android.os.Parcel
import android.os.Parcelable

class GuestInfo() : Parcelable {
    var name:String = ""
    var birth:String = ""
    var phone_num:String = ""
    var register_date:String = ""
    var sign_url:String = ""
    var key:String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString().toString()
        birth = parcel.readString().toString()
        phone_num = parcel.readString().toString()
        register_date = parcel.readString().toString()
        sign_url = parcel.readString().toString()
        key = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(birth)
        parcel.writeString(phone_num)
        parcel.writeString(register_date)
        parcel.writeString(sign_url)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GuestInfo> {
        override fun createFromParcel(parcel: Parcel): GuestInfo {
            return GuestInfo(parcel)
        }

        override fun newArray(size: Int): Array<GuestInfo?> {
            return arrayOfNulls(size)
        }
    }
}