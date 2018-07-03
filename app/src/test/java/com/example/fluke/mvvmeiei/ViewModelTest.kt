package com.example.fluke.mvvmeiei

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.ui.MainActivity
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ViewModelTest {
    @Mock
    private val viewModel = mockkClass(ProjectListViewModel::class)
    private val mockView = mockkClass(MainActivity::class)
    private val rePO = mockkClass(ProjectRepository::class)

    private val mockData = Project("0", "fluke", "www.google.com", "kotlin", "0")
    private val mockList1: MutableList<Project> = arrayListOf()
    private val mutableLiveData: MutableLiveData<MutableList<Project>>? = null

    @Before
    fun setUp() {
        mockList1.add(mockData)
        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Test
    fun testProjectListViewModel() {
        mutableLiveData?.value = mockList1

        every { mockView.observeViewModel(viewModel) }
            .answers {
                viewModel.getListObservable()
            }

        every { viewModel.getListObservable() }
            .answers {
                mutableLiveData
            }

        mockView.observeViewModel(viewModel)
        verify { viewModel.getListObservable() }
        Assert.assertEquals(viewModel.getListObservable(), mutableLiveData)
    }
}