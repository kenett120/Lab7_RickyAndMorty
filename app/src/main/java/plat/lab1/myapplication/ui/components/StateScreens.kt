package plat.lab1.myapplication.ui.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.myapplication.ui.screens.characters.CharactersScreen
import plat.lab1.myapplication.ui.screens.details.CharacterDetailsScreen
import plat.lab1.myapplication.ui.screens.locations.LocationsScreen
import plat.lab1.myapplication.ui.screens.locationdetails.LocationDetailsScreen
import plat.lab1.myapplication.ui.viewmodel.CharactersViewModel
import plat.lab1.myapplication.ui.viewmodel.CharacterDetailsViewModel
import plat.lab1.myapplication.ui.viewmodel.LocationsViewModel
import plat.lab1.myapplication.ui.viewmodel.LocationDetailsViewModel

@Composable
fun CharactersStateScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> ErrorScreen(
            onRetryClick = viewModel::loadCharacters
        )

        state.isLoading -> LoadingScreen(
            onClick = viewModel::showError
        )

        state.data != null -> CharactersScreen(
            characters = state.data,
            onCharacterClick = onCharacterClick,
            onFavoriteClick = viewModel::toggleFavorite
        )
    }
}

@Composable
fun CharacterDetailsStateScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> ErrorScreen(
            onRetryClick = viewModel::loadCharacter
        )

        state.isLoading -> LoadingScreen(
            onClick = viewModel::showError
        )

        state.data != null -> CharacterDetailsScreen(
            character = state.data,
            onBackClick = onBackClick,
            onFavoriteClick = viewModel::toggleFavorite
        )
    }
}

@Composable
fun LocationsStateScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> ErrorScreen(
            onRetryClick = viewModel::loadLocations
        )

        state.isLoading -> LoadingScreen(
            onClick = viewModel::showError
        )

        state.data != null -> LocationsScreen(
            locations = state.data,
            onLocationClick = onLocationClick
        )
    }
}

@Composable
fun LocationDetailsStateScreen(
    onBackClick: () -> Unit,
    viewModel: LocationDetailsViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    when {
        state.hasError -> ErrorScreen(
            onRetryClick = viewModel::loadLocation
        )

        state.isLoading -> LoadingScreen(
            onClick = viewModel::showError
        )

        state.data != null -> LocationDetailsScreen(
            location = state.data,
            onBackClick = onBackClick
        )
    }
}