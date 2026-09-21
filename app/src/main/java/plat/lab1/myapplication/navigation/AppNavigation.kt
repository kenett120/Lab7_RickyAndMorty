package plat.lab1.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab1.myapplication.data.CharacterDb
import plat.lab1.myapplication.ui.screens.characters.CharactersScreen
import plat.lab1.myapplication.ui.screens.details.CharacterDetailsScreen
import plat.lab1.myapplication.ui.screens.login.LoginScreen


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val characterDb = remember {
        CharacterDb()
    }

    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(CharactersDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<CharactersDestination> {
            CharactersScreen(
                characters = characterDb.getAllCharacters(),
                onCharacterClick = { characterId ->
                    navController.navigate(
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

            val character = characterDb.getCharacterById(
                id = destination.id
            )

            CharacterDetailsScreen(
                character = character,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}