package plat.lab1.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.Character
import plat.lab1.myapplication.data.CharacterDb
import plat.lab1.myapplication.ui.state.ScreenState

class CharactersViewModel : ViewModel() {

    private val db = CharacterDb()

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
            delay(4000)

            try {
                _uiState.value = ScreenState(
                    isLoading = false,
                    data = db.getAllCharacters()
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