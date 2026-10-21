package plat.lab1.myapplication.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import plat.lab1.myapplication.data.room.dao.CharacterDao
import plat.lab1.myapplication.data.room.dao.LocationDao
import plat.lab1.myapplication.data.room.entity.CharacterEntity
import plat.lab1.myapplication.data.room.entity.LocationEntity

@Database(
    entities = [
        CharacterEntity::class,
        LocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rick_morty_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}