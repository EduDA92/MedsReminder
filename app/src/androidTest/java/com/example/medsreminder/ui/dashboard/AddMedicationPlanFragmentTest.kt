package com.example.medsreminder.ui.dashboard


import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medsreminder.R
import com.example.medsreminder.launchFragmentInHiltContainer
import com.example.medsreminder.ui.dashboard.addMedicationPlan.AddMedicationPlanFragment
import com.example.medsreminder.util.checkSnackBarDisplayedByMessage
import com.example.medsreminder.util.waitFor
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

    @Test
    fun addMedicationPlanFragment_filling_form_test() {
        launchAddMedicationPlanFragment()

        /* Pills name */
        onView(withId(R.id.addMedicationPlan_pills_name_edit_text)).perform(
            typeText(pillsName),
            closeSoftKeyboard()
        )

        onView(withId(R.id.addMedicationPlan_add_plan_button)).perform(
            click()
        )

        checkSnackBarDisplayedByMessage(R.string.empty_field)

        /* wait till snackbar dissapear */
        onView(isRoot()).perform(waitFor(5000))


        /* Pills amount */
        onView(withId(R.id.addMedicationPlan_pills_amount_edit_text)).perform(
            typeText(amount),
            closeSoftKeyboard()
        )

        /* Pills How Long & check empty fields snackbar*/
        onView(withId(R.id.addMedicationPlan_pills_how_long_edit_text)).perform(
            typeText(howLong),
            closeSoftKeyboard()
        )


        /* Pills how often */
        onView(withId(R.id.addMedicationPlan_pills_howOften_edit_text)).perform(
            typeText(howOften),
            closeSoftKeyboard()
        )

        onView(withId(R.id.addMedicationPlan_add_plan_button)).perform(
            click()
        )

        checkSnackBarDisplayedByMessage(R.string.empty_field)

        /* wait till snackbar dissapear */
        onView(isRoot()).perform(waitFor(5000))


        /* Pills taking meal */
        onView(withId(R.id.addMedicationPlan_during_meal_button)).perform(
            click()
        )

        onView(withId(R.id.addMedicationPlan_add_plan_button)).perform(
            click()
        )

        /* Assert no snackbar shown because all fields are filled */
        onView(withText(R.string.empty_field)).check(doesNotExist())


    }




    private fun launchAddMedicationPlanFragment() {

        val navController = TestNavHostController(getApplicationContext())


        launchFragmentInHiltContainer<AddMedicationPlanFragment>(
            navHostController = navController,
            graphResInt = R.navigation.nav_graph
        )

    }
}