package plat.lab1.myapplication.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.Character
import plat.lab1.myapplication.data.CharacterDb
import plat.lab1.myapplication.navigation.CharacterDetailsDestination
import plat.lab1.myapplication.ui.state.ScreenState

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val db = CharacterDb()

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
            delay(2000)

            try {
                _uiState.value = ScreenState(
                    isLoading = false,
                    data = db.getCharacterById(id)
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