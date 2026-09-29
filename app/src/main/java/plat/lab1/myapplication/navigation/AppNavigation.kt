package plat.lab1.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab1.myapplication.data.CharacterDb
import plat.lab1.myapplication.data.LocationDb
import plat.lab1.myapplication.ui.screens.characters.CharactersScreen
import plat.lab1.myapplication.ui.screens.details.CharacterDetailsScreen
import plat.lab1.myapplication.ui.screens.locationdetails.LocationDetailsScreen
import plat.lab1.myapplication.ui.screens.locations.LocationsScreen
import plat.lab1.myapplication.ui.screens.login.LoginScreen
import plat.lab1.myapplication.ui.screens.main.MainScreen
import plat.lab1.myapplication.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val characterDb = remember {
        CharacterDb()
    }

    val locationDb = remember {
        LocationDb()
    }

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
                currentDestination?.route?.contains("Locations") == true -> 1
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
                            CharactersScreen(
                                characters = characterDb.getAllCharacters(),
                                onCharacterClick = { characterId ->
                                    mainNavController.navigate(
                                        CharacterDetailsDestination(
                                            id = characterId
                                        )
                                    )
                                }
                            )
                        }

                        composable<CharacterDetailsDestination> { backStackEntry ->
                            val destination =
                                backStackEntry.toRoute<CharacterDetailsDestination>()

                            val character =
                                characterDb.getCharacterById(
                                    id = destination.id
                                )

                            CharacterDetailsScreen(
                                character = character,
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
                            LocationsScreen(
                                locations = locationDb.getAllLocations(),
                                onLocationClick = { locationId ->
                                    mainNavController.navigate(
                                        LocationDetailsDestination(
                                            id = locationId
                                        )
                                    )
                                }
                            )
                        }

                        composable<LocationDetailsDestination> { backStackEntry ->
                            val destination =
                                backStackEntry.toRoute<LocationDetailsDestination>()

                            val location =
                                locationDb.getLocationById(
                                    id = destination.id
                                )

                            LocationDetailsScreen(
                                location = location,
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