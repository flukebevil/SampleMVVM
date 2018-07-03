package com.example.fluke.mvvmeiei.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.fluke.mvvmeiei.R
import com.example.fluke.mvvmeiei.databinding.ActivityMainBinding
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.view.adapter.ProjectAdapter
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var projectAdapter: ProjectAdapter? = ProjectAdapter()

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
            .get(ProjectListViewModel()::class.java)

        model.liveItems?.observe(this,
            Observer<List<Project>> { t ->
                Log.d("POND", "$t")
                projectAdapter?.setItem(t)
            }
        )

        initAdapter()
        observeViewModel(model)
    }

    private fun initAdapter() {
        projectList.adapter = projectAdapter
        projectList.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel(viewModel: ProjectListViewModel) {
        viewModel.getListObservable()
    }
}