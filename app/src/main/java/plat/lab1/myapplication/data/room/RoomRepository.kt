package plat.lab1.myapplication.data.room

import android.content.Context
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import plat.lab1.myapplication.data.Character
import plat.lab1.myapplication.data.CharacterDb
import plat.lab1.myapplication.data.Location
import plat.lab1.myapplication.data.LocationDb
import plat.lab1.myapplication.data.room.mapper.toEntity
import plat.lab1.myapplication.data.room.mapper.toModel

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
        database.withTransaction {
            val charactersCount = characterDao.countCharacters()
            val locationsCount = locationDao.countLocations()

            if (charactersCount == 0) {
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { it.toEntity() }

                characterDao.insertAll(characters)
            }

            if (locationsCount == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { it.toEntity() }

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
}