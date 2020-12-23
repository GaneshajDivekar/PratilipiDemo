package pratilipi.demo

import android.Manifest
import android.content.ClipData.Item
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import pratilipi.demo.adapter.ContactAdapter
import pratilipi.demo.adapter.RecyclerItemTouchHelper
import pratilipi.demo.base.BaseActivity
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.databinding.ActivityMainBinding
import pratilipi.demo.interfaces.ItemClick
import pratilipi.demo.ui.AddContactActivity
import pratilipi.demo.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


@AndroidEntryPoint
@ActivityScoped
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), ItemClick,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    var contactAdapter: ContactAdapter? = null
    var itemClick: ItemClick? = null
    private val PERMISSION_REQUEST_CODE = 200
    var displayContact = ArrayList<CustomerListEntity>() as List<CustomerListEntity>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        initView()
    }


    private fun initView() {
        itemClick = this@MainActivity

        if (checkPermission()) {
            //getAllContacts(this@MainActivity)
            getAllsContacts(this@MainActivity)

            mViewModel.getContact().observe(this, Observer {
                displayContact = it
                contactAdapter =
                    ContactAdapter(this, displayContact, itemClick as MainActivity)
                mViewBinding.rcView.setLayoutManager(LinearLayoutManager(this))
                mViewBinding.rcView.setAdapter(contactAdapter)
            })
        } else {
            requestPermission()
        }
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mViewBinding.rcView)
        mViewBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddContactActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllsContacts(mainActivity: MainActivity) {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))


            val contactModel = CustomerListEntity()
            contactModel.customer_name = (name)
            contactModel.customer_mobile = (phoneNumber)
            mViewModel.insertPost(contactModel)


            Log.e("name>>", name + "  " + phoneNumber)
        }
        phones.close()
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
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_CALL_LOG,
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
                val writeExternalAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
                val callPhoneAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED
                val readCallLogAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED
                val writeCallLogAccepted = grantResults[5] == PackageManager.PERMISSION_GRANTED
                val writeReadPhoneStateAccepted =
                    grantResults[6] == PackageManager.PERMISSION_GRANTED

                if (writeContactAccepted && readContactAccepted) {
                    getAllsContacts(this@MainActivity)
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


    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onClick(customerListEntity: CustomerListEntity) {
        if (customerListEntity.call_status) {
            mViewModel.updateStatus(customerListEntity.dialer_unique_id, false)
        } else {
            mViewModel.updateStatus(customerListEntity.dialer_unique_id, true)
        }

        //contactAdapter?.updateList(displayContact)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is ContactAdapter.RecyclerViewHolder) {
            val name: String = displayContact.get(viewHolder!!.adapterPosition).customer_name
            val deletedItem: CustomerListEntity = displayContact.get(viewHolder!!.adapterPosition)
            val deletedIndex = viewHolder!!.adapterPosition
            contactAdapter?.removeItem(viewHolder!!.adapterPosition)
        }
    }


}