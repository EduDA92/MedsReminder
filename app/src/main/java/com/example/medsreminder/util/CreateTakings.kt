package com.example.medsreminder.util

import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.Taking
import java.time.Duration
import java.time.LocalDateTime

fun createTakings(medicine: Medicine): List<Taking> {

    val takingsList = mutableListOf<Taking>()

    var numberOfTakings = 0

    var startingDate = LocalDateTime.parse(medicine.creationDate)

    // Temporal amount to increase the starting date increasingly for every taking
    val temporalAmount = Duration.ofHours(medicine.hourSeparation.toLong())

    /* To simplify the implementation will assume that Duration(hours) % takings hour separation(hours) == 0,
    * also check if > 0 to prevent by zero div */
    if(medicine.hourSeparation > 0){
        numberOfTakings = medicine.duration.times(24).div(medicine.hourSeparation)
    }

    for (taking in 0 until numberOfTakings) {


        takingsList.add(
            Taking(
                medicineName = medicine.name,
                date = startingDate.toString(),
                pillNumber = medicine.pillsAmount,
                status = medicine.meal
            )
        )

        // Increase starting date
        startingDate = startingDate.plus(temporalAmount)


    }

    return takingsList
}