package com.example.fluke.mvvmeiei

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.fluke.mvvmeiei.model.Project
import com.example.fluke.mvvmeiei.service.ProjectRepository
import com.example.fluke.mvvmeiei.view.viewmodel.ProjectListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProjectListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: ProjectListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(ProjectRepository)
        vm = ProjectListViewModel()
    }

    @Test
    fun liveItemsShouldBeNull() {
        Assert.assertNull(vm.liveItems)
    }

    @Test
    fun requestAPIShouldBeSuccess() {
        val mockData = Project("0", "fluke", "www.google.com", "kotlin", "0")
        val mockList = listOf(mockData, mockData)
        val liveData = MutableLiveData<List<Project>>()
        liveData.value = mockList
        every { ProjectRepository.getProjectList("t-jedsada") } returns liveData
        vm.getListObservable()
        vm.liveItems.observeForever { Assert.assertEquals(it, mockList)}
    }
}