package com.example.medsreminder.data.repository

import com.example.medsreminder.model.Appointment
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.MedicineTaking
import com.example.medsreminder.model.Response
import com.example.medsreminder.model.Taking
import kotlinx.coroutines.flow.Flow

interface MedicineTakingRepository {

    fun saveMedicineTaking(medicine: Medicine): Flow<Response<Boolean>>

    fun saveAppointment(appointment: Appointment): Flow<Response<Boolean>>

    fun getMedicineTakings(): Flow<List<MedicineTaking>>

    fun updateTaking(taking: Taking, status: MedicineStatusEnum)

}