package pratilipi.demo.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import pratilipi.demo.base.BaseActivity

abstract class BaseFragment<V : ViewModel, T : ViewDataBinding> : Fragment() {

    private var mViewDataBinding: T? = null
    private var mActivity: BaseActivity<*, *>? = null

    @LayoutRes
    abstract fun getLayoutId(): Int


    abstract fun getViewModel(): V

    abstract fun setUp(view: View, savedInstanceState: Bundle?)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //injectFeature() // Can be used with dynamic feature approach
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mViewDataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view, savedInstanceState)
    }


    fun getBaseActivity(): BaseActivity<*, *>? {
        return mActivity
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    fun getViewDataBinding(): T? {
        return mViewDataBinding
    }


}