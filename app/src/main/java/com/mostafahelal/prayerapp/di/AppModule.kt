package com.mostafahelal.prayerapp.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mostafahelal.prayerapp.prayer.data.local.LocalDataSource
import com.mostafahelal.prayerapp.prayer.data.local.room.AppDatabase
import com.mostafahelal.prayerapp.prayer.data.local.room.ReminderDao
import com.mostafahelal.prayerapp.prayer.data.remote.RemoteDataSource
import com.mostafahelal.prayerapp.prayer.data.remote.network.PrayerService
import com.mostafahelal.prayerapp.prayer.data.repository.PrayerRepositoryImpl
import com.mostafahelal.prayerapp.prayer.domin.repo.PrayerRepository
import com.mostafahelal.prayerapp.qibla.data.network.QiblaService
import com.mostafahelal.prayerapp.qibla.data.qiblaRepository.QuiblaRepositoryImp
import com.mostafahelal.prayerapp.qibla.domin.qiblaRepo.QiblaRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideRetrofitClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    fun providePrayerService(client: OkHttpClient): PrayerService {
        val retrofit = Retrofit.Builder().baseUrl("https://api.aladhan.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
        return retrofit.create(PrayerService::class.java)
    }

    @Provides
    fun provideQiblaServices(client: OkHttpClient): QiblaService {
        val retrofit = Retrofit.Builder().baseUrl("https://api.aladhan.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
        return retrofit.create(QiblaService::class.java)
    }

    @Provides
    fun provideQiblaRepository(service: QiblaService): QiblaRepository =
        QuiblaRepositoryImp(service)

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        AppDatabase.getInstance(appContext)

    @Provides
    fun provideReminderDao(appDatabase: AppDatabase): ReminderDao = appDatabase.reminderDao()

    @Provides
    fun provideRemoteDataSource(
        service: PrayerService
    ): RemoteDataSource = RemoteDataSource(service)

    @Provides
    fun provideLocalDataSource(reminderDao: ReminderDao) = LocalDataSource(reminderDao)

    @Provides
    fun providePrayerRepository(
        remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource
    ): PrayerRepository = PrayerRepositoryImpl(remoteDataSource, localDataSource)

}