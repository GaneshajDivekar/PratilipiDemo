package pratilipi.demo.respository

import androidx.lifecycle.LiveData
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.db.PratilipiDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val pratilipiDao: PratilipiDao
) {

    fun insertData(customerListEntity: ArrayList<CustomerListEntity>) {
        pratilipiDao.insertData(customerListEntity)
    }

    fun getContactData(): LiveData<List<CustomerListEntity>> {
        return pratilipiDao.getContact()
    }


}