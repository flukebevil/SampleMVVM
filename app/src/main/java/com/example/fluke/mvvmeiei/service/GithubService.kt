package com.example.fluke.mvvmeiei.service

import com.example.fluke.mvvmeiei.model.Project
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    companion object {
        var HTTPS_API_GITHUB_URL = "https://api.github.com/"
    }

    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String):
        Observable<Response<MutableList<Project>>>
}