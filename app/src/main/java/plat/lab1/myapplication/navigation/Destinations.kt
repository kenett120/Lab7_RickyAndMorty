package plat.lab1.myapplication.navigation

import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

@Serializable
data object CharactersDestination

@Serializable
data class CharacterDetailsDestination(
    val id: Int
)