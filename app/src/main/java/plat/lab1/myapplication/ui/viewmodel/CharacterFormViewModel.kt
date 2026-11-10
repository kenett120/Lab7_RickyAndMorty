package plat.lab1.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab1.myapplication.data.room.RoomRepository
import plat.lab1.myapplication.navigation.EditCharacterDestination

data class CharacterFormState(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

class CharacterFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = RoomRepository(application)

    private val characterId =
        savedStateHandle.toRoute<EditCharacterDestination>().id

    private val _uiState = MutableStateFlow(CharacterFormState())
    val uiState = _uiState.asStateFlow()

    init {
        if (characterId != 0) {
            viewModelScope.launch {
                val character = repository.getCharacter(characterId).first()

                if (character != null) {
                    _uiState.value = CharacterFormState(
                        name = character.name,
                        status = character.status,
                        species = character.species,
                        gender = character.gender,
                        image = character.image
                    )
                }
            }
        }
    }

    fun updateName(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun updateStatus(value: String) {
        _uiState.value = _uiState.value.copy(status = value)
    }

    fun updateSpecies(value: String) {
        _uiState.value = _uiState.value.copy(species = value)
    }

    fun updateGender(value: String) {
        _uiState.value = _uiState.value.copy(gender = value)
    }

    fun updateImage(value: String) {
        _uiState.value = _uiState.value.copy(image = value)
    }

    fun save(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.name.isBlank() || state.isLoading) return

        viewModelScope.launch {
            _uiState.value = state.copy(
                isLoading = true,
                hasError = false
            )

            try {
                if (characterId == 0) {
                    repository.createCharacter(
                        name = state.name,
                        status = state.status,
                        species = state.species,
                        gender = state.gender,
                        image = state.image
                    )
                } else {
                    val current = repository.getCharacter(characterId).first()

                    if (current != null) {
                        repository.updateCharacter(
                            current.copy(
                                name = state.name,
                                status = state.status,
                                species = state.species,
                                gender = state.gender,
                                image = state.image
                            )
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            hasError = true
                        )
                        return@launch
                    }
                }

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hasError = true
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }
}