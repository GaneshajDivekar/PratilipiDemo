package pratilipi.demo.interfaces

import pratilipi.demo.database.CustomerListEntity


interface ItemClick {
    fun onClick(customerListEntity: CustomerListEntity)
}