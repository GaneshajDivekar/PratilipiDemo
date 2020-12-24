package pratilipi.demo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.android.gms.common.Feature
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import pratilipi.demo.database.CustomerListEntity

@Dao
interface PratilipiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(customerListEntity: CustomerListEntity)

    @Update
    fun updateStatusObject(customerListEntity: CustomerListEntity)

    @Query("SELECT * FROM customer_table")
    fun getContact(): LiveData<List<CustomerListEntity>>

    @Query("SELECT * FROM customer_table WHERE customer_mobile=:phoneNumber")
    fun getContacts(phoneNumber: String): CustomerListEntity

    @Query("Update customer_table   SET  call_status =:status  WHERE  customer_mobile =:dialerUniqueId")
    fun updateStatus(dialerUniqueId: String, status: String)

}