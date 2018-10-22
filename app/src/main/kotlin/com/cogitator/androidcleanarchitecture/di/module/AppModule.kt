package com.cogitator.androidcleanarchitecture.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.cogitator.androidcleanarchitecture.CleanAndroidApp
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Ankit Kumar on 01/10/2018
 */


class AppModule(var app: CleanAndroidApp) {

    @Provides
    @Singleton
    fun provideApp(): Application = app


    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
}