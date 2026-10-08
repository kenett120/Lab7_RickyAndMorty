package plat.lab1.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.Location
import plat.lab1.myapplication.data.LocationDb
import plat.lab1.myapplication.ui.state.ScreenState

class LocationsViewModel : ViewModel() {

    private val db = LocationDb()

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
            delay(4000)

            try {
                _uiState.value = ScreenState(
                    isLoading = false,
                    data = db.getAllLocations()
                )
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