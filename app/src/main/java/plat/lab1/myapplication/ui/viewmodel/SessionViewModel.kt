package plat.lab1.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.local.UserPreferences

data class SessionState(
    val isLoading: Boolean = true,
    val userName: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application)

    private val _uiState = MutableStateFlow(SessionState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.userName.collect { name ->
                _uiState.value = SessionState(
                    isLoading = false,
                    userName = name
                )
            }
        }
    }

    fun login(name: String, onSuccess: () -> Unit) {
        val cleanName = name.trim()

        if (cleanName.isBlank()) return

        viewModelScope.launch {
            preferences.saveName(cleanName)
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferences.clearName()
            onSuccess()
        }
    }
}