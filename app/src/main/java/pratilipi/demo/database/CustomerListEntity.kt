package pratilipi.demo.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
class CustomerListEntity() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pratilipi_unique_id")
    var dialer_unique_id: Int = 0

    @ColumnInfo(name = "customer_mobile")
    var customer_mobile: String = ""

    @ColumnInfo(name = "customer_name")
    var customer_name: String = ""

    @ColumnInfo(name = "call_status")
    var call_status: Boolean = false

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


}