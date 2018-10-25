package com.cogitator.androidcleanarchitecture.base

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context

/**
 * @author Ankit Kumar on 01/10/2018
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Any

    protected fun getAppContext(): Context = getApplication<Application>().applicationContext

    protected fun <T> setObserverValue(observer: MutableLiveData<T>, data: T) {
        observer.value = data
    }
}