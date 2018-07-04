package com.example.fluke.mvvmeiei.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.ui.MainActivity
import java.lang.ref.WeakReference

class ProjectListViewModel : ViewModel(), ProjectRepository.OnProjectCallBackListener {

    var repo: ProjectRepository = ProjectRepository()

    private var viewRef: WeakReference<MainActivity>? = null

    fun attachView(context: MainActivity) {
        this.viewRef = WeakReference(context)
    }

    fun getView(): MainActivity? = viewRef?.get()

    var liveData = MutableLiveData<MutableList<Project>>()

    override fun onSuccess(t: MutableList<Project>?) {
        t?.let {
            liveData.value = it
        }
        getView()?.observeViewModel(liveData)
    }

    fun getListObservable() {
        repo.getProjectList("flukebevil", this)
    }

    override fun onCleared() {
        super.onCleared()
        liveData.value = null
    }
}