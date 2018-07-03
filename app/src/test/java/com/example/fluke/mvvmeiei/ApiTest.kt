package com.example.fluke.mvvmeiei

import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.GithubService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import retrofit2.Response
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ApiTest {
    @Mock
    private val githubService = mock(GithubService::class.java)

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
    }

    @Test
    fun apiTestSuccess() {
        val response: List<Project> = arrayListOf()
        Observable.just(Response.success(response))
            .test()
            .assertComplete()
    }

    @Test
    fun checkApiReturnValueWhenHttpCode200() {
        val mockData = Project("0", "fluke", "www.google.com", "kotlin", "0")
        val mockList1: List<Project> = listOf(mockData, mockData)

        Mockito.`when`(githubService.getProjectList("flukebevil")).thenReturn(
            Observable.just(Response.success(mockList1))
        )

        githubService.getProjectList("flukebevil")
        Mockito.verify(githubService).getProjectList("flukebevil")

        githubService.getProjectList("flukebevil")
            .test()
            .assertNoErrors()
            .assertValue { t: Response<List<Project>> ->
                t.body() == mockList1
            }
    }
}