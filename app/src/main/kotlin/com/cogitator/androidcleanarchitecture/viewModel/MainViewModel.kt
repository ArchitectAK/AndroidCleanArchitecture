package com.cogitator.androidcleanarchitecture.viewModel

import android.app.Application
import com.cogitator.androidcleanarchitecture.base.BaseViewModel
import javax.inject.Inject

/**
 * @author Ankit Kumar on 08/10/2018
 */
class MainViewModel @Inject constructor(app: Application)
    : BaseViewModel(app), AccountRepository.AccountCallback {
}