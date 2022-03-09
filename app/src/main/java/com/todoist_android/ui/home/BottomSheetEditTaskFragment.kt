package com.todoist_android.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.data.models.TodoModel
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.databinding.FragmentBottomsheetEditTaskBinding
import com.todoist_android.ui.formartDate
import com.todoist_android.ui.hideKeyboard
import com.todoist_android.ui.pickDate
import com.todoist_android.ui.pickTime
import com.todoist_android.ui.popupMenu
import com.todoist_android.ui.popupMenuTwo
import com.todoist_android.ui.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BottomSheetEditTaskFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var prefs: UserPreferences

    private lateinit var todoModel: TasksResponseItem
    private var selectedReminderDate: String? = null
    private var selectedDueTime: String? = null
    private var selectedReminderTime: String? = null
    private var dueDate: String? = null
    private var newDueDate: String? = null
    private var taskReminder: String? = null
    private var taskStatus = "created"


    companion object {
        const val TAG = "EditBottomSheet"

        fun newInstance(item: TasksResponseItem): BottomSheetEditTaskFragment {
            val bundle = Bundle()
            bundle.apply {
                putParcelable("data", item)
            }
            return BottomSheetEditTaskFragment().apply {
                arguments = bundle
            }
        }
    }

    private val viewModel: EditTaskBottomSheetViewModel by viewModels()
    private lateinit var binding: FragmentBottomsheetEditTaskBinding
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoModel = arguments?.getParcelable("data")!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetEditTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoModel.apply {
            title?.let {
                binding.editTextEditTitle.setText( it )
            }

            description?.let {
                binding.editTextEditTask.setText(it)
            }

            due_date?.let {
                dueDate = formartDate(it, "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy h:mm a")
                binding.tvEditDatePicker.text = dueDate
            }

            reminder?.let {
                taskReminder = formartDate(it, "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy h:mm a")
            }

            status?.let {
                taskStatus = it
            }
        }


        var editedDescription = binding.editTextEditTask.text.trim().toString()
        var editedTitle = binding.editTextEditTitle.text.trim().toString()

        binding.pbEditBottomSheet.visibility = GONE

        getLoggedInUserId()

        binding.editTextEditTask.showKeyboard()

        binding.ivEditReminder.setOnClickListener {
            pickDate(childFragmentManager) { selectedText, timeInMilliseconds ->
                selectedReminderDate = selectedText
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = timeInMilliseconds
                selectedReminderDate =
                    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)

                pickTime(childFragmentManager) { selectTime ->

                    selectedReminderTime = formartDate(selectTime, "h:mm a", "HH:mm:ss")
                    Log.d("selectedReminderDate", selectedReminderTime.toString())
                    taskReminder = "$selectedReminderDate $selectedReminderTime"
                }

            }
        }
        binding.ivEditFlag.setOnClickListener { view ->
            popupMenu(requireContext(), view) { statusSelected ->
                taskStatus = statusSelected
            }
        }

        binding.tvEditDatePicker.setOnClickListener {
            pickDate(childFragmentManager) { selectedText, timeInMilliseconds ->
                newDueDate = selectedText
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = timeInMilliseconds
                newDueDate =
                    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)


                pickTime(childFragmentManager) { selectTime ->
                    selectedDueTime = formartDate(selectTime, "h:mm a", "HH:mm:ss")
                    binding.tvEditDatePicker.text = "$newDueDate $selectedDueTime"
                    dueDate = "$newDueDate $selectedDueTime"
                }


            }

        }

        binding.ivEditFlag.setOnClickListener { view ->
            popupMenuTwo(requireContext(), view) { statusSelected ->
                taskStatus = statusSelected

            }
        }

        binding.tvDeleteTask.setOnClickListener {
            val deleteTaskRequest = TodoModel(
                id = todoModel.id,
                title = editedTitle,
                description = editedDescription,
                due_date = "${dueDate ?: " "}",
                reminder = "${taskReminder ?: " "}",
                status = taskStatus
            )

            deleteTask(deleteTaskRequest)
        }

        binding.buttonEditTask.setOnClickListener {
            // validate
            if (binding.editTextEditTitle.text.isNullOrEmpty()) {
                binding.editTextEditTask.error = "Please enter a Task Title"
                return@setOnClickListener
            }

            if (binding.editTextEditTask.text.isNullOrEmpty()) {
                binding.editTextEditTitle.error = "Please enter a Task"
                return@setOnClickListener
            }
            var editedDescription = binding.editTextEditTask.text.trim().toString()
            var editedTitle = binding.editTextEditTitle.text.trim().toString()

            val editTasksRequest = TodoModel(
                id = todoModel.id,
                title = editedTitle,
                description = editedDescription,
                due_date = dueDate,
                reminder = taskReminder,
                status = taskStatus
            )

            editTask(editTasksRequest)

        }
        binding.tvCloseEditTask.setOnClickListener {
            dismiss()
        }
    }

    private fun editTask(editTasksRequest: TodoModel) {
        binding.root.hideKeyboard()
        binding.pbEditBottomSheet.visibility = View.VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Editing your task...", Snackbar.LENGTH_LONG)
            .show()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editTasks(editTasksRequest).collect {
                    when (it) {
                        is APIResource.Success -> {
                            binding.pbEditBottomSheet.visibility = GONE
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                "Task edited successfully",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                            Log.d("task", editTasksRequest.toString())

                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(1000)
                                dismiss()
                            }
                        }

                        is APIResource.Error -> {
                            binding.pbEditBottomSheet.visibility = GONE
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                it.errorBody.toString(),
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        is APIResource.Loading -> {
                            binding.pbEditBottomSheet.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }


    }

    private fun deleteTask(deleteTaskRequest: TodoModel) {
        binding.root.hideKeyboard()
        binding.pbEditBottomSheet.visibility = View.VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Deleting your task...", Snackbar.LENGTH_LONG)
            .show()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deleteTasks(deleteTaskRequest).collect {
                    when (it) {
                        is APIResource.Success -> {
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                "Task deleted successfully",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                            Log.d("task", deleteTaskRequest.toString())
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(1000)
                                dismiss()
                            }
                        }
                        is APIResource.Error -> {
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                it.errorBody.toString(),
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        is APIResource.Loading -> {

                        }
                    }


                }
            }
        }

    }


    private fun getLoggedInUserId() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                prefs.todoToken.collectLatest { todoId ->
                    todoId?.let {
                        userId = todoId
                    } ?: kotlin.run {
                        Toast.makeText(
                            requireActivity(),
                            "Unable to find user id",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        binding.root.hideKeyboard()
        super.onDestroy()
    }

}