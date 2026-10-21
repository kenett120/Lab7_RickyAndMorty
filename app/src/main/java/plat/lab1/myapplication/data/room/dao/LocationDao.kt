package plat.lab1.myapplication.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import plat.lab1.myapplication.data.room.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations ORDER BY id")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun countLocations(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}