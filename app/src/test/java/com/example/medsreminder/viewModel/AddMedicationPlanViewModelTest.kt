package com.example.medsreminder.viewModel

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.testDoubles.TestMedicineTakingRepository
import com.example.medsreminder.ui.dashboard.addMedicationPlan.AddMedicationPlanViewModel
import com.example.medsreminder.ui.dashboard.addMedicationPlan.MedicationPlanUiState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AddMedicationPlanViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("AddMedicationPlanViewModelTest thread")

    private val medicine = Medicine(
        name = "Frenadol",
        pillsAmount = 1f,
        duration = 10,
        hourSeparation = 4,
        meal = MedicineStatusEnum.DURING.name,
        creationDate = LocalDateTime.now().toString()
    )

    private lateinit var medicineTakingRepository: TestMedicineTakingRepository
    private lateinit var subject: AddMedicationPlanViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        medicineTakingRepository = TestMedicineTakingRepository()
        subject = AddMedicationPlanViewModel(medicineTakingRepository)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun addMedicationPlanViewModel_check_initial_state() = runTest {

        subject.medicineTakingState.test {
            assertEquals(MedicationPlanUiState.WaitingForData, awaitItem())
        }

    }

    @Test
    fun addMedicationPlanViewModel_incorrect_medicine_returns_failure() = runTest {

        turbineScope {
            val state = subject.medicineTakingState.testIn(backgroundScope)

            // Save medicine
            subject.saveMedicineTaking(Medicine())


            assertEquals(MedicationPlanUiState.WaitingForData, state.awaitItem())
            assertEquals(MedicationPlanUiState.ErrorSavingData("Error"), state.awaitItem())

        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addMedicationPlanViewModel_correct_medicine_returns_success() = runTest {

        turbineScope {
            val state = subject.medicineTakingState.testIn(backgroundScope)

            // Save medicine
            subject.saveMedicineTaking(medicine)


            assertEquals(MedicationPlanUiState.WaitingForData, state.awaitItem())
            assertEquals(MedicationPlanUiState.SuccessfullySavedData(true), state.awaitItem())
        }

    }


}