package plat.lab1.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.Location
import plat.lab1.myapplication.data.room.RoomRepository
import plat.lab1.myapplication.navigation.LocationDetailsDestination
import plat.lab1.myapplication.ui.state.ScreenState

class LocationDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = RoomRepository(application)

    private val id =
        savedStateHandle.toRoute<LocationDetailsDestination>().id

    private val _uiState =
        MutableStateFlow(ScreenState<Location>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()
        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            try {
                delay(2000)

                repository.getLocation(id).collect { location ->
                    _uiState.value = ScreenState(
                        isLoading = false,
                        data = location,
                        hasError = location == null
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