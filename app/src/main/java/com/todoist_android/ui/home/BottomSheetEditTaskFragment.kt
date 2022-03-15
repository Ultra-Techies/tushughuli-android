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
import com.todoist_android.R
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.requests.EditTaskRequest
import com.todoist_android.data.responses.TasksResponseItem
import com.todoist_android.databinding.FragmentBottomsheetEditTaskBinding
import com.todoist_android.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BottomSheetEditTaskFragment(private var refreshListCallback: () -> Unit) :
    BottomSheetDialogFragment(), View.OnClickListener {
    @Inject
    lateinit var prefs: UserPreferences

    private lateinit var binding: FragmentBottomsheetEditTaskBinding
    private val viewModel: EditTaskBottomSheetViewModel by viewModels()

    private lateinit var todoModel: TasksResponseItem
    private var selectedDueTime: String? = null
    private var dueDate: String? = null
    private var newDueDate: String? = null

    private var taskStatus = "created"
    private var userId: String? = null

    companion object {
        fun newInstance(
            item: TasksResponseItem,
            refreshListCallback: () -> Unit
        ): BottomSheetEditTaskFragment {
            val bundle = Bundle()
            bundle.apply {
                putParcelable("data", item)
            }
            return BottomSheetEditTaskFragment(refreshListCallback).apply {
                arguments = bundle
            }
        }
    }


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

        setText()

        setUpGlobalVariables()

        getLoggedInUserId()

        setOnClickListeners()

        editTaskListener()

        errorListener()

        deleteTaskListener()

        deleteErrorListener()
    }

    private fun setOnClickListeners() {
        binding.ivEditFlag.setOnClickListener(this)
        binding.tvEditDatePicker.setOnClickListener(this)
        binding.tvDeleteTask.setOnClickListener(this)
        binding.buttonEditTask.setOnClickListener(this)
        binding.tvCloseEditTask.setOnClickListener(this)
    }

    private fun editTaskListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editResponse.collectLatest {
                    binding.pbEditBottomSheet.visibility = GONE
                    refreshListCallback.invoke()
                    Snackbar.make(
                        dialog?.window!!.decorView,
                        "Task edited successfully",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()

                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1000)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun errorListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorResponse.collectLatest { message ->
                    binding.pbEditBottomSheet.visibility = GONE
                    Snackbar.make(
                        dialog?.window!!.decorView,
                        message,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun editTask(id: Int, editTasksRequest: EditTaskRequest) {
        binding.root.hideKeyboard()
        binding.pbEditBottomSheet.visibility = View.VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Editing your task...", Snackbar.LENGTH_LONG)
            .show()

        binding.pbEditBottomSheet.visibility = View.VISIBLE
        viewModel.editTasks(id, editTasksRequest)
    }

    private fun deleteTaskListener(){
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.deleteResponse.collectLatest {
                    refreshListCallback.invoke()
                    Snackbar.make(
                        dialog?.window!!.decorView,
                        getString(R.string.task_deleted),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1000)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun deleteErrorListener(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.deleteErrorResponse.collectLatest {message ->
                    binding.pbEditBottomSheet.visibility = GONE
                    Snackbar.make(
                        dialog?.window!!.decorView,
                        message,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
//                    binding.root.handleApiError(APIResource.Error())
                }
            }
        }

    }

    private fun deleteTask(id: Int){
        binding.root.hideKeyboard()
        binding.pbEditBottomSheet.visibility = View.VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Deleting your task...", Snackbar.LENGTH_LONG)
            .show()
        viewModel.deleteTasks(id)
    }


    private fun setText() {
        todoModel.apply {
            title?.let {
                binding.editTextEditTitle.setText(it)
            }

            description?.let {
                binding.editTextEditTask.setText(it)
            }

            dueDate?.let {
                this@BottomSheetEditTaskFragment.dueDate = dueDate.toString()
                val formatDueDate = formartDate(it, "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy h:mm a")
                binding.tvEditDatePicker.text = formatDueDate
            }
            reminder?.let {
                status?.let {
                    taskStatus = it
                }
            }

        }
    }

    private fun setUpGlobalVariables() {
        binding.pbEditBottomSheet.visibility = GONE
        binding.editTextEditTask.showKeyboard()

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
                            getString(R.string.unable_to_find_user_id),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }


    override fun onClick(view: View?) {
        when (view) {
            binding.buttonEditTask -> submitEditedTask()
            binding.ivEditFlag -> selectNewTaskStatus()
            binding.tvEditDatePicker -> selectNewDueDate()
            binding.tvDeleteTask -> deleteTask(todoModel.id!!)
            binding.tvCloseEditTask -> closeBottomSheet()
        }

    }


    private fun submitEditedTask() {
        // validate
        if (binding.editTextEditTitle.text.isNullOrEmpty()) {
            binding.editTextEditTask.error = getString(R.string.error_task_title)
            return
        }

        if (binding.editTextEditTask.text.isNullOrEmpty()) {
            binding.editTextEditTitle.error = getString(R.string.error_task_description)
            return
        }

        val editTasksRequest = EditTaskRequest(
            title = binding.editTextEditTitle.text.trim().toString(),
            description = binding.editTextEditTask.text.trim().toString(),
            dueDate = dueDate.toString(),
            status = taskStatus,
            reminder = dueDate.toString(),
            createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),

        )

        Log.d("edit task", editTasksRequest.toString())
        editTask( id = todoModel.id!!,editTasksRequest)
    }

    private fun selectNewTaskStatus() {
        popupMenuTwo(requireContext(), binding.tvDeleteTask) { statusSelected ->
            taskStatus = statusSelected

        }
    }

    private fun selectNewDueDate() {
        binding.root.hideKeyboard()

        pickDate(childFragmentManager) { selectedText, timeInMilliseconds ->
            newDueDate = selectedText
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = timeInMilliseconds
            newDueDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)


            pickTime(childFragmentManager) { selectTime ->
                selectedDueTime = formartDate(selectTime, "h:mm a", "HH:mm:ss")
                binding.tvEditDatePicker.text = formartDate(
                    "$newDueDate $selectedDueTime",
                    "yyyy/MM/dd HH:mm:ss",
                    "dd/MM/yyyy h:mm a"
                )
                binding.tvEditDatePicker.text = "$newDueDate $selectedDueTime"
               dueDate= "$newDueDate $selectedDueTime"
            }


        }

    }


    private fun closeBottomSheet() {
        dismiss()
    }

    override fun onDestroy() {
        binding.root.hideKeyboard()
        super.onDestroy()
    }

}