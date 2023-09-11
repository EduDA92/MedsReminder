package com.example.medsreminder.ui.dashboard

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medsreminder.R
import com.example.medsreminder.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val date = LocalDateTime.now()
    private  val  expectedProgressBar = "50%"
    private  val expectedProgressText=  "3 of 6 completed"
    private var rvSize = 6




    @Test
    fun dashboardFragment_check_states_match_expected(){
        launchDashboardFragment()

        /* Check date matches expected date(Today date) */
        onView(withId(R.id.dashboard_date)).check(matches(withText(date.dayOfWeek.name)))

        /* Expected progress is 50% 3 of 6 completed */
        onView(withId(R.id.dashboard_analytics_card_progress_bar_text)).check(matches(withText(expectedProgressBar)))
        onView(withId(R.id.dashboard_analytics_card_progress_text)).check(matches(withText(expectedProgressText)))

        /* Check RecyclerView size */
        onView(withId(R.id.dashboard_meds_recycler_view)).check(matches(hasChildCount(rvSize)))
    }

    private fun launchDashboardFragment() {

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())


        launchFragmentInHiltContainer<DashboardFragment>(
            navHostController = navController,
            graphResInt = R.navigation.nav_graph
        )

    }
}