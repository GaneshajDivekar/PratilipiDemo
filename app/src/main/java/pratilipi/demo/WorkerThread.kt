package pratilipi.demo

import android.content.Context
import android.util.Log
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.viewmodel.MainViewModel


class WorkerThread(
    var mViewModel: MainViewModel,
    var context: Context,
    var customerlist: ArrayList<CustomerListEntity>,
    var i: Int
) : Runnable {

   // var itemHeaderListEntityList = ArrayList<ItemHeaderListEntity>()
    override fun run() {
       var itemHeaderList = customerlist

       if (itemHeaderList.size>0) {
           mViewModel.insertPost(itemHeaderList)
       }
        Log.w("thread start", "" + i)

        }
}
