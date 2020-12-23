package pratilipi.demo.di.model

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import pratilipi.demo.db.PratilipiDB
import pratilipi.demo.db.PratilipiDao
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PratilipiDataBaseModule {

    @Provides
    @Singleton
    fun provideJustCleanDB(application: Application?): PratilipiDB {
        return Room.databaseBuilder(application!!, PratilipiDB::class.java, "Contact Database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providePostsDao(pratilipiDB: PratilipiDB): PratilipiDao {
        return pratilipiDB.pratilipiDao()!!
    }
}