package com.example.fluke.mvvmeiei

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.GithubService
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import io.reactivex.Observable
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
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ApiTest {
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @InjectMocks
    val githubService = mock(GithubService::class.java)

    var repository = ProjectRepository()

    private val latch = CountDownLatch(1)
    var dataList: Call<List<Project>>? = null

    var dataMutableList: MutableLiveData<List<Project>> = MutableLiveData()

    var mockCallBack = mock(ProjectRepository.CallBackListener::class.java)

    val viewModel: ProjectListViewModel = ProjectListViewModel(this)

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
    }

    @Test
    fun apiSuccessResponseTest() {
        val response: List<Project> = arrayListOf()
        Mockito.`when`(githubService.getProjectList("flukebevil")).thenReturn(
            Observable.just(Response.success(response))
        )
        dataMutableList.value = response
        Mockito.verify(mockCallBack)
    }

    @Test
    fun apiTestSuccess() {
        val response: List<Project> = arrayListOf()
        Observable.just(Response.success(response)).test().assertComplete()
    }

    @Test
    fun testInstant() {
        Assert.assertNotNull(repository)
    }

    @Test
    @Throws(Exception::class)
    fun fetchOnTime() {
        val response: List<Project> = arrayListOf()
        `when`(githubService.getProjectList("flukebevil")).thenReturn(
            Observable.create<Response<List<Project>>> { subscriber ->
                try {
                    Thread.sleep(4900)
                    subscriber.onNext(Response.success(response))
                    subscriber.onComplete()
                } catch (e: InterruptedException) {
                    subscriber.onError(e)
                }
            }
        )
    }
}