package com.example.fluke.mvvmeiei.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository

class ProjectListViewModel : ViewModel() {

    private var liveData: MutableLiveData<MutableList<Project>>? = null

    fun getListObservable(): MutableLiveData<MutableList<Project>>? {
        liveData ?: kotlin.run {
            liveData = ProjectRepository.getProjectList("flukebevil")
        }
        return liveData
    }
}