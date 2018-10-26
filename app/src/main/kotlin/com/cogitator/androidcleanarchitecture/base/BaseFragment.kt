package com.cogitator.androidcleanarchitecture.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cogitator.androidcleanarchitecture.CleanAndroidApp
import dagger.android.support.AndroidSupportInjection

/**
 * @author Ankit Kumar on 01/10/2018
 */


abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : Fragment() {

    var viewModelFactory: ViewModelProvider.Factory = CleanAndroidApp.component.factory()

    var binding: DB? = null
        private set
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null

    private fun getViewModel(viewmodel: Class<VM>): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(viewmodel)
    }

    val viewModel by lazy {
        getViewModel(mViewModelClass)
    }

    abstract fun initViewModel(viewModel: VM)

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int


    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity
            activity!!.onFragmentAttached()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel(viewModel)
        super.onCreate(savedInstanceState)
        onInject()
        retainInstance = true
    }

    open fun onInject() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding?.root
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.setVariable(bindingVariable, viewModel)
        binding?.executePendingBindings()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

}