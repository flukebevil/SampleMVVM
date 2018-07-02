package com.example.fluke.mvvmeiei.service

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.fluke.mvvmeiei.BuildConfig
import com.example.fluke.mvvmeiei.model.Project
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectRepository {
    private var githubService = ApiManager.serviceWTF
    private var projectRepository = ApiManager.repo

    init {
        val serviceWTF: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
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

    fun getProjectList(projectId: String, callback: CallBackListener) {
        val mutableLiveData: MutableLiveData<List<Project>> = MutableLiveData()
        githubService?.getProjectList(projectId)?.enqueue(object : Callback<List<Project>> {
            override fun onFailure(call: Call<List<Project>>?, t: Throwable?) {
                Log.e("error Retrofit :: ", t?.message)
            }

            override fun onResponse(call: Call<List<Project>>?, response: Response<List<Project>>?) {
                mutableLiveData.value = response?.body()
                callback.onFetchSuccess(mutableLiveData)
            }
        })
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

    interface CallBackListener {
        fun onFetchSuccess(body: MutableLiveData<List<Project>>)
    }
}