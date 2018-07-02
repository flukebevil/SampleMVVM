package com.example.fluke.mvvmeiei.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fluke.mvvmeiei.MainActivity
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository

class ProjectListViewModel : ViewModel() {

    private val repo = ProjectRepository()

    fun getListObservable(): MutableLiveData<List<Project>>? {
       return repo.getInstance().getProjectList("flukebevil")
    }
}