package com.example.medsreminder.data.repository.fake

import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.MedicineTaking
import com.example.medsreminder.model.Response
import com.example.medsreminder.model.Taking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeMedicineTakingRepository @Inject constructor(): MedicineTakingRepository {
    override fun saveMedicineTaking(medicine: Medicine): Flow<Response<Boolean>> = flow{
        emit(Response.Success(true))
    }

    override fun getMedicineTakings(): Flow<List<MedicineTaking>> = flow{
        emit(emptyList<MedicineTaking>())
    }

    override fun updateTaking(taking: Taking, status: MedicineStatusEnum) {
        /* no op */
    }
}