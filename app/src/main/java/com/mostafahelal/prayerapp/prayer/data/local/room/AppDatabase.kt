package com.mostafahelal.prayerapp.prayer.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mostafahelal.prayerapp.prayer.data.local.entity.ReminderEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [ReminderEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "Prayer.db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        instance?.let {
                            GlobalScope.launch {
                                it.reminderDao().addAllReminder(ReminderEntity.INIT)
                            }
                        }
                    }
                }).fallbackToDestructiveMigration().build()
            }
    }
}