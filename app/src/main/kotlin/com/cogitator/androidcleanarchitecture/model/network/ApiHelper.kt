package com.cogitator.androidcleanarchitecture.model.network

import com.cogitator.androidcleanarchitecture.model.Repo
import com.cogitator.androidcleanarchitecture.model.UserProfile
import io.reactivex.Single
import retrofit2.Response

/**
 * @author Ankit Kumar on 03/10/2018
 */
open class ApiHelper constructor(private val serviceApi: ServiceApi) {
    open fun getUser(username: String): Single<Response<UserProfile>> {
        return serviceApi.getUserProfile(username)
    }

    open fun getRepos(username: String, count: Int, page: Int): Single<Response<List<Repo>>> {
        return serviceApi.getUserAllRepository(username, count, page)
    }
}