package pratilipi.demo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pratilipi.demo.database.CustomerListEntity


@Database(entities = [CustomerListEntity::class], version = 2, exportSchema = false)
abstract class PratilipiDB : RoomDatabase() {
    abstract fun pratilipiDao(): PratilipiDao?
}