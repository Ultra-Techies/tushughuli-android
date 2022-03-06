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
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.databinding.FragmentBottomsheetBinding
import com.todoist_android.ui.hideKeyboard
import com.todoist_android.ui.showKeyboard
import com.todoist_android.view.pickDate
import com.todoist_android.view.pickTime
import com.todoist_android.view.popupMenu
import com.todoist_android.view.todayDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var prefs: UserPreferences

    private lateinit var binding: FragmentBottomsheetBinding
    private val viewModel: BottomSheetViewModel by viewModels()
    private var dueDate: String? = todayDate()
    private var userId: String? = null
    private var status = "created"

    private var dateTime = "Today"
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

        binding.pbBottomSheet.visibility = GONE

        getUserId()
        binding.editTextTaskName.showKeyboard()


        binding.tvDatePicker.setOnClickListener {
            pickDate(childFragmentManager) { selectedText, timeInMilliseconds ->
                dateTime = selectedText

                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = timeInMilliseconds
                dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
            }
        }

        binding.ivReminder.setOnClickListener {
            pickTime(childFragmentManager) { selectTime ->
                dateTime = "$dateTime at  $selectTime"
            }
        }
        binding.ivFlag.setOnClickListener { view ->
            popupMenu(requireContext(), view) { statusSelected ->
                status = statusSelected
            }
        }

        binding.buttonAddTask.setOnClickListener {
            if (binding.editTextTaskTitle.text.isNullOrEmpty()) {
                binding.editTextTaskTitle.error = "Please enter a Task Title"
                return@setOnClickListener
            }

            if (binding.editTextTaskName.text.isNullOrEmpty()) {
                binding.editTextTaskName.error = "Please enter a Task"
                return@setOnClickListener
            }

            val description = binding.editTextTaskName.text.trim().toString()
            val title = binding.editTextTaskTitle.text.trim().toString()

            val taskRequest = AddTaskRequest(
                id = userId,
                title = title,
                description = description,
                status = status,
                due_date = dueDate.toString(),
            )

            addTasks(taskRequest)


        }

        binding.tvEndTask.setOnClickListener { dismiss() }
    }

    private fun getUserId() {
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

    private fun addTasks(taskRequest: AddTaskRequest) {
        binding.root.hideKeyboard()
        binding.pbBottomSheet.visibility = VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Adding your task...", Snackbar.LENGTH_LONG)
            .show()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addTasks(taskRequest).collect {
                    when (it) {
                        is APIResource.Success -> {
                            binding.pbBottomSheet.visibility = GONE
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                "Task added successfully",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                            Log.d("task", taskRequest.toString())

                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(1000)
                                dismiss()
                            }
                        }

                        is APIResource.Error -> {
                            binding.pbBottomSheet.visibility = GONE
                            Snackbar.make(
                                dialog?.window!!.decorView,
                                it.errorBody.toString(),
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        is APIResource.Loading -> {
                            binding.pbBottomSheet.visibility = VISIBLE
                        }
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