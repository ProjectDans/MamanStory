package com.danscoding.mamanstory.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.danscoding.mamanstory.api.ApiConfiguration
import com.danscoding.mamanstory.api.ApiService
import com.danscoding.mamanstory.data.AppPreference
import com.danscoding.mamanstory.data.database.RemoteKeyDao
import com.danscoding.mamanstory.data.database.SnapStoryDao
import com.danscoding.mamanstory.data.database.SnapStoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("application")

@Module
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfiguration.getApiService()

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideUserPreferences(dataStore: DataStore<Preferences>): AppPreference =
        AppPreference(dataStore)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SnapStoryDatabase {
        return Room.databaseBuilder(
            context,
            SnapStoryDatabase::class.java,
            "story_database"
        ).build()
    }

    @Provides
    fun provideStoryDao(snapStoryDatabase: SnapStoryDatabase): SnapStoryDao = snapStoryDatabase.storyDao()

    @Provides
    fun provideKeyDao(snapStoryDatabase: SnapStoryDatabase): RemoteKeyDao = snapStoryDatabase.remoteKeysDao()
}