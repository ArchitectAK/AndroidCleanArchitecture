package com.cogitator.androidcleanarchitecture.di.module

import android.content.Intent
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Ankit Kumar on 01/10/2018
 */

@Module
class NetModule(var app: MyPortfolioApp) {
    @Provides
    @Singleton
    fun provideNetworkHelper(retrofit: Retrofit): ApiHelper {
        val platformAPI = retrofit.create(PlatformAPI::class.java)
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
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()

                    requestBuilder.addHeader("Accept", "application/json;")
                    if (DataHolder.getInstance().cookies != null)
                        requestBuilder.addHeader("Cookie", DataHolder.getInstance().cookies!!)

                    val token = DataHolder.getInstance().token
                    if (token != null)
                        requestBuilder.addHeader("Authorization", "bearer $token")

                    val response = chain.proceed(requestBuilder.build())
                    val rawCookie = response.headers().get("Set-Cookie")
                    if (DataHolder.getInstance().cookies == null)
                        DataHolder.getInstance().cookies = rawCookie
                    if (response.code() == 401 && !chain.request().url().encodedPath().contains(LOGIN_URL)) {
                        val loginActivity = LoginActivity()
                        loginActivity.source = "401"
                        app.applicationContext.startActivity(Intent(app.applicationContext, loginActivity::class.java))
                    }
                    response
                }
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