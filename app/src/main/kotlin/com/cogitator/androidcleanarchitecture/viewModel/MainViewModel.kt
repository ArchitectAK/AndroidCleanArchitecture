package com.cogitator.androidcleanarchitecture.viewModel

import android.app.Application
import javax.inject.Inject

/**
 * @author Ankit Kumar on 08/10/2018
 */
class MainViewModel  @Inject constructor(app: Application)
    : BaseV(app), AccountRepository.AccountCallback {
