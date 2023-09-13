package com.example.medsreminder.ui.appointments

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medsreminder.R
import com.example.medsreminder.launchFragmentInHiltContainer
import com.example.medsreminder.ui.appointments.addAppointment.AddAppointmentFragment
import com.example.medsreminder.ui.dashboard.addMedicationPlan.AddMedicationPlanFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddAppointmentFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun addAppointmentFragment_filling_form_test(){


    }

    private fun launchAddAppointmentFragment() {

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())


        launchFragmentInHiltContainer<AddAppointmentFragment>(
            navHostController = navController,
            graphResInt = R.navigation.nav_graph
        )

    }
}