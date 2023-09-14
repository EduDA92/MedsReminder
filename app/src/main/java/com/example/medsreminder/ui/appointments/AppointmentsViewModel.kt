package com.example.medsreminder.ui.appointments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Appointment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(private val repository: MedicineTakingRepository) :
    ViewModel() {

    private val _date = MutableStateFlow(LocalDate.now())
    private val appointments = repository.getAppointments()

    val appointmentUiState: StateFlow<AppointmentUiState> =
        combine(_date, appointments) { date, appointments ->

            Log.e("LIST", "VIEWMODEL LIST = " + appointments.appointments.toString())
            val appointmentList = appointments.appointments.filter {
                LocalDateTime.parse(it.date).toLocalDate() == date
            }

            AppointmentUiState.Success(
                appointments = appointmentList,
                date = date
            )

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            AppointmentUiState.Loading
        )

    fun deleteAppointment(appointment: Appointment) {
        repository.removeAppointment(appointment)
    }

    fun updateDate(date: LocalDate) {
        _date.update {
            date
        }
    }
}

sealed interface AppointmentUiState {

    object Loading : AppointmentUiState

    data class Success(
        val appointments: List<Appointment>,
        val date: LocalDate
    ) : AppointmentUiState

}