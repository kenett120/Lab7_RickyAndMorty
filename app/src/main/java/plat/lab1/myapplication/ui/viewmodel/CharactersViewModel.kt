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
import plat.lab1.myapplication.data.Character
import plat.lab1.myapplication.data.room.RoomRepository
import plat.lab1.myapplication.ui.state.ScreenState

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RoomRepository(application)

    private val _uiState =
        MutableStateFlow(ScreenState<List<Character>>())

    val uiState = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadingJob?.cancel()
        _uiState.value = ScreenState(isLoading = true)

        loadingJob = viewModelScope.launch {
            try {
                delay(4000)

                repository.getCharacters().collect { characters ->
                    _uiState.value = ScreenState(
                        isLoading = false,
                        data = characters
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

    fun toggleFavorite(character: Character) {
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