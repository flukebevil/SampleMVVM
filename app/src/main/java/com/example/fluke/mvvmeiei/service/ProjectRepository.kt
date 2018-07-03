package com.example.fluke.mvvmeiei.service

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.fluke.mvvmeiei.BuildConfig
import com.example.fluke.mvvmeiei.model.Project
import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//interface ProjectRepositoryAble {
//    fun getProjectList(projectId: String): MutableLiveData<List<Project>>?
//}

object ProjectRepository {

    private val gson = Gson()

    private fun getHttpClientNew(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME)
            .build())
        return client.build()
    }

    private fun getGithubService(): GithubService? {
        val serviceWTF: Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(GithubService.HTTPS_API_GITHUB_URL)
            .client(getHttpClientNew())
            .build()
        return serviceWTF.create(GithubService::class.java)
    }

    fun getProjectList(projectId: String): MutableLiveData<List<Project>>? {
        val mutableLiveData: MutableLiveData<List<Project>> = MutableLiveData()
        getGithubService()?.getProjectList(projectId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DisposableObserver<Response<List<Project>>?>() {
                override fun onComplete() {
                }

                override fun onNext(t: Response<List<Project>>) {
                    mutableLiveData.value = t.body()
                    Log.d("POND", "result: ${mutableLiveData.value}")
                }

                override fun onError(e: Throwable) {
                }
            })
        return mutableLiveData
    }
}