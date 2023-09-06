package com.example.medsreminder.ui.dashboard.addMedicationPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.Medicine
import com.example.medsreminder.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMedicationPlanViewModel @Inject constructor(
    private val repository: MedicineTakingRepository
) : ViewModel() {

    private val _medicineTakingState: MutableStateFlow<MedicationPlanUiState> =
        MutableStateFlow(MedicationPlanUiState.WaitingForData)
    val medicineTakingState = _medicineTakingState.asStateFlow()


    fun saveMedicineTaking(medicine: Medicine) {
        viewModelScope.launch {
            repository.saveMedicineTaking(medicine).collect{response ->
                _medicineTakingState.update {
                    when(response){
                        is Response.Failure -> MedicationPlanUiState.ErrorSavingData(response.msg)
                        Response.Loading -> MedicationPlanUiState.WaitingDataSentSuccessfully
                        is Response.Success -> MedicationPlanUiState.SuccessfullySavedData(response.success)
                    }
                }
            }
        }
    }


}

sealed interface MedicationPlanUiState {
    object WaitingForData : MedicationPlanUiState
    object WaitingDataSentSuccessfully: MedicationPlanUiState
    data class SuccessfullySavedData(val success: Boolean) : MedicationPlanUiState
    data class ErrorSavingData(val error: String) : MedicationPlanUiState
}