package com.cogitator.androidcleanarchitecture.model.network

import com.cogitator.androidcleanarchitecture.model.Repo
import com.cogitator.androidcleanarchitecture.model.UserProfile
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Ankit Kumar on 03/10/2018
 */
interface ServiceApi {

    @GET("/users/{user}")
    fun getUserProfile(@Path("user") user: String): Single<Response<UserProfile>>


    @GET("/users/{user}/repos")
    fun getUserAllRepository(@Path("user") user: String,
                             @Query("per_page") count: Int,
                             @Query("page") page: Int): Single<Response<List<Repo>>>

}