package pratilipi.demo.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
class CustomerListEntity() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pratilipi_unique_id")
    var dialer_unique_id: Int = 0
    @ColumnInfo(name = "customer_mobile")
    var customer_mobile: String = ""
    @ColumnInfo(name = "customer_name")
    var customer_name: String = ""
    @ColumnInfo(name = "call_status")
    var call_status: String = ""
    @ColumnInfo(name = "call_duration")
    var call_duration: String = ""
    @ColumnInfo(name = "online")
    var has_online: String = ""
    @ColumnInfo(name = "call_start_time")
    var call_start_time: String = ""
    @ColumnInfo(name = "call_end_time")
    var call_end_time: String = ""
    @ColumnInfo(name = "service_provider")
    var service_provider: String = ""
    @ColumnInfo(name = "call_date")
    var call_date: String = ""

    constructor(parcel: Parcel) : this() {
        dialer_unique_id = parcel.readInt()
        customer_mobile = parcel.readString().toString()
        customer_name = parcel.readString().toString()
        call_status = parcel.readString().toString()
        call_duration = parcel.readString().toString()
        has_online = parcel.readString().toString()
        call_start_time = parcel.readString().toString()
        call_end_time = parcel.readString().toString()
        service_provider = parcel.readString().toString()
        call_date = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dialer_unique_id)
        parcel.writeString(customer_mobile)
        parcel.writeString(customer_name)
        parcel.writeString(call_status)
        parcel.writeString(call_duration)
        parcel.writeString(has_online)
        parcel.writeString(call_start_time)
        parcel.writeString(call_end_time)
        parcel.writeString(service_provider)
        parcel.writeString(call_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerListEntity> {
        override fun createFromParcel(parcel: Parcel): CustomerListEntity {
            return CustomerListEntity(parcel)
        }

        override fun newArray(size: Int): Array<CustomerListEntity?> {
            return arrayOfNulls(size)
        }
    }


}