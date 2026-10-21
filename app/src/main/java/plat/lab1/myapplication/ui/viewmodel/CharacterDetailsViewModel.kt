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
import plat.lab1.myapplication.data.Character
import plat.lab1.myapplication.data.room.RoomRepository
import plat.lab1.myapplication.navigation.CharacterDetailsDestination
import plat.lab1.myapplication.ui.state.ScreenState

class CharacterDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = RoomRepository(application)

    private val id =
        savedStateHandle.toRoute<CharacterDetailsDestination>().id

    private val _uiState =
        MutableStateFlow(ScreenState<Character>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()
        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            try {
                delay(2000)

                repository.getCharacter(id).collect { character ->
                    _uiState.value = ScreenState(
                        isLoading = false,
                        data = character,
                        hasError = character == null
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

    fun toggleFavorite() {
        val character = _uiState.value.data ?: return

        viewModelScope.launch {
            repository.updateFavorite(
                character.id,
                !character.isFavorite
            )
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