package com.example.fluke.mvvmeiei

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkClass
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProjectListViewModel
    private lateinit var repo: ProjectRepository

    private val mockData = Project("0", "fluke", "www.google.com", "kotlin", "0")
    private val mockList1: MutableList<Project> = arrayListOf()
    private val mutableLiveData: MutableLiveData<MutableList<Project>>? = null

    @Before
    fun setUp() {
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

        MockitoAnnotations.initMocks(this)
        MockKAnnotations.init(this)
        viewModel = ProjectListViewModel()
        repo = mockkClass(ProjectRepository::class)
    }

    @Test
    fun testProjectListViewModel() {
        mockList1.add(mockData)
        mutableLiveData?.value = mockList1

        every { repo.getProjectList("flukebevil", viewModel) } answers {
            viewModel.onSuccess(mockList1)
        }

        viewModel.getListObservable()
        repo.getProjectList("flukebevil", viewModel)

        viewModel.liveData.observeForever {
            Assert.assertEquals(it, mockList1)
        }
    }
}