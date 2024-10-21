package com.mostafahelal.prayerapp.prayer.domin.repo

import com.mostafahelal.prayerapp.prayer.domin.model.PrayerReminder
import com.mostafahelal.prayerapp.prayer.domin.model.Schedule
import com.mostafahelal.prayerapp.utils.States
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    suspend fun getSchedule(lat: Double, long: Double, month: Int, year: Int): Flow<States<List<Schedule>>>
    suspend fun getAllReminder(): Flow<List<PrayerReminder>>
    suspend fun addAllReminders(listOfReminder: List<PrayerReminder>)
    suspend fun updateReminder(prayerReminder: PrayerReminder)

}