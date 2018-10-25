package com.cogitator.androidcleanarchitecture.base

import android.annotation.SuppressLint
import android.support.annotation.CallSuper
import com.cogitator.androidcleanarchitecture.model.network.ApiHelper
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.xml.sax.ErrorHandler
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Ankit Kumar on 01/10/2018
 */

abstract class BaseRepository<LISTENER : BaseRepoCallback> constructor(
        protected val apiHelper: ApiHelper? = null,
        protected val errorHandler: ErrorHandler? = null) {

    protected var listener: LISTENER? = null

    @CallSuper
    open fun call(listener: LISTENER) {
        this.listener = listener
    }

    @SuppressLint("CheckResult")
    protected inline fun <RESPONSE : ParentResponse> executeNetworkCall(
            crossinline request: () -> Single<Response<RESPONSE>>,
            crossinline successful: (t: RESPONSE) -> Unit,
            crossinline errorful: (t: BaseResponse) -> Unit) {
        request().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.isSuccessful) {
                                it.body()?.let { it1 ->
                                    successful(it1)
                                }
                            } else {
                                if (it.errorBody() != null) {
                                    val errorResponse = Gson().fromJson(it.errorBody()?.charStream(), BaseResponse::class.java)
                                    errorResponse?.let { errorful(it) }
                                }
                            }
                        },
                        { t: Throwable ->
                            when (t) {
                                is HttpException -> {
                                    listener?.onErrorWithId(t.message!! + "")
                                }
                                is SocketTimeoutException -> {
                                    listener?.onErrorWithId("Timeout")
                                }
                                is UnknownHostException -> {
                                    listener?.onErrorWithId("Unknown Host")
                                }
                                is IOException -> {
                                    listener?.onErrorWithId(t.message!! + "")
                                }
                                else -> {
                                    listener?.onErrorWithId(t.message!! + "")
                                }
                            }
                            hideLoading()
                        }
                )
    }

    @SuppressLint("CheckResult")
    protected inline fun <RESPONSE : ParentResponse> executeNetworkCall(
            crossinline request: () -> Single<Response<RESPONSE>>) {
        request().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _, _ -> }
    }
}