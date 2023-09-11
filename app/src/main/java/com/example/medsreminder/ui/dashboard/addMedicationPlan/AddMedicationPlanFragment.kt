package com.example.medsreminder.ui.dashboard.addMedicationPlan

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
import com.example.medsreminder.R
import com.example.medsreminder.databinding.FragmentAddMedicationPlanBinding
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.ui.alarm.AlarmTakingItem
import com.example.medsreminder.ui.alarm.TakingAlarmScheduler
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class AddMedicationPlanFragment : Fragment() {

    private var _binding: FragmentAddMedicationPlanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddMedicationPlanViewModel by viewModels()

    // Get current date
    private val currentDate = LocalDateTime.now()

    // Current selected meal for taking the medicine
    private var medicineStatusEnum: MedicineStatusEnum? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMedicationPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmScheduler = TakingAlarmScheduler(requireContext())

        /* setting up the topAppBar */
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.addMedicationPlanTopAppBar.setupWithNavController(
            navController,
            appBarConfiguration
        )


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.medicineTakingState.collect{state ->
                    when(state){
                        is MedicationPlanUiState.ErrorSavingData -> {
                            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        is MedicationPlanUiState.SuccessfullySavedData -> {
                            navController.popBackStack()
                        }
                        MedicationPlanUiState.WaitingForData -> {/* Initial state, do nothing */}
                        MedicationPlanUiState.WaitingDataSentSuccessfully -> {
                            binding.addMedicationPlanAddPlanButton.visibility = View.INVISIBLE
                            binding.addMedicationPlanLoadingCircle.visibility = View.VISIBLE
                        }
                    }

                }
            }
        }

        binding.addMedicationPlanChoosePillsMeal.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.addMedicationPlan_before_meal_button -> medicineStatusEnum = MedicineStatusEnum.BEFORE
                R.id.addMedicationPlan_during_meal_button -> medicineStatusEnum = MedicineStatusEnum.DURING
                R.id.addMedicationPlan_after_meal_button -> medicineStatusEnum = MedicineStatusEnum.AFTER
            }
        }


        binding.addMedicationPlanAddPlanButton.setOnClickListener {

            if (binding.addMedicationPlanPillsNameInputLayout.editText?.text?.isEmpty() == true ||
                binding.addMedicationPlanPillsAmountInputLayout.editText?.text?.isEmpty() == true ||
                binding.addMedicationPlanPillsHowLongInputLayout.editText?.text?.isEmpty() == true ||
                binding.addMedicationPlanPillsHowOftenInputLayout.editText?.text?.isEmpty() == true ||
                binding.addMedicationPlanChoosePillsMeal.checkedButtonId == -1
            ) {
                Snackbar.make(binding.root, getString(R.string.empty_field), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val currentMedicine = Medicine(
                    name = binding.addMedicationPlanPillsNameInputLayout.editText?.text.toString(),
                    pillsAmount = binding.addMedicationPlanPillsAmountInputLayout.editText?.text.toString()
                        .toFloat(),
                    duration = binding.addMedicationPlanPillsHowLongInputLayout.editText?.text.toString()
                        .toInt(),
                    hourSeparation = binding.addMedicationPlanPillsHowOftenInputLayout.editText?.text.toString()
                        .toInt(),
                    creationDate = currentDate.toString(),
                    meal = medicineStatusEnum!!.name
                )
                // Save medicine
                viewModel.saveMedicineTaking(currentMedicine)

                val alarmItem = AlarmTakingItem(
                    time = LocalDateTime.parse(currentMedicine.creationDate),
                    interval = currentMedicine.hourSeparation.toMilis(),
                    duration = currentMedicine.duration,
                    msg = resources.getString(R.string.notification_taking_reminder, currentMedicine.name))

                /* Set alarms for the takings and also add one for canceling the repeating alarm at the
                * calculated ending date */
                alarmScheduler.schedule(alarmItem)
                alarmScheduler.scheduleCancelAlarm(alarmItem)

            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Int.toMilis(): Long{
        return this.times(60).times(60).times(1000).toLong()
    }

}