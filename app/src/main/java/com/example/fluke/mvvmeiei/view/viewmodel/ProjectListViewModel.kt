package com.example.fluke.mvvmeiei.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository

class ProjectListViewModel : ViewModel() {

    var liveItems= MutableLiveData<List<Project>>()

    fun getListObservable() {
        val items = ProjectRepository.getProjectList("t-jedsada")
        liveItems.value = items?.value
    }

    override fun onCleared() {
        super.onCleared()
        liveItems.value = null
    }
}