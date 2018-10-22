package com.cogitator.androidcleanarchitecture.view

import com.cogitator.androidcleanarchitecture.base.BaseActivity
import com.cogitator.androidcleanarchitecture.databinding.ActivityMainBinding
import com.cogitator.androidcleanarchitecture.viewModel.MainViewModel

/**
 * @author Ankit Kumar on 04/10/2018
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(MainViewModel::class.java) {
    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel(viewModel: MainViewModel) {

    }

}