package com.example.medsreminder.ui.dashboard

import android.os.Bundle
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
import com.example.medsreminder.databinding.FragmentDashboardBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    private var datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(R.string.dashboard_datepicker_title)
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboard, R.id.calendar, R.id.appointments, R.id.user
            )
        )
        binding.dashboardTopAppBar.setupWithNavController(navController, appBarConfiguration)
        // test
        val resources = resources
        /* RecyclerView setup */
        val rvAdapter = TakingsAdapter()
        val swipeHelper = ItemTouchHelper(SwipeCallback(rvAdapter, resources) { taking, medicineStatusEnum ->
            viewModel.updateTaking(
                taking,
                medicineStatusEnum
            )
        })
        binding.dashboardMedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            adapter = rvAdapter
            swipeHelper.attachToRecyclerView(this)
        }




        rvAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dashboardUiState.collect { state ->
                    when (state) {
                        DashboardUiState.Loading -> {
                            /* TODO */
                        }

                        is DashboardUiState.Success -> {
                            binding.dashboardDate.text = state.date.dayOfWeek.name
                            binding.dashboardAnalyticsCardProgressText.text = getString(
                                R.string.dashboard_card_plan_progress_text_sr,
                                state.completedTakings,
                                state.takingsNumber
                            )
                            binding.dashboardAnalyticsCardProgressBar.progress = state.progress
                            binding.dashboardAnalyticsCardProgressBarText.text = getString(
                                R.string.dashboard_card_plan_progress_percentage_sr,
                                state.progress
                            )
                            rvAdapter.submitList(state.takings)
                        }
                    }
                }

            }
        }

        binding.dashboardSelectDateButton.setOnClickListener {

            if (!datePicker.isAdded) {
                datePicker.show(childFragmentManager, TAG)
            }

            datePicker.addOnPositiveButtonClickListener {
                viewModel.updateDate(
                    LocalDate.ofEpochDay(Duration.ofMillis(datePicker.selection ?: 0).toDays())
                )
                datePicker.dismiss()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DATEPICKER"
    }
}