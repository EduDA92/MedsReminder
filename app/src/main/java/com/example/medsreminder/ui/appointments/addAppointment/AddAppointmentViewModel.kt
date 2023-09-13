package com.example.medsreminder.ui.appointments.addAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Appointment
import com.example.medsreminder.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAppointmentViewModel @Inject constructor(private val repository: MedicineTakingRepository) :
    ViewModel() {

    private val _addAppointmentUiState = MutableSharedFlow<AddAppointmentUiState>()
    val addAppointmentUiState = _addAppointmentUiState.asSharedFlow()

    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.saveAppointment(appointment).collect { response ->
                    when (response) {
                        is Response.Failure -> _addAppointmentUiState.emit(AddAppointmentUiState.ErrorSavingData(response.msg))
                        Response.Loading -> _addAppointmentUiState.emit(AddAppointmentUiState.WaitingDataSentSuccessfully)
                        is Response.Success -> _addAppointmentUiState.emit(AddAppointmentUiState.SuccessfullySavedData(response.success))
                    }
            }
        }
    }

}

sealed interface AddAppointmentUiState {
    object WaitingDataSentSuccessfully : AddAppointmentUiState
    data class SuccessfullySavedData(val success: Boolean) : AddAppointmentUiState
    data class ErrorSavingData(val error: String) : AddAppointmentUiState
}