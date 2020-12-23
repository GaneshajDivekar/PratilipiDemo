package pratilipi.demo

import android.Manifest
import android.app.Application
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import pratilipi.demo.base.BaseActivity
import pratilipi.demo.databinding.ActivityMainBinding
import pratilipi.demo.viewmodel.MainViewModel
import java.text.SimpleDateFormat

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import pratilipi.demo.adapter.ContactAdapter
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.interfaces.ItemClick
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


@AndroidEntryPoint
@ActivityScoped
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), ItemClick {

    var contactAdapter: ContactAdapter? = null
    var itemClick: ItemClick? = null
    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        initView()
    }

    private fun initView() {
        if (checkPermission()) {
            itemClick = this
            getAllContacts(this@MainActivity)
            mViewModel.getContact().observe(this, Observer {
                contactAdapter =
                    ContactAdapter(this, it, itemClick as MainActivity)
                mViewBinding.rcView.setLayoutManager(LinearLayoutManager(this))
                mViewBinding.rcView.setAdapter(contactAdapter)
            })
        } else {
            requestPermission()
        }
    }


    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_CONTACTS
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_CONTACTS
        )
        val result2 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result3 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CALL_PHONE
        )
        val result4 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_CALL_LOG
        )
        val result5 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_CALL_LOG
        )
        val result6 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_PHONE_STATE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED && result6 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val writeContactAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
               val readContactAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val writeExternalAccepted =grantResults[2]==PackageManager.PERMISSION_GRANTED
                val callPhoneAccepted =grantResults[3]==PackageManager.PERMISSION_GRANTED
                val readCallLogAccepted =grantResults[4]==PackageManager.PERMISSION_GRANTED
                val writeCallLogAccepted =grantResults[5]==PackageManager.PERMISSION_GRANTED
                val writeReadPhoneStateAccepted =grantResults[6]==PackageManager.PERMISSION_GRANTED

                if (writeContactAccepted && readContactAccepted) {
                    getAllContacts(this@MainActivity)
                    mViewModel.getContact().observe(this, Observer {
                        contactAdapter =
                            ContactAdapter(this, it, itemClick as MainActivity)
                        mViewBinding.rcView.setLayoutManager(LinearLayoutManager(this))
                        mViewBinding.rcView.setAdapter(contactAdapter)
                    })
                    Toast.makeText(
                        this,
                        "Permission Granted, Now you can access contacts",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied, You cannot access Contacts.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun getAllContacts(mainActivity: MainActivity) {
        val sb = StringBuffer()
        val managedCursor = managedQuery(
            CallLog.Calls.CONTENT_URI, null,
            null, null, CallLog.Calls.DATE + " ASC"
        )
        val number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
        val callName = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME)

        if (callName == null) {
            var callName = ("Unknown")
        } else {
            var callName = callName
        }
        sb.append("Call Log :")
        var testarray = ArrayList<CustomerListEntity>()
        while (managedCursor.moveToNext()) {
            var phNumber = managedCursor.getString(number)
            val callType = managedCursor.getString(type)
            val callDate = managedCursor.getString(date)
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm")
            val callDayTime = formatter.format(Date(callDate.toLong()))
            val callDuration = managedCursor.getString(duration)
            val name = managedCursor.getString(callName)
            var dir: String? = null
            val dircode = callType.toInt()
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "Outgoing"
                CallLog.Calls.INCOMING_TYPE -> dir = "Incoming"
                CallLog.Calls.MISSED_TYPE -> dir = "Missed"
            }
            phNumber = phNumber.replace(" ", "")
            if (!phNumber.contains("+91")) {
                phNumber = "+91" + phNumber
            }
            var contactModel = CustomerListEntity()
            contactModel.has_online = "0"
            contactModel.customer_mobile = phNumber
            contactModel.call_date = callDayTime.toString()
            val p1: Int = callDuration.toInt() % 60
            var p2: Int = callDuration.toInt() / 60
            val p3 = p2 % 60
            p2 = p2 / 60
            var calltime: String = ""
            if (p2 != 0) {
                calltime = "" + p2 + ":" + p3 + ":" + p1
            } else if (p3 != 0) {
                calltime = "" + p3 + ":" + p1
            } else {
                calltime = "" + p1
            }
            contactModel.call_duration = calltime
            contactModel.customer_name = (name ?: "Unknown").toString()
            if (dir.equals("Outgoing")) {
                contactModel.call_status = "3"
            } else if (dir.equals("Incoming")) {
                contactModel.call_status = "2"
            } else if (dir.equals("Missed")) {
                contactModel.call_status = "4"
            }

            testarray.add(contactModel)
        }

        Insertdata(testarray)


    }

    fun Insertdata(testarray: ArrayList<CustomerListEntity>) {

        val executor = Executors.newFixedThreadPool(2)
        val parts = chopped<CustomerListEntity>(
            testarray,
            1000
        )

        for (i in parts.indices) {
            val worker =
                WorkerThread(mViewModel, application, parts[i] as ArrayList<CustomerListEntity>, i)
            executor.execute(worker)
        }

        executor.shutdown()
        while (!executor.isTerminated) {
        }
    }


    internal fun <T> chopped(list: List<T>, L: Int): List<List<T>> {
        val parts = ArrayList<List<T>>()
        val N = list.size
        var i = 0
        while (i < N) {
            parts.add(
                ArrayList(
                    list.subList(i, Math.min(N, i + L))
                )
            )
            i += L
        }
        return parts
    }


    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onClick(customerListEntity: CustomerListEntity) {

    }


}