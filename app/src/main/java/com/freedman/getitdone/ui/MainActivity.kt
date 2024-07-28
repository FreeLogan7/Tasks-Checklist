package com.freedman.getitdone.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.freedman.getitdone.data.GetItDoneDatabase
import com.freedman.getitdone.data.Task
import com.freedman.getitdone.data.TaskDao
import com.freedman.getitdone.databinding.ActivityMainBinding
import com.freedman.getitdone.databinding.DialogAddTaskBinding
import com.freedman.getitdone.ui.tasks.TasksFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: GetItDoneDatabase
    private val taskDao: TaskDao by lazy { database.getTaskDao() }
    private val tasksFragment: TasksFragment = TasksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter = PagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, i ->
            tab.text = "Tasks"
        }.attach()

        binding.fab.setOnClickListener { showAddTaskDialog() }
        database = GetItDoneDatabase.getDatabase(this)




        // val database = GetItDoneDatabase.getDatabase()

    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.buttonShowDetails.setOnClickListener {
            dialogBinding.editTextTaskDetails.visibility =
                if (dialogBinding.editTextTaskDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        dialogBinding.buttonSave.setOnClickListener {
            val task = Task(
                title = dialogBinding.editTextTaskTitle.text.toString(),
                description = dialogBinding.editTextTaskDetails.text.toString()
            )
            thread {
                taskDao.createTask(task)
            }
            dialog.dismiss()
            tasksFragment.fetchAllTasks()
        }
        dialog.show()
    }


    inner class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 1

        override fun createFragment(position: Int): Fragment {
            return tasksFragment
        }

    }

}