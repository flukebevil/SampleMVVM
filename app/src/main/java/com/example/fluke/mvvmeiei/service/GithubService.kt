package com.example.fluke.mvvmeiei.service

import com.example.fluke.mvvmeiei.model.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    companion object {
        var HTTPS_API_GITHUB_URL = "https://api.github.com/"
    }

    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String): Call<List<Project>>
}