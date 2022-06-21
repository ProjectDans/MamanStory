package com.danscoding.mamanstory.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.danscoding.mamanstory.response.LoginStoryResult
import kotlinx.coroutines.flow.map

class PreferenceStoryAccount private constructor(private val dataStoreStoryApp: DataStore<Preferences>){

    fun getAccountStory(): kotlinx.coroutines.flow.Flow<LoginStoryResult>{
        return dataStoreStoryApp.data.map { preferences ->
            LoginStoryResult(
                preferences[ID_ACCOUNT_STORY_KEY]?:"",
                preferences[NAME_ACCOUNT_STORY_KEY]?:"",
                preferences[TOKEN_ACCOUNT_STORY_KEY]?:"",
                preferences[STATE_ACCOUNT_STORY_KEY]?:false

            )
        }
    }

    suspend fun storyAccountSave(storyLogin: LoginStoryResult){
        dataStoreStoryApp.edit { preferences ->
            preferences[ID_ACCOUNT_STORY_KEY] = storyLogin.userId
            preferences[NAME_ACCOUNT_STORY_KEY]= storyLogin.name
            preferences[TOKEN_ACCOUNT_STORY_KEY]= storyLogin.token
            preferences[STATE_ACCOUNT_STORY_KEY]= storyLogin.isLogin
        }
    }

    suspend fun storyLoginApp(){
        dataStoreStoryApp.edit { preferences ->
            preferences[STATE_ACCOUNT_STORY_KEY] = true
        }
    }

    suspend fun storyLogoutApp(){
        dataStoreStoryApp.edit { preferences ->
            preferences[STATE_ACCOUNT_STORY_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE_STORY: PreferenceStoryAccount? = null

        private val ID_ACCOUNT_STORY_KEY = stringPreferencesKey("userid")
        private val NAME_ACCOUNT_STORY_KEY = stringPreferencesKey("name")
        private val TOKEN_ACCOUNT_STORY_KEY = stringPreferencesKey("token")
        private val STATE_ACCOUNT_STORY_KEY = booleanPreferencesKey("state")

        fun getStoryApp(dataStore: DataStore<Preferences>): PreferenceStoryAccount {
            return INSTANCE_STORY ?: synchronized(this) {
                val instance = PreferenceStoryAccount(dataStore)
                INSTANCE_STORY = instance
                instance
            }
        }
    }

}