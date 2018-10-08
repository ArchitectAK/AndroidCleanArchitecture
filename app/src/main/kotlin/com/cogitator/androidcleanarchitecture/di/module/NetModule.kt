package com.cogitator.androidcleanarchitecture.di.module

import com.cogitator.androidcleanarchitecture.CleanAndroidApp
import com.cogitator.androidcleanarchitecture.model.network.ApiHelper
import com.cogitator.androidcleanarchitecture.model.network.ServiceApi
import com.cogitator.androidcleanarchitecture.utils.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Ankit Kumar on 01/10/2018
 */

@Module
class NetModule(var app: CleanAndroidApp) {
    @Provides
    @Singleton
    fun provideNetworkHelper(retrofit: Retrofit): ApiHelper {
        val platformAPI = retrofit.create(ServiceApi::class.java)
        return ApiHelper(platformAPI)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpCacheDirectory = File(app.cacheDir, "responses")

        var cache: Cache? = null
        try {
            cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(CookieJar.NO_COOKIES)
                .cache(cache)
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .build()

        return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}