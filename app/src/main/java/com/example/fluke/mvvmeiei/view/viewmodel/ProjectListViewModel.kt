package com.example.fluke.mvvmeiei.view.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.fluke.mvvmeiei.service.ProjectRepository

class ProjectListViewModel : ViewModel() {
    private val repo = ProjectRepository()

    fun getListObservable(callback: ProjectRepository.CallBackListener) {
        repo.getInstance().getProjectList("flukebevil", callback)
    }
}