package plat.lab1.myapplication.ui.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.myapplication.ui.screens.characterform.CharacterFormScreen
import plat.lab1.myapplication.ui.viewmodel.CharacterFormViewModel

@Composable
fun CharacterFormStateScreen(
    onSaveSuccess: () -> Unit,
    viewModel: CharacterFormViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    CharacterFormScreen(
        state = state,
        onNameChange = viewModel::updateName,
        onStatusChange = viewModel::updateStatus,
        onSpeciesChange = viewModel::updateSpecies,
        onGenderChange = viewModel::updateGender,
        onImageChange = viewModel::updateImage,
        onSaveClick = {
            viewModel.save(onSaveSuccess)
        }
    )
}