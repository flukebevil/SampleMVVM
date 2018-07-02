package com.example.fluke.mvvmeiei.service

import android.arch.lifecycle.MutableLiveData
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

class ProjectRepository {
     var githubService = ApiManager.serviceWTF
     var projectRepository = ApiManager.repo
     val gson = Gson()

    init {
        val serviceWTF: Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(GithubService.HTTPS_API_GITHUB_URL)
            .client(setHttpClientNew())
            .build()
        githubService = serviceWTF.create(GithubService::class.java)
    }

    fun getInstance(): ProjectRepository {
        if (projectRepository == null) {
            if (projectRepository == null) {
                projectRepository = ProjectRepository()
            }
        }
        return projectRepository as ProjectRepository
    }

    fun getProjectList(projectId: String): MutableLiveData<List<Project>>? {
        val mutableLiveData: MutableLiveData<List<Project>> = MutableLiveData()
        githubService?.getProjectList(projectId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DisposableObserver<Response<List<Project>>?>() {
                override fun onComplete() {
                }

                override fun onNext(t: Response<List<Project>>) {
                    mutableLiveData.value = t.body()
                }

                override fun onError(e: Throwable) {
                }
            })
        return mutableLiveData
    }

     private fun setHttpClientNew(): OkHttpClient {
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
}