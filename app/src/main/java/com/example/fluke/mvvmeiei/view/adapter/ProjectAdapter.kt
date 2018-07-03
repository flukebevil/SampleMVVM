package com.example.fluke.mvvmeiei.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fluke.mvvmeiei.R
import com.example.fluke.mvvmeiei.databinding.ProjectListItemBinding
import com.example.fluke.mvvmeiei.model.Project
import java.util.Objects

class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    var projectListOld: List<Project>? = arrayListOf()

    fun setItem(projectList: MutableList<Project>?) {
        projectList?.let {
            projectListOld = it
            projectListOld?.let { notifyItemRangeInserted(0, it.size) }
        } ?: apply {
            val diffUtils: DiffUtil.DiffResult = DiffUtil.calculateDiff(
                object : DiffUtil.Callback() {
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                    {
                        return projectListOld?.get(oldItemPosition)?.id ==
                            projectList?.get(newItemPosition)?.id
                    }

                    override fun getOldListSize(): Int = projectListOld?.size ?: 0

                    override fun getNewListSize(): Int = projectList?.size ?: 0

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean {
                        val project = projectList?.get(newItemPosition)
                        val old = projectList?.get(oldItemPosition)
                        return project?.id === old?.id &&
                            Objects.equals(project?.git_url, old?.git_url)
                    }
                })
            projectListOld = projectList
            diffUtils.dispatchUpdatesTo(this)
        }
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.apply {
            holder.binding?.project = projectListOld?.get(position)
            holder.binding?.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        ProjectViewHolder {
        val binding: ProjectListItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.project_list_item,
                parent,
                false)
        return ProjectViewHolder(binding)
    }

    override fun getItemCount(): Int = projectListOld?.size ?: 0

    inner class ProjectViewHolder(var binding: ProjectListItemBinding? = null) :
        RecyclerView.ViewHolder(binding?.root)
}