package com.danscoding.mamanstory.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountTokenPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val KEY_TOKEN = stringPreferencesKey("token")

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN] ?: null
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    suspend fun removeToken() {
        dataStore.edit {
            it.clear()
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: AccountTokenPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): AccountTokenPreference {
            return INSTANCE ?: synchronized(this) {
                val instance =AccountTokenPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}