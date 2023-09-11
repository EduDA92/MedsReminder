package com.example.medsreminder.repository


import com.example.medsreminder.data.repository.DefaultMedicineTakingRepository
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.util.createTakings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DefaultMedicineTakingRepositoryTest {


    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("DefaultMedicineTakingRepositoryTest thread")

    private lateinit var subject: DefaultMedicineTakingRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val medicine = Medicine(
        name = "Frenadol",
        pillsAmount = 1f,
        duration = 2,
        hourSeparation = 4,
        meal = "AFTER",
        creationDate = LocalDateTime.now().withHour(0).withMinute(0).toString()
    )

    private val zeroMed = Medicine(
        name = "Frenadol",
        pillsAmount = 0f,
        duration = 0,
        hourSeparation = 0,
        meal = "AFTER",
        creationDate = LocalDateTime.now().withHour(0).withMinute(0).toString()
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)

        firebaseAuth = Mockito.mock(FirebaseAuth::class.java)

        firestore = Mockito.mock(FirebaseFirestore::class.java)

        subject = DefaultMedicineTakingRepository(firebaseAuth, firestore)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun test_div_by_zero_doesnt_throw(){

        val list = createTakings(zeroMed)

    }

    @Test
    fun testTakingsCreation() {

        val list = createTakings(medicine)

        /* Expected number of takings for 10 days, each day 6 takings (every 4 hours) */
        assertEquals(medicine.duration.times(24).div(medicine.hourSeparation), list.size)

        /* Assert that the date of each taking increases */
        assert(LocalDateTime.parse(list[0].date).hour < LocalDateTime.parse(list[1].date).hour)

        /* Assert that the time increased is the time set by the user */

        assert((LocalDateTime.parse(list[1].date).hour - LocalDateTime.parse(list[0].date).hour) == medicine.hourSeparation)


    }

}