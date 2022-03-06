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
import com.todoist_android.databinding.FragmentBottomsheetEditTaskBinding
import com.todoist_android.ui.hideKeyboard
import com.todoist_android.ui.showKeyboard
import com.todoist_android.view.pickDate
import com.todoist_android.view.pickTime
import com.todoist_android.view.popupMenu
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

    private lateinit var todoModel: TodoModel


    companion object {
        const val TAG = "EditBottomSheet"

        fun newInstance(): BottomSheetEditTaskFragment {
            val bundle = Bundle()
            bundle.apply {
                putParcelable("data", TodoModel().getSampleTodo())
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
        Log.d("get Task",todoModel.getSampleTodo().toString())
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


        var editTitle = todoModel.getSampleTodo().title
        var editTitleTask = binding.editTextEditTitle
        editTitleTask.setText(editTitle)

        var editTask = todoModel.getSampleTodo().description
        var editDescriptionTask = binding.editTextEditTask
        editDescriptionTask.setText(editTask)

        var dueDate = todoModel.getSampleTodo().due_date
        var editDueDate = binding.tvEditDatePicker
        editDueDate.text = dueDate

        var reminder = todoModel.getSampleTodo().reminder

        var taskStatus = todoModel.getSampleTodo().status

        binding.pbEditBottomSheet.visibility = GONE

        getUserId()

        binding.editTextEditTask.showKeyboard()

        binding.ivEditReminder.setOnClickListener{
            pickTime(childFragmentManager){selectTime ->
                dueDate = "$dueDate at  $selectTime"
                editDueDate.text = dueDate
            }
        }
//        binding.ivEditFlag.setOnClickListener {view->
//            popupMenu(requireContext(),view)
//        }

        binding.tvEditDatePicker.setOnClickListener {
            pickDate(childFragmentManager){selectedText,timeInMilliseconds->
              dueDate = selectedText
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = timeInMilliseconds
                dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                editDueDate.text = dueDate

            }

        }

        binding.ivEditFlag.setOnClickListener { view->
            popupMenu(requireContext(),view){statusSelected->
                taskStatus = statusSelected

            }
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
                id=123,
                title = editedTitle,
                description = editedDescription,
                due_date = dueDate,
                reminder = reminder,
                status = taskStatus
            )

        editTask(editTasksRequest)

        }
        binding.tvCloseEditTask.setOnClickListener {
            dismiss()
        }
    }

        private fun editTask(editTasksRequest: TodoModel){
            binding.root.hideKeyboard()
            binding.pbEditBottomSheet.visibility = View.VISIBLE
            Snackbar.make(dialog?.window!!.decorView, "Editing your task...", Snackbar.LENGTH_LONG).show()
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                   viewModel.editTasks(editTasksRequest).collect{
                       when (it) {
                           is APIResource.Success -> {
                               binding.pbEditBottomSheet.visibility = GONE
                               Snackbar.make( dialog?.window!!.decorView, "Task edited successfully", Snackbar.LENGTH_SHORT )
                                   .show()
                               Log.d("task",editTasksRequest.toString())

                               viewLifecycleOwner.lifecycleScope.launch {
                                   delay( 1000 )
                                   dismiss()
                               }
                           }

                           is APIResource.Error -> {
                               binding.pbEditBottomSheet.visibility = GONE
                               Snackbar.make( dialog?.window!!.decorView, it.errorBody.toString(), Snackbar.LENGTH_SHORT )
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


    private fun getUserId(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                prefs.todoToken.collectLatest { todoId->
                    todoId?.let {
                        userId = todoId
                    } ?: kotlin.run {
                        Toast.makeText(requireActivity(), "Unable to find user id", Toast.LENGTH_SHORT).show()
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