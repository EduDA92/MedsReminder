package com.example.medsreminder.data.repository.fake

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
import javax.inject.Inject

class FakeMedicineTakingRepository @Inject constructor() : MedicineTakingRepository {

    val medicineTakings = testData()
    override fun saveMedicineTaking(medicine: Medicine): Flow<Response<Boolean>> = flow {
        emit(Response.Success(true))
    }

    override fun getMedicineTakings(): Flow<List<MedicineTaking>> = flow {
        emit(medicineTakings)
    }

    override fun updateTaking(taking: Taking, status: MedicineStatusEnum) {
        /* no - op */
    }
    private fun testData(): List<MedicineTaking>{

        /* In order to the DashboardFragmentTest to be consistent with the expected values
        * the date has to be set at the start of the day. This way the state of the fragment
        * can be tested better. */
         val date = LocalDateTime.now().withHour(0).withMinute(0)


         val testMedicine1 = Medicine(
            name = "Naloxona",
            pillsAmount = 1f,
            duration = 1,
            hourSeparation = 4,
            meal = MedicineStatusEnum.DURING.name,
            creationDate = date.toString()
        )

         val temporalAmount = Duration.ofHours(testMedicine1.hourSeparation.toLong())
         val temporalAmount2 = Duration.ofHours(testMedicine1.hourSeparation.times(2).toLong())
         val temporalAmount3 = Duration.ofHours(testMedicine1.hourSeparation.times(3).toLong())
         val temporalAmount4 = Duration.ofHours(testMedicine1.hourSeparation.times(4).toLong())
         val temporalAmount5 = Duration.ofHours(testMedicine1.hourSeparation.times(5).toLong())

         val takingList = mutableListOf<Taking>(
            Taking(
                medicineName = testMedicine1.name,
                date = date.toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.DURING.name
            ),
            Taking(
                medicineName = testMedicine1.name,
                date = date.plus(temporalAmount).toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.AFTER.name
            ),
            Taking(
                medicineName = testMedicine1.name,
                date = date.plus(temporalAmount2).toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.AFTER.name
            ),
            Taking(
                medicineName = testMedicine1.name,
                date = date.plus(temporalAmount3).toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.COMPLETED.name
            ),
            Taking(
                medicineName = testMedicine1.name,
                date = date.plus(temporalAmount4).toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.COMPLETED.name
            ),
            Taking(
                medicineName = testMedicine1.name,
                date = date.plus(temporalAmount5).toString(),
                pillNumber = testMedicine1.pillsAmount,
                status = MedicineStatusEnum.COMPLETED.name
            )
        )

        return listOf(MedicineTaking(medicine = testMedicine1, takings = takingList))
    }


}