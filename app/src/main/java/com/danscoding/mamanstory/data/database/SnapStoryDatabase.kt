package com.danscoding.mamanstory.data.database

import androidx.room.Database
import com.danscoding.mamanstory.data.SnapStoryResponseItem

@Database(
    entities = [SnapStoryResponseItem::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class SnapStoryDatabase {
    abstract fun storyDao(): SnapStoryDao
    abstract fun remoteKeysDao(): RemoteKeyDao
}