package com.cogitator.androidcleanarchitecture

import android.app.Application
import com.cogitator.androidcleanarchitecture.di.component.ApplicationComponent
import com.cogitator.androidcleanarchitecture.di.module.AppModule
import com.cogitator.androidcleanarchitecture.di.module.NetModule

/**
 * @author Ankit Kumar on 01/10/2018
 */
class CleanAndroidApp : Application() {
    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
//        Fabric.with(this, Crashlytics())
        component = DaggerApplicationComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(this))
                .build()
        component.inject(this)
    }
}