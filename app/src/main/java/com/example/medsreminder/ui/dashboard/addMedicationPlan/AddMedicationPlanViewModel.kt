package com.example.medsreminder.ui.dashboard.addMedicationPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Medicine
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
class AddMedicationPlanViewModel @Inject constructor(
    private val repository: MedicineTakingRepository
) : ViewModel() {

    private val _medicineTakingState = MutableSharedFlow<MedicationPlanUiState>()
    val medicineTakingState = _medicineTakingState.asSharedFlow()


    fun saveMedicineTaking(medicine: Medicine) {
        viewModelScope.launch {
            repository.saveMedicineTaking(medicine).collect{response ->
                    when(response){
                        is Response.Failure -> _medicineTakingState.emit(MedicationPlanUiState.ErrorSavingData(response.msg))
                        Response.Loading -> _medicineTakingState.emit(MedicationPlanUiState.WaitingDataSentSuccessfully)
                        is Response.Success -> _medicineTakingState.emit(MedicationPlanUiState.SuccessfullySavedData(response.success))
                    }
            }
        }
    }


}

sealed interface MedicationPlanUiState {
    object WaitingDataSentSuccessfully: MedicationPlanUiState
    data class SuccessfullySavedData(val success: Boolean) : MedicationPlanUiState
    data class ErrorSavingData(val error: String) : MedicationPlanUiState
}