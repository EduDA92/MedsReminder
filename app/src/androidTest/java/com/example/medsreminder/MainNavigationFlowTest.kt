package com.example.medsreminder

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medsreminder.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainNavigationFlowTest {


    /* First fragment, Loading Data Fragment loads so fast that Espresso cant catch it so start testing
    * from MainAuthFragment */

    private val testEmail = "test@test.con"
    private val testEmailPass = "testing"

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun createUserFlowTest() {
        openSignUpFragment()
        assertSignUpFragment()

        signUp()

        assertDashboard()

        openUser()
        assertUser()
        logOut()

        assertMainAuthFragment()

    }

    /* Check usual login flow */
    @Test
    fun loginFlowTest() {

        assertMainAuthFragment()

        openLogInFragment()

        assertLogInFragment()

        signIn()

        assertDashboard()

        openCalendar()
        assertCalendar()

        openAppointments()
        assertAppointments()

        openUser()
        assertUser()
        logOut()

        assertMainAuthFragment()

    }

    @Test
    fun forgotPasswordFlowTest(){
        assertMainAuthFragment()

        openLogInFragment()
        assertLogInFragment()

        openForgotPasswordFragment()
        assertForgotPasswordFragment()
        forgotPassword()

        assertLogInFragment()
    }


    @Test(expected = NoActivityResumedException::class)
    fun backFromMainAuthExits() {

        assertMainAuthFragment()

        Espresso.pressBack() // This should throw NoActivityResumedException

        Assert.fail() // If it doesn't throw


    }

    @Test(expected = NoActivityResumedException::class)
    fun backFromDashboardExits() {

        assertMainAuthFragment()

        openLogInFragment()

        signIn()

        assertDashboard()

        Espresso.pressBack()

        Assert.fail()

    }

    @Test
    fun notFillingPasswordSignInKeepsButtonDeactivated(){

        assertMainAuthFragment()

        openLogInFragment()

        // Only type Email
        onView(withId(R.id.emailTextFieldEditText)).perform(
            typeText(testEmail),
            closeSoftKeyboard()
        )

        onView(withId(R.id.sign_in_button)).check(matches(isNotEnabled()))

    }

    @Test
    fun notFillingEmailSignInKeepsButtonDeactivated(){

        assertMainAuthFragment()

        openLogInFragment()

        // Only type Password
        onView(withId(R.id.passwordTextFieldEditText)).perform(
            typeText(testEmailPass),
            closeSoftKeyboard()
        )

        onView(withId(R.id.sign_in_button)).check(matches(isNotEnabled()))

    }


    @Test
    fun notFillingPasswordSignUpKeepsButtonDeactivated(){

        assertMainAuthFragment()

        openSignUpFragment()

        // Only type Email
        onView(withId(R.id.emailTextFieldEditText)).perform(
            typeText(testEmail),
            closeSoftKeyboard()
        )

        onView(withId(R.id.create_account_button)).check(matches(isNotEnabled()))

    }

    @Test
    fun notFillingEmailSignUpKeepsButtonDeactivated(){

        assertMainAuthFragment()

        openSignUpFragment()

        // Only type Password
        onView(withId(R.id.passwordTextFieldEditText)).perform(
            typeText(testEmailPass),
            closeSoftKeyboard()
        )

        onView(withId(R.id.create_account_button)).check(matches(isNotEnabled()))

    }


    @Test
    fun notFillingEmailRecoverPasswordKeepsButtonDeactivated(){
        assertMainAuthFragment()

        openLogInFragment()
        assertLogInFragment()

        openForgotPasswordFragment()

        onView(withId(R.id.send_email_button)).check(matches(isNotEnabled()))
    }

    private fun signIn() {

        onView(withId(R.id.emailTextFieldEditText)).perform(
            typeText(testEmail),
            closeSoftKeyboard()
        )
        onView(withId(R.id.passwordTextFieldEditText)).perform(
            typeText(testEmailPass),
            closeSoftKeyboard()
        )
        onView(withId(R.id.sign_in_button)).perform(click())

    }

    private fun signUp() {
        onView(withId(R.id.emailTextFieldEditText)).perform(
            typeText(testEmail),
            closeSoftKeyboard()
        )
        onView(withId(R.id.passwordTextFieldEditText)).perform(
            typeText(testEmailPass),
            closeSoftKeyboard()
        )
        onView(withId(R.id.create_account_button)).perform(
            click()
        )
    }

    private fun forgotPassword(){
        onView(withId(R.id.emailTextFieldEditText)).perform(
            typeText(testEmail),
            closeSoftKeyboard()
        )
        onView(withId(R.id.send_email_button)).perform(click())
    }

    private fun assertMainAuthFragment() {
        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()))
    }

    private fun assertLogInFragment() {
        onView(withId(R.id.logInTopAppBarLayout)).check(matches(isDisplayed()))
    }

    private fun openLogInFragment() {
        onView(withId(R.id.log_in_button)).perform(click())
    }

    private fun openForgotPasswordFragment(){
        onView(withId(R.id.forgotPasswordTextView)).perform(click())
    }
    private fun assertForgotPasswordFragment(){
        onView(withId(R.id.forgotPasswordTopAppBar)).check(matches(isDisplayed()))
    }

    private fun openSignUpFragment() {
        onView(withId(R.id.sign_up_button)).perform(click())
    }

    private fun assertSignUpFragment() {
        onView(withId(R.id.signUpTopAppBarLayout)).check(matches(isDisplayed()))
    }

    private fun assertDashboard() {
        onView(withId(R.id.dashboardTopAppBarLayout)).check(matches(isDisplayed()))
    }

    private fun openCalendar() {
        onView(allOf(ViewMatchers.withContentDescription(R.string.calendar), isDisplayed()))
            .perform(click())
    }

    private fun assertCalendar() {
        onView(withId(R.id.calendarTopAppBar)).check(matches(isDisplayed()))
    }

    private fun openAppointments() {
        onView(allOf(ViewMatchers.withContentDescription(R.string.appointments), isDisplayed()))
            .perform(click())
    }

    private fun assertAppointments() {
        onView(withId(R.id.appointmentsTopAppBar)).check(matches(isDisplayed()))
    }

    private fun openUser() {
        onView(allOf(ViewMatchers.withContentDescription(R.string.user), isDisplayed()))
            .perform(click())
    }

    private fun assertUser() {
        onView(withId(R.id.userTopAppBar)).check(matches(isDisplayed()))
    }

    private fun logOut() {
        onView(withId(R.id.test_logout_button)).perform(click())
    }

}