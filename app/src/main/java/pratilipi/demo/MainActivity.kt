package pratilipi.demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlin.collections.ArrayList


@AndroidEntryPoint
@ActivityScoped
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), ItemClick,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    var layoutManager: RecyclerView.LayoutManager? = null
    var positions = 0
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

            layoutManager = LinearLayoutManager(this)
            mViewModel.getContact().observe(this, Observer {
                if (it.size == 0) {
                    getAllsContacts(this@MainActivity)
                } else {

                }
                displayContact = it
                contactAdapter =
                    ContactAdapter(this, displayContact, displayContact, itemClick as MainActivity)
                mViewBinding.rcView.setLayoutManager(layoutManager)
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



        mViewBinding?.searchEdittext?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val string = s.toString()
                if (contactAdapter != null) {
                    contactAdapter!!.filter.filter(string)
                }
            }
        })
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
        val result7 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ANSWER_PHONE_CALLS
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED && result6 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED
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
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ANSWER_PHONE_CALLS
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
                val answerCallPhoneStateAccepted =
                    grantResults[7] == PackageManager.PERMISSION_GRANTED

                if (writeContactAccepted && readContactAccepted) {
                    getAllsContacts(this@MainActivity)
                    mViewModel.getContact().observe(this, Observer {
                        contactAdapter =
                            ContactAdapter(this, it, it, itemClick as MainActivity)
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

    override fun onClick(customerListEntity: CustomerListEntity, pos: Int) {
        positions = pos
        if (customerListEntity.call_status.equals("0")) {
            mViewModel.updateStatus(customerListEntity.customer_mobile, "1")
        } else {
            mViewModel.updateStatus(customerListEntity.customer_mobile, "0")
        }

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