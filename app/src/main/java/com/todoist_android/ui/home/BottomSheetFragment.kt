package com.todoist_android.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.UserPreferences
import com.todoist_android.data.requests.AddTaskRequest
import com.todoist_android.databinding.FragmentBottomsheetBinding
import com.todoist_android.view.hideKeyboard
import com.todoist_android.view.showKeyboard
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
    @Inject lateinit var prefs: UserPreferences

    private lateinit var binding: FragmentBottomsheetBinding
    private val viewModel: BottomSheetViewModel by viewModels()
    private var dueDate: String? = todayDate()
    private var userId : String? = null

    private var taskStatus = "created"
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
            datePicker()
        }
        binding.ivReminder.setOnClickListener {
            timePicker()
        }
        binding.ivFlag.setOnClickListener { it ->
            val popup = PopupMenu(requireContext(), it)
            popup.inflate(R.menu.set_status_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_created -> {
                        taskStatus = "created"
                    }
                    R.id.item_progress -> {
                        taskStatus = "progress"
                    }
                }
                true
            }
            popup.show()
        }

        binding.buttonAddTask.setOnClickListener {

            if (binding.editTextTaskName.text.isNullOrEmpty()) {
                binding.editTextTaskName.error = "Please enter a Task"
                return@setOnClickListener
            }

            val description = binding.editTextTaskName.text.trim().toString()

            val taskRequest = AddTaskRequest(
                id = userId,
                title = description,
                description = description,
                status = taskStatus,
                due_date = dueDate.toString(),
            )

            addTasks(taskRequest)


        }

        binding.tvEndTask.setOnClickListener { dismiss() }
    }

    private fun getUserId() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                prefs.todoToken.collectLatest {  todoId ->
                    todoId?.let {
                        userId = todoId
                    } ?: kotlin.run {
                        Toast.makeText(requireActivity(), "Unable to find user id", Toast.LENGTH_SHORT).show()
                    }
                }
            }
         }
    }

    private fun addTasks(taskRequest: AddTaskRequest) {
        binding.root.hideKeyboard()
        binding.pbBottomSheet.visibility = VISIBLE
        Snackbar.make(dialog?.window!!.decorView, "Adding your task...", Snackbar.LENGTH_LONG).show()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addTasks(taskRequest).collect {
                    when (it) {
                        is APIResource.Success -> {
                            binding.pbBottomSheet.visibility = GONE
                           Snackbar.make( dialog?.window!!.decorView, "Task added successfully", Snackbar.LENGTH_SHORT )
                               .show()

                            viewLifecycleOwner.lifecycleScope.launch {
                                delay( 1000 )
                                dismiss()
                            }
                        }

                        is APIResource.Error -> {
                            binding.pbBottomSheet.visibility = GONE
                            Snackbar.make( dialog?.window!!.decorView, it.errorBody.toString(), Snackbar.LENGTH_SHORT )
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

    private fun datePicker() {
        // Date Picker
        val calendarConstraintBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

        val materialDateBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("SELECT DATE ")
                .setCalendarConstraints(calendarConstraintBuilder.build())

        val materialDatePicker = materialDateBuilder.build()
        materialDatePicker.show(requireActivity().supportFragmentManager, "tag")
        materialDatePicker.addOnPositiveButtonClickListener { timeInMilliseconds ->
            // Respond to positive button click.
            dateTime = materialDatePicker.headerText

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = timeInMilliseconds
            dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }
    }

    private fun timePicker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTitleText("Select Appointment time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .build()
        picker.show(requireActivity().supportFragmentManager, "tag")
        picker.addOnPositiveButtonClickListener {
            val pickedHour: Int = picker.hour
            val pickedMinute: Int = picker.minute

            val formattedTime: String = when {
                pickedHour > 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour - 12}:0${picker.minute} pm"
                    } else {
                        "${picker.hour - 12}:${picker.minute} pm"
                    }
                }
                pickedHour == 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} pm"
                    } else {
                        "${picker.hour}:${picker.minute} pm"
                    }
                }
                pickedHour == 0 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour + 12}:0${picker.minute} am"
                    } else {
                        "${picker.hour + 12}:${picker.minute} am"
                    }
                }
                else -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} am"
                    } else {
                        "${picker.hour}:${picker.minute} am"
                    }
                }
            }

            dateTime = "$dateTime at $formattedTime"

        }
    }

    override fun onDestroy() {
        binding.root.hideKeyboard()
        super.onDestroy()
    }

}