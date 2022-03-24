package com.todoist_android.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.R
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.databinding.FragmentBottomsheetBinding
import com.todoist_android.ui.formartDate
import com.todoist_android.ui.hideKeyboard
import com.todoist_android.ui.pickDate
import com.todoist_android.ui.pickTime
import com.todoist_android.ui.showKeyboard
import com.todoist_android.ui.todayDate
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
class BottomSheetFragment(var addNewTaskCallback : ()->Unit ) : BottomSheetDialogFragment(), View.OnClickListener {
    @Inject
    lateinit var prefs: UserPreferences

    private lateinit var binding: FragmentBottomsheetBinding
    private val viewModel: BottomSheetViewModel by viewModels()

    private var dueDate: String? = todayDate()
    private var selectedTime: String? = null
     private var loggedInUserId: String? = null
    private var isDateClicked: Boolean? = false
    private var dateTime = " "
        set(value) {
            binding.tvDatePicker.text = value
            field = value
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.state = STATE_EXPANDED
        }
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        globalVariables()

        getLoggedInUserId()

        setOnClickListeners()

        addTaskListener()

        errorListener()

    }


    private fun setOnClickListeners() {
        binding.tvDatePicker.setOnClickListener(this)
        binding.buttonAddTask.setOnClickListener(this)
        binding.tvEndTask.setOnClickListener(this)
    }

    private fun globalVariables() {
        binding.pbBottomSheet.visibility = GONE
        binding.editTextTaskName.showKeyboard()
    }

    private fun getLoggedInUserId() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                prefs.todoToken.collectLatest { todoId ->
                    todoId?.let {
                        loggedInUserId = todoId
                    } ?: kotlin.run {
                        Toast.makeText(
                            requireActivity(), getString(R.string.unable_to_find_user_id),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.tvDatePicker -> selectDueDate()
            binding.buttonAddTask -> addNewTask()
            binding.tvEndTask -> closeAddTaskBottomSheet()
        }
    }

    private fun selectDueDate() {
        binding.root.hideKeyboard()
        pickDate(childFragmentManager) { selectedText, timeInMilliseconds ->
            dateTime = selectedText
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = timeInMilliseconds
            dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            pickTime(childFragmentManager) { selectTime ->
                // format to HH:mm:ss
                selectedTime = formartDate(selectTime, "h:mm a", "HH:mm:ss")
                dateTime = "$dateTime at  $selectTime"
                isDateClicked = true

            }
        }
    }


    private fun addNewTask() {
        if (binding.etTaskTitle.text.isNullOrEmpty()) {
            binding.etTaskTitle.error = getString(R.string.error_task_title)
            return
        }

        if (binding.editTextTaskName.text.isNullOrEmpty()) {
            binding.editTextTaskName.error = getString(R.string.error_task_description)
            return
        }

        if (isDateClicked == false){
            binding.root.hideKeyboard()
            Snackbar.make(
                dialog?.window!!.decorView,
                "Please select a due date",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }


        val description = binding.editTextTaskName.text.trim().toString()
        val title = binding.etTaskTitle.text.trim().toString()

        val taskRequest = AddTaskRequest(
            title = title,
            description = description,
            reminder=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
            dueDate = "${dueDate ?: " "} ${selectedTime ?: DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())}",
            createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
        )
        Log.d("--->",taskRequest.toString())
        addTasks(loggedInUserId!!.toInt(),taskRequest)
    }


    private fun closeAddTaskBottomSheet() {
        dismiss()
    }

    private fun addTaskListener(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.addTasksResponse.collectLatest {
                    binding.pbBottomSheet.visibility = GONE
                    Snackbar.make(
                        dialog?.window!!.decorView,
                        "Task added successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    addNewTaskCallback.invoke()
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
                    binding.pbBottomSheet.visibility = GONE
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

    private fun addTasks( id: Int,addTaskRequest: AddTaskRequest){
        binding.root.hideKeyboard()
        binding.pbBottomSheet.visibility = VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Adding your task...", Snackbar.LENGTH_LONG)
            .show()
        viewModel.addTasks(id, addTaskRequest)
    }


    override fun onDestroy() {
        binding.root.hideKeyboard()
        super.onDestroy()
    }

}