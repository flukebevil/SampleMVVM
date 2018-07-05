package com.example.fluke.mvvmeiei.view.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.fluke.mvvmeiei.R
import com.example.fluke.mvvmeiei.databinding.ActivityMainBinding
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.adapter.ProjectAdapter
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var projectAdapter: ProjectAdapter? = ProjectAdapter()
    private val repo: ProjectRepository = ProjectRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding? = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding?.apply {
            executePendingBindings()
        }
        val model: ProjectListViewModel = ViewModelProviders
            .of(this@MainActivity)
            .get(ProjectListViewModel(repo)::class.java)

        model.attachView(this)
        initAdapter()
        getProjectData(model)
    }

    private fun initAdapter() {
        projectList.adapter = projectAdapter
        projectList.layoutManager = LinearLayoutManager(this)
    }

    fun observeViewModel(liveData: MutableLiveData<MutableList<Project>>?) {
        liveData?.observe(
            this,
            Observer<MutableList<Project>>
            { t ->
                projectAdapter?.setItem(t)
            }
        )
    }

    private fun getProjectData(viewModel: ProjectListViewModel) {
        viewModel.getListObservable()
    }
}