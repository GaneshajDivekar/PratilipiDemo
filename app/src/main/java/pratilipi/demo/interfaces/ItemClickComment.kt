package pratilipi.demo.interfaces

import pratilipi.demo.database.CustomerListEntity


interface ItemClickComment {
    fun onClick(customerListEntity: CustomerListEntity)
}