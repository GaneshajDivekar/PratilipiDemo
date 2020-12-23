package pratilipi.demo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.respository.MainRepository


class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {


  fun insertPost(customerListEntity: ArrayList<CustomerListEntity>) {
        mainRepository.insertData(customerListEntity)
    }

  fun getContact():LiveData<List<CustomerListEntity>>  {
    return mainRepository.getContactData()
  }

}