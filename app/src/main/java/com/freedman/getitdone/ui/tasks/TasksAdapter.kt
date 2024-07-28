package com.freedman.getitdone.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freedman.getitdone.data.Task
import com.freedman.getitdone.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox

class TasksAdapter(private val listener: TaskUpdatedListener) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

        private var tasks: List<Task> = listOf()

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks (tasks: List<Task>){

       val sortedTasks = tasks.sortedBy {it.title.uppercase()}
        val furtherSort = sortedTasks.sortedBy { it.isComplete }

        this.tasks = furtherSort
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.checkBox.isChecked = task.isComplete
            binding.toggleStar.isChecked = task.isStarred
            if (task.isComplete)
            {
                binding.textViewTitle.paintFlags = binding.textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.textViewDetails.paintFlags = binding.textViewDetails.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.textViewTitle.paintFlags = 0
                binding.textViewDetails.paintFlags = 0
            }
            binding.textViewTitle.text = task.title
            binding.textViewDetails.text = task.description
            binding.checkBox.setOnClickListener {
                val updatedBoxTask = task.copy(isComplete = binding.checkBox.isChecked)
                listener.onTaskUpdated(updatedBoxTask)
            }
            binding.toggleStar.setOnClickListener {
                val updatedStarTask = task.copy(isStarred = binding.toggleStar.isChecked)
                listener.onTaskUpdated(updatedStarTask)
            }
        }
    }

    interface TaskUpdatedListener {
        fun onTaskUpdated(task: Task)
    }


}