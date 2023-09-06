package com.example.medsreminder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medsreminder.data.repository.MedicineTakingRepository
import com.example.medsreminder.model.MedicineStatusEnum
import com.example.medsreminder.model.Taking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: MedicineTakingRepository
) : ViewModel() {

    private val _date = MutableStateFlow(LocalDate.now())
    private val medicineTakingList = repository.getMedicineTakings()

    val dashboardUiState: StateFlow<DashboardUiState> =
        combine(_date, medicineTakingList) { date, list ->

            // first filter the taking list to the ones that match te current date
            val takingList = mutableListOf<Taking>()

            list.forEach { medicineTaking ->
                takingList.addAll(medicineTaking.takings.filter { taking ->
                    taking.date == date.toString()
                })
            }

            // Sort by taking hour
            takingList.sortBy { taking -> taking.hour }

            // Get completed takings in order to track the progress

            val completedTakings = takingList.filter { taking ->
                taking.status == MedicineStatusEnum.COMPLETED.name
            }


            DashboardUiState.Success(
                takings = takingList,
                date = date,
                takingsNumber = takingList.size,
                completedTakings = completedTakings.size,
                progress = calculateProgress(completedTakings.size, takingList.size)
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            DashboardUiState.Loading
        )

    fun updateTaking(taking: Taking, status: MedicineStatusEnum) =
        repository.updateTaking(taking, status)


    fun updateDate(date: LocalDate) {
        _date.update {
            date
        }
    }

    private fun calculateProgress(completedTakings: Int, totalTakings: Int): Int{

        return if(completedTakings == 0){
            0
        } else {
            completedTakings.times(100).div(totalTakings)
        }

    }


}

sealed interface DashboardUiState {
    object Loading : DashboardUiState

    data class Success(
        val takings: List<Taking>,
        val date: LocalDate,
        val takingsNumber: Int,
        val completedTakings: Int,
        val progress: Int
    ) : DashboardUiState
}