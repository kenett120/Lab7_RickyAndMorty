package plat.lab1.myapplication.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import plat.lab1.myapplication.ui.components.CharactersStateScreen
import plat.lab1.myapplication.ui.components.CharacterDetailsStateScreen
import plat.lab1.myapplication.ui.components.LocationsStateScreen
import plat.lab1.myapplication.ui.components.LocationDetailsStateScreen
import plat.lab1.myapplication.ui.screens.login.LoginScreen
import plat.lab1.myapplication.ui.screens.main.BottomNavigationBar
import plat.lab1.myapplication.ui.screens.profile.ProfileScreen
import plat.lab1.myapplication.ui.viewmodel.SessionViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val sessionViewModel: SessionViewModel = viewModel()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val isMainScreen = currentDestination?.hierarchy?.any {
        it.route == MainDestination::class.qualifiedName
    } == true

    val selectedItem = when {
        currentDestination?.hierarchy?.any {
            it.route == LocationsGraph::class.qualifiedName
        } == true -> 1

        currentDestination?.route ==
                ProfileDestination::class.qualifiedName -> 2

        else -> 0
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (isMainScreen) {
                BottomNavigationBar(
                    selectedItem = selectedItem,

                    onCharactersClick = {
                        if (selectedItem != 0) {
                            navController.popBackStack(
                                route = CharactersDestination,
                                inclusive = false
                            )
                        }
                    },

                    onLocationsClick = {
                        if (selectedItem != 1) {
                            navController.navigate(LocationsGraph) {
                                popUpTo(CharactersDestination) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    },

                    onProfileClick = {
                        if (selectedItem != 2) {
                            navController.navigate(ProfileDestination) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = SessionDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<SessionDestination> {
                LaunchedEffect(sessionState.isLoading) {
                    if (!sessionState.isLoading) {
                        if (sessionState.userName.isNullOrBlank()) {
                            navController.navigate(LoginDestination) {
                                popUpTo<SessionDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(MainDestination) {
                                popUpTo<SessionDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            composable<LoginDestination> {
                LoginScreen(
                    onStartClick = { name ->
                        sessionViewModel.login(name) {
                            navController.navigate(MainDestination) {
                                popUpTo<LoginDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            navigation<MainDestination>(
                startDestination = CharactersGraph
            ) {
                navigation<CharactersGraph>(
                    startDestination = CharactersDestination
                ) {
                    composable<CharactersDestination> {
                        CharactersStateScreen(
                            onCharacterClick = { characterId ->
                                navController.navigate(
                                    CharacterDetailsDestination(
                                        id = characterId
                                    )
                                )
                            }
                        )
                    }

                    composable<CharacterDetailsDestination> {
                        CharacterDetailsStateScreen(
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                }

                navigation<LocationsGraph>(
                    startDestination = LocationsDestination
                ) {
                    composable<LocationsDestination> {
                        LocationsStateScreen(
                            onLocationClick = { locationId ->
                                navController.navigate(
                                    LocationDetailsDestination(
                                        id = locationId
                                    )
                                )
                            }
                        )
                    }

                    composable<LocationDetailsDestination> {
                        LocationDetailsStateScreen(
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }
                }

                composable<ProfileDestination> {
                    ProfileScreen(
                        userName = sessionState.userName.orEmpty(),
                        onLogoutClick = {
                            sessionViewModel.logout {
                                navController.navigate(LoginDestination) {
                                    popUpTo<MainDestination> {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}