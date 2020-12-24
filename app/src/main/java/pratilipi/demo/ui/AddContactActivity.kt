package pratilipi.demo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import pratilipi.demo.MainActivity
import pratilipi.demo.R
import pratilipi.demo.base.BaseActivity
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.databinding.ActivityAddContactBinding
import pratilipi.demo.databinding.ActivityMainBinding
import pratilipi.demo.viewmodel.MainViewModel
import java.io.File


@AndroidEntryPoint
@ActivityScoped
class AddContactActivity : BaseActivity<MainViewModel, ActivityAddContactBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        initView()
    }

    private fun initView() {
        mViewBinding.btnSubmit.setOnClickListener {
            if (mViewBinding.username.text.isNullOrEmpty()) {
                Toast.makeText(this,"Please enter customer name",Toast.LENGTH_SHORT).show()
            } else if (mViewBinding.mobile.text.isNullOrEmpty()) {
                Toast.makeText(this,"Please enter mobile number",Toast.LENGTH_SHORT).show()
            } else {
                var customerListEntity=CustomerListEntity()
                customerListEntity.customer_name=mViewBinding.username.text.toString()
                customerListEntity.customer_mobile=mViewBinding.mobile.text.toString()
                mViewModel.insertPost(customerListEntity)
                finish()
            }
        }
    }


    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityAddContactBinding =
        ActivityAddContactBinding.inflate(layoutInflater)
}