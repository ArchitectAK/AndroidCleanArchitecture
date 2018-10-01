package com.cogitator.androidcleanarchitecture.di.component

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.SharedPreferences
import org.xml.sax.ErrorHandler

/**
 * @author Ankit Kumar on 01/10/2018
 */

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface ApplicationComponent {

    fun inject(app: MyPortfolioApp)

    fun app(): Application

    fun factory(): ViewModelProvider.Factory
    fun context(): Context

    fun preferences(): SharedPreferences
    fun errorHandler(): ErrorHandler
    fun apiHelper(): ApiHelper

}