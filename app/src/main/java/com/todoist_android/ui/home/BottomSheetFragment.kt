package com.todoist_android.ui.home

import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.todoist_android.R
import com.todoist_android.databinding.FragmentBottomsheetBinding
import com.todoist_android.view.showKeyboard


open class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomsheetBinding
    // default date is today
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
                when(it.itemId) {
                    R.id.item_created -> {

                    }
                    R.id.item_progress -> {

                    }
                }
                true
            }
            popup.show()
        }

        binding.tvEndTask.setOnClickListener { dismiss() }
    }

    private fun datePicker(){
        // Date Picker
        val calendarConstraintBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

        val materialDateBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("SELECT DATE ")
                .setCalendarConstraints(calendarConstraintBuilder.build())

        val materialDatePicker = materialDateBuilder.build()
        materialDatePicker.show(requireActivity().supportFragmentManager, "tag")
        materialDatePicker.addOnPositiveButtonClickListener {
            // Respond to positive button click.
            dateTime = materialDatePicker.headerText
        }
    }

    private fun timePicker(){
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
        val closeKeyboard= requireContext(). getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        closeKeyboard.hideSoftInputFromWindow(binding.editTextTaskName.windowToken, 0)
        super.onDestroy()
    }

}