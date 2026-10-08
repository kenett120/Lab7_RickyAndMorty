package plat.lab1.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import plat.lab1.myapplication.ui.screens.main.MainScreen
import plat.lab1.myapplication.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MainDestination> {
            val mainNavController = rememberNavController()

            val backStackEntry by
            mainNavController.currentBackStackEntryAsState()

            val currentDestination = backStackEntry?.destination

            val selectedItem = when {
                currentDestination?.route?.contains("Location") == true -> 1
                currentDestination?.route?.contains("Profile") == true -> 2
                else -> 0
            }

            MainScreen(
                selectedItem = selectedItem,

                onCharactersClick = {
                    mainNavController.navigate(CharactersGraph) {
                        launchSingleTop = true
                    }
                },

                onLocationsClick = {
                    mainNavController.navigate(LocationsGraph) {
                        launchSingleTop = true
                    }
                },

                onProfileClick = {
                    mainNavController.navigate(ProfileDestination) {
                        launchSingleTop = true
                    }
                }
            ) { paddingValues ->

                NavHost(
                    navController = mainNavController,
                    startDestination = CharactersGraph,
                    modifier = Modifier.padding(paddingValues)
                ) {

                    navigation<CharactersGraph>(
                        startDestination = CharactersDestination
                    ) {

                        composable<CharactersDestination> {
                            CharactersStateScreen(
                                onCharacterClick = { characterId ->
                                    mainNavController.navigate(
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
                                    mainNavController.navigateUp()
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
                                    mainNavController.navigate(
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
                                    mainNavController.navigateUp()
                                }
                            )
                        }
                    }

                    composable<ProfileDestination> {
                        ProfileScreen(
                            onLogoutClick = {
                                navController.navigate(LoginDestination) {
                                    popUpTo<MainDestination> {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}