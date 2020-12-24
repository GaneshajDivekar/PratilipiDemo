package pratilipi.demo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.respository.MainRepository


class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {


  fun insertPost(customerListEntity: CustomerListEntity) {
        mainRepository.insertData(customerListEntity)
    }

  fun getContact():LiveData<List<CustomerListEntity>>  {
    return mainRepository.getContactData()
  }


  fun updateStatus(dialerUniqueId: String, status: String)
  {
    return mainRepository.updateStatus(dialerUniqueId,status)
  }
  fun updateStatusObject(customerListEntity: CustomerListEntity)
  {
    return mainRepository.updateStatusObject(customerListEntity)
  }
}