package pratilipi.demo.respository

import androidx.lifecycle.LiveData
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.db.PratilipiDao
import javax.inject.Inject

class MainRepository @Inject constructor(
     val pratilipiDao: PratilipiDao
) {

    fun insertData(customerListEntity:CustomerListEntity) {
        pratilipiDao.insertData(customerListEntity)
    }

    fun getContactData(): LiveData<List<CustomerListEntity>> {
        return pratilipiDao.getContact()
    }


    fun updateStatus(dialerUniqueId: Int, status: Boolean) {
        pratilipiDao.updateStatus(dialerUniqueId, status)
    }


}