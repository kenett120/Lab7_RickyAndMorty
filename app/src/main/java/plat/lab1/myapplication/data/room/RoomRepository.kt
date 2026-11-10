package plat.lab1.myapplication.data.room

import android.content.Context
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import plat.lab1.myapplication.data.remote.RetrofitClient
import plat.lab1.myapplication.data.room.entity.CharacterEntity
import plat.lab1.myapplication.data.room.entity.LocationEntity
import plat.lab1.myapplication.data.room.mapper.toModel
import kotlinx.coroutines.delay
import retrofit2.HttpException

class RoomRepository(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val characterDao = database.characterDao()
    private val locationDao = database.locationDao()

    fun getCharacters() =
        characterDao.getAllCharacters().map { list ->
            list.map { it.toModel() }
        }

    fun getCharacter(id: Int) =
        characterDao.getCharacterById(id).map {
            it?.toModel()
        }

    fun getLocations() =
        locationDao.getAllLocations().map { list ->
            list.map { it.toModel() }
        }

    fun getLocation(id: Int) =
        locationDao.getLocationById(id).map {
            it?.toModel()
        }

    suspend fun updateFavorite(id: Int, favorite: Boolean) {
        characterDao.updateFavorite(id, favorite)
    }

    suspend fun synchronize() = withContext(Dispatchers.IO) {

        val charactersCount = characterDao.countCharacters()
        val locationsCount = locationDao.countLocations()

        if (charactersCount > 0 && locationsCount > 0) {
            return@withContext
        }

        val api = RetrofitClient.api

        val characters = mutableListOf<CharacterEntity>()
        var nextCharactersUrl: String? = null
        var firstCharactersPage = true

        do {
            val response = retryRequest {
                if (firstCharactersPage) {
                    api.getCharacters()
                } else {
                    api.getCharactersPage(nextCharactersUrl!!)
                }
            }

            firstCharactersPage = false

            delay(500)

            characters.addAll(
                response.results.map { character ->
                    CharacterEntity(
                        id = character.id,
                        name = character.name,
                        status = character.status,
                        species = character.species,
                        gender = character.gender,
                        image = character.image,
                        isFavorite = false
                    )
                }
            )

            nextCharactersUrl = response.info.next

        } while (nextCharactersUrl != null)

        val locations = mutableListOf<LocationEntity>()
        var nextLocationsUrl: String? = null
        var firstLocationsPage = true

        do {
            val response = retryRequest {
                if (firstLocationsPage) {
                    api.getLocations()
                } else {
                    api.getLocationsPage(nextLocationsUrl!!)
                }
            }

            firstLocationsPage = false

            delay(500)

            locations.addAll(
                response.results.map { location ->
                    LocationEntity(
                        id = location.id,
                        name = location.name,
                        type = location.type,
                        dimension = location.dimension,
                        isFavorite = false
                    )
                }
            )

            nextLocationsUrl = response.info.next

        } while (nextLocationsUrl != null)

        database.withTransaction {
            if (characterDao.countCharacters    () == 0) {
                characterDao.insertAll(characters)
            }

            if (locationDao.countLocations() == 0) {
                locationDao.insertAll(locations)
            }
        }
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        database.withTransaction {
            characterDao.deleteAll()
            locationDao.deleteAll()
        }
    }
    suspend fun createCharacter(
        name: String,
        status: String,
        species: String,
        gender: String,
        image: String
    ) = withContext(Dispatchers.IO) {

        database.withTransaction {
            val minimumId = characterDao.getMinimumId() ?: 0

            val newId = if (minimumId <= 0) {
                minimumId - 1
            } else {
                -1
            }

            characterDao.insertCharacter(
                CharacterEntity(
                    id = newId,
                    name = name,
                    status = status,
                    species = species,
                    gender = gender,
                    image = image,
                    isFavorite = false
                )
            )
        }
    }

    suspend fun updateCharacter(character: plat.lab1.myapplication.data.Character) {
        withContext(Dispatchers.IO) {
            characterDao.updateCharacter(
                CharacterEntity(
                    id = character.id,
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    gender = character.gender,
                    image = character.image,
                    isFavorite = character.isFavorite
                )
            )
        }
    }

    suspend fun deleteCharacter(id: Int) {
        withContext(Dispatchers.IO) {
            characterDao.deleteCharacter(id)
        }
    }
    private suspend fun <T> retryRequest(
        request: suspend () -> T
    ): T {
        var attempt = 0
        var waitTime = 2000L

        while (true) {
            try {
                return request()
            } catch (e: HttpException) {
                if (e.code() != 429 || attempt >= 4) {
                    throw e
                }

                delay(waitTime)
                waitTime *= 2
                attempt++
            }
        }
    }
    suspend fun updateLocationFavorite(
        id: Int,
        favorite: Boolean
    ) {
        locationDao.updateFavorite(id, favorite)
    }
}