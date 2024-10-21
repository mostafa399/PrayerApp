package com.mostafahelal.prayerapp.prayer.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mostafahelal.prayerapp.prayer.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    fun getAllReminder(): Flow<List<ReminderEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAllReminder(news: List<ReminderEntity>)

    @Update
    suspend fun updateReminder(news: ReminderEntity)

}