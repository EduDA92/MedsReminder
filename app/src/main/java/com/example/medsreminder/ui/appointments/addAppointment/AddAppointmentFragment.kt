package com.example.medsreminder.ui.appointments.addAppointment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentAddAppointmentBinding
import com.example.medsreminder.model.Appointment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class AddAppointmentFragment : Fragment() {

    private var _binding: FragmentAddAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddAppointmentViewModel by viewModels()

    private var datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.dashboard_datepicker_title)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    private var timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(R.string.addAppointment_timepicker_title)
        .setInputMode(INPUT_MODE_KEYBOARD)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.addAppointmenTopAppBar.setupWithNavController(
            navController,
            appBarConfiguration
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addAppointmentUiState.collect { state ->
                    when (state) {
                        is AddAppointmentUiState.ErrorSavingData -> {
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT).show()
                            binding.addAppointmentSubmitAppointmentButton.visibility = View.VISIBLE
                            binding.addAppointmentLoadingCircle.visibility = View.INVISIBLE
                        }

                        is AddAppointmentUiState.SuccessfullySavedData -> navController.popBackStack()

                        AddAppointmentUiState.WaitingDataSentSuccessfully -> {
                            binding.addAppointmentSubmitAppointmentButton.visibility =
                                View.INVISIBLE
                            binding.addAppointmentLoadingCircle.visibility = View.VISIBLE
                        }

                    }

                }
            }
        }

        binding.addAppointmentSelectDateButton.setOnClickListener {
            if (!datePicker.isAdded) {
                datePicker.show(childFragmentManager, TAG)
            }

            datePicker.addOnPositiveButtonClickListener {
                binding.addAppointmentSelectedDateText.text =
                    LocalDate.ofEpochDay(Duration.ofMillis(datePicker.selection ?: 0).toDays())
                        .toString()
                datePicker.dismiss()
            }

        }



        binding.addAppointmentSelectHourButton.setOnClickListener {
            if (!timePicker.isAdded) {
                timePicker.show(childFragmentManager, TIMETAG)
            }

            timePicker.addOnPositiveButtonClickListener {
                binding.addAppointmentSelectedHourText.text = getString(
                    R.string.addAppointment_binding_hour_text,
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
                timePicker.dismiss()
            }
        }

        binding.addAppointmentReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.addAppointmentReminderDateButton.isVisible = isChecked
            binding.addAppointmentReminderDateText.isVisible = isChecked
            binding.addAppointmentReminderHourButton.isVisible = isChecked
            binding.addAppointmentReminderHourText.isVisible = isChecked
        }


        binding.addAppointmentSubmitAppointmentButton.setOnClickListener {
            if (binding.addAppointmentTypeInputLayout.editText?.text?.isEmpty() == true ||
                binding.addAppointmentLocationInputLayout.editText?.text?.isEmpty() == true ||
                binding.addAppointmentSelectedDateText.text.isEmpty() ||
                binding.addAppointmentSelectedHourText.text.isEmpty()
            ) {
                Snackbar.make(binding.root, getString(R.string.empty_field), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                Log.e("MAIN", "TEST")
                val currentAppointment = Appointment(
                    type = binding.addAppointmentTypeInputLayout.editText?.text.toString(),
                    location = binding.addAppointmentLocationInputLayout.editText?.text.toString(),
                    date = LocalDateTime.of(
                        LocalDate.parse(binding.addAppointmentSelectedDateText.text),
                        LocalTime.parse(binding.addAppointmentSelectedHourText.text)
                    ).toString()
                )

                viewModel.saveAppointment(currentAppointment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(LOADINGBUTTON, binding.addAppointmentLoadingCircle.isVisible)
        outState.putBoolean(SUBMITBUTTON, binding.addAppointmentSubmitAppointmentButton.isVisible)
        outState.putString(DATETEXT, binding.addAppointmentSelectedDateText.text.toString())
        outState.putString(HOURTEXT, binding.addAppointmentSelectedHourText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            binding.addAppointmentLoadingCircle.isVisible = savedInstanceState.getBoolean(
                LOADINGBUTTON
            )
            binding.addAppointmentSubmitAppointmentButton.isVisible = savedInstanceState.getBoolean(
                SUBMITBUTTON
            )
            binding.addAppointmentSelectedDateText.text = savedInstanceState.getString(DATETEXT)
            binding.addAppointmentSelectedHourText.text = savedInstanceState.getString(HOURTEXT)
        }
    }

    companion object {
        const val TAG = "DATEPICKER"
        const val TIMETAG = "TIMEPICKER"
        const val DATETEXT = "DATETEXT"
        const val HOURTEXT = "HOURTEXT"
        const val LOADINGBUTTON = "loadingButton"
        const val SUBMITBUTTON = "submitButton"
    }
}