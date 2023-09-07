package com.example.medsreminder.testDoubles

import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.MedicineTaking
import com.example.medsreminder.model.Response
import com.example.medsreminder.model.Taking
import com.example.medsreminder.util.createTakings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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


}