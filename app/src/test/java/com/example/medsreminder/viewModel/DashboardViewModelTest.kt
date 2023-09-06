package com.example.medsreminder.viewModel

import app.cash.turbine.turbineScope
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.Taking
import com.example.medsreminder.testDoubles.TestMedicineTakingRepository
import com.example.medsreminder.ui.dashboard.DashboardUiState
import com.example.medsreminder.ui.dashboard.DashboardViewModel
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

class DashboardViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("DashboardViewModelTest thread")

    private lateinit var medicineTakingRepository: TestMedicineTakingRepository
    private lateinit var subject: DashboardViewModel
    private lateinit var takingList: List<Taking>
    private val actualDate = LocalDateTime.now()
    private val testDate = actualDate.plusDays(5)

    private val medicine = Medicine(
        name = "Frenadol",
        pillsAmount = 1f,
        duration = 10,
        hourSeparation = 4,
        meal = MedicineStatusEnum.DURING.name,
        creationDate = actualDate.toString()
    )



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(mainThreadSurrogate)

        medicineTakingRepository = TestMedicineTakingRepository()
        subject = DashboardViewModel((medicineTakingRepository))

        // Create some test data in order to test
        medicineTakingRepository.saveMedicine(medicine)

        takingList = medicineTakingRepository.createTakings(medicine)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    @Test
    fun dashboardViewModel_date_state_updates_correctly() = runTest {


        turbineScope {

            val dashboardState = subject.dashboardUiState.testIn(backgroundScope)

            // Initial State
            assertEquals(DashboardUiState.Loading, dashboardState.awaitItem())


            assertEquals(DashboardUiState.Success(
                takings = takingList.filter { it.date == actualDate.toLocalDate().toString() },
                date = actualDate.toLocalDate(),
                takingsNumber = takingList.filter { it.date == actualDate.toLocalDate().toString() }.size,
                completedTakings = 0,
                 progress = 0

            ), dashboardState.awaitItem())

            // Update date
            subject.updateDate(testDate.toLocalDate())

            assertEquals(DashboardUiState.Success(
                takings = takingList.filter { it.date == testDate.toLocalDate().toString() },
                date = testDate.toLocalDate(),
                takingsNumber = takingList.filter { it.date == testDate.toLocalDate().toString() }.size,
                completedTakings = 0,
                progress = 0

            ), dashboardState.awaitItem())


        }

    }



}