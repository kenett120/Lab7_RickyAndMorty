package plat.lab1.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.Location
import plat.lab1.myapplication.data.room.RoomRepository
import plat.lab1.myapplication.ui.state.ScreenState

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RoomRepository(application)

    private val _uiState =
        MutableStateFlow(ScreenState<List<Location>>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()
        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            try {
                delay(4000)

                repository.getLocations().collect { locations ->
                    _uiState.value = ScreenState(
                        isLoading = false,
                        data = locations
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ScreenState(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()
            _uiState.value = ScreenState(
                isLoading = false,
                hasError = true
            )
        }
    }
}