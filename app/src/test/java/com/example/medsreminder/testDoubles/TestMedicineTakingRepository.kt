package com.example.medsreminder.testDoubles

import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.MedicineTaking
import com.example.medsreminder.model.Response
import com.example.medsreminder.model.Taking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime

class TestMedicineTakingRepository(): MedicineTakingRepository {

    private val medicineTakings = mutableListOf<MedicineTaking>()

    override fun saveMedicineTaking(medicine: Medicine): Flow<Response<Boolean>> = flow{

        if(medicine.name.isNotEmpty()){
            emit(Response.Success(true))
        } else {
            emit(Response.Failure("Error"))
        }
    }

    fun saveMedicine(medicine: Medicine){
        medicineTakings.add(MedicineTaking(medicine = medicine, takings = createTakings(medicine)))
    }

    override fun getMedicineTakings(): Flow<List<MedicineTaking>> = flow{
        emit(medicineTakings)
    }

    override fun updateTaking(taking: Taking, status: MedicineStatusEnum) {
        /* no op */
    }

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
                    date = startingDate.toLocalDate().toString(),
                    hour = startingDate.hour,
                    minute = startingDate.minute,
                    pillNumber = medicine.pillsAmount,
                    status = medicine.meal
                )
            )

            // Increase starting date
            startingDate = startingDate.plus(temporalAmount)


        }

        return takingsList
    }
}