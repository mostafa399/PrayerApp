package com.mostafahelal.prayerapp.prayer.data.repository

import com.mostafahelal.prayerapp.prayer.data.local.LocalDataSource
import com.mostafahelal.prayerapp.prayer.data.local.entity.toPayerReminders
import com.mostafahelal.prayerapp.prayer.data.remote.RemoteDataSource
import com.mostafahelal.prayerapp.prayer.domin.model.PrayerReminder
import com.mostafahelal.prayerapp.prayer.domin.model.Schedule
import com.mostafahelal.prayerapp.prayer.domin.model.toReminderEntities
import com.mostafahelal.prayerapp.prayer.domin.model.toReminderEntity
import com.mostafahelal.prayerapp.prayer.domin.repo.PrayerRepository
import com.mostafahelal.prayerapp.utils.States
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrayerRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource

): PrayerRepository {
    override suspend fun getSchedule(
        lat: Double,
        long: Double,
        month: Int,
        year: Int
    ): Flow<States<List<Schedule>>> = remoteDataSource.getSchedule(lat, long, month, year)

    override suspend fun getAllReminder(): Flow<List<PrayerReminder>> =localDataSource.getAllReminder().map { it.toPayerReminders() }

    override suspend fun addAllReminders(listOfReminder: List<PrayerReminder>) =localDataSource.addAllReminder(listOfReminder.toReminderEntities())

    override suspend fun updateReminder(prayerReminder: PrayerReminder) =  localDataSource.updateReminder(prayerReminder.toReminderEntity())
}