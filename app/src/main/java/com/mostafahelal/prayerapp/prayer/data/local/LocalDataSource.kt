package com.mostafahelal.prayerapp.prayer.data.local

import com.mostafahelal.prayerapp.prayer.data.local.entity.ReminderEntity
import com.mostafahelal.prayerapp.prayer.data.local.room.ReminderDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val reminderDao: ReminderDao
)  {
    fun getAllReminder(): Flow<List<ReminderEntity>> = reminderDao.getAllReminder()
    suspend fun addAllReminder(reminders : List<ReminderEntity>) = reminderDao.addAllReminder(reminders)
    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)


}