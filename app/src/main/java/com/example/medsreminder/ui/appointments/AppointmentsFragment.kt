package com.example.medsreminder.ui.appointments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentAppointmentsBinding
import com.example.medsreminder.extensions.showDatepicker
import com.example.medsreminder.extensions.toDate
import com.example.medsreminder.ui.alarm.AlarmScheduler
import com.example.medsreminder.ui.alarm.items.AlarmAppointmentItem
import com.example.medsreminder.ui.appointments.addAppointment.AddAppointmentFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class AppointmentsFragment : Fragment() {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppointmentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppointmentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmScheduler = AlarmScheduler(requireContext())

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboard, R.id.calendar, R.id.appointments, R.id.user
            )
        )
        binding.appointmentsTopAppBar.setupWithNavController(navController, appBarConfiguration)

        /* RV setup */
        val rvAdapter = AppointmentsAdapter()
        val swipeHelper =
            ItemTouchHelper(AppointmentSwipeCallback(rvAdapter, resources) { appointment ->
                viewModel.deleteAppointment(appointment)
                alarmScheduler.cancelAppointmentAlarm(
                    AlarmAppointmentItem(
                        time = LocalDateTime.parse(
                            appointment.date
                        ), msg = resources.getString(
                            R.string.notification_appointment_reminder,
                            appointment.type,
                            appointment.location,
                            appointment.date
                        )
                    )
                )
            })
        rvAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.appointmentsRv.apply {
            layoutManager = LinearLayoutManager(this@AppointmentsFragment.context)
            adapter = rvAdapter
            swipeHelper.attachToRecyclerView(this)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appointmentUiState.collect { state ->
                    when (state) {
                        AppointmentUiState.Loading -> {
                            //TODO()
                        }

                        is AppointmentUiState.Success -> {
                            binding.appointmentsDate.text = state.date.dayOfWeek.name
                            rvAdapter.submitList(state.appointments)
                            Log.e("LIST", state.appointments.toString())
                        }
                    }
                }
            }
        }

        binding.appointmentsSelectDateButton.setOnClickListener {

            childFragmentManager.showDatepicker(
                titleText = R.string.dashboard_datepicker_title,
                selection = MaterialDatePicker.todayInUtcMilliseconds(),
                tag = AddAppointmentFragment.TAG
            ) { selection ->
                selection?.let {
                    viewModel.updateDate(it.toDate())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}