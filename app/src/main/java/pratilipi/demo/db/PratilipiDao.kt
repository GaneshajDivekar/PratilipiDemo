package pratilipi.demo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pratilipi.demo.database.CustomerListEntity

@Dao
interface PratilipiDao {

    @Insert
    fun insertData(customerListEntity: ArrayList<CustomerListEntity>)

    @Query("SELECT * FROM customer_table")
    fun getContact(): LiveData<List<CustomerListEntity>>


}