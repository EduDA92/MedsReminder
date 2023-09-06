package com.example.medsreminder.ui.dashboard


import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medsreminder.R
import com.example.medsreminder.launchFragmentInHiltContainer
import com.example.medsreminder.ui.dashboard.addMedicationPlan.AddMedicationPlanFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddMedicationPlanFragmentTest {

    private val pillsName = "Naproxeno"
    private val amount = "2"
    private val howLong = "10"
    private val howOften = "8"

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // TODO
    @Test
    fun addMedicationPlanFragment_filling_form_test() {
       launchAddMedicationPlanFragment()

        onView(withId(R.id.addMedicationPlan_pills_name_edit_text)).perform(
            typeText(pillsName),
            closeSoftKeyboard()
        )
    }


    private fun launchAddMedicationPlanFragment(){

        val navController = TestNavHostController(getApplicationContext())

        launchFragmentInHiltContainer<AddMedicationPlanFragment> (navHostController = navController)

    }
}