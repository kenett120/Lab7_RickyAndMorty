package plat.lab1.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.local.UserPreferences
import plat.lab1.myapplication.data.room.RoomRepository

data class SessionState(
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val hasError: Boolean = false,
    val userName: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application)
    private val repository = RoomRepository(application)

    private val _uiState = MutableStateFlow(SessionState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.userName.collect { name ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = name
                )
            }
        }
    }

    fun login(name: String, onSuccess: () -> Unit) {
        val cleanName = name.trim()

        if (cleanName.isBlank() || _uiState.value.isSyncing) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncing = true,
                hasError = false
            )

            try {
                delay(4000)
                repository.synchronize()
                preferences.saveName(cleanName)
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hasError = true
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false
                )
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncing = true,
                hasError = false
            )

            try {
                repository.clearAll()
                preferences.clearName()
                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hasError = true
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false
                )
            }
        }
    }
}