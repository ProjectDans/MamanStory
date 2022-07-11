package com.danscoding.mamanstory.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danscoding.mamanstory.data.SnapStoryResponseItem

@Dao
interface SnapStoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: SnapStoryResponseItem)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, SnapStoryResponseItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}