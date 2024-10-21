package com.mostafahelal.prayerapp.prayer.presentation.viewmodels

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.mostafahelal.prayerapp.prayer.domin.model.Prayer
import com.mostafahelal.prayerapp.prayer.domin.model.PrayerReminder
import com.mostafahelal.prayerapp.prayer.domin.model.Schedule
import com.mostafahelal.prayerapp.prayer.domin.model.TimingSchedule
import com.mostafahelal.prayerapp.prayer.domin.model.getScheduleName
import com.mostafahelal.prayerapp.prayer.domin.model.hour
import com.mostafahelal.prayerapp.prayer.domin.model.minutes
import com.mostafahelal.prayerapp.prayer.domin.model.toList
import com.mostafahelal.prayerapp.prayer.domin.model.toTimingSchedule
import com.mostafahelal.prayerapp.prayer.domin.repo.PrayerRepository
import com.mostafahelal.prayerapp.utils.States
import com.mostafahelal.prayerapp.utils.TimeUtils.day
import com.mostafahelal.prayerapp.utils.TimeUtils.hour
import com.mostafahelal.prayerapp.utils.TimeUtils.month
import com.mostafahelal.prayerapp.utils.TimeUtils.year
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PrayerRepository,
) : ViewModel() {

    var currentScheduleC = MutableStateFlow(Schedule.EMPTY)
    var currentSchedule by mutableStateOf(Schedule.EMPTY)

    var timingScheduleC = MutableStateFlow(TimingSchedule.EMPTY)
    var timingSchedule by mutableStateOf(TimingSchedule.EMPTY)
    private var currentTimingSchedule = TimingSchedule.EMPTY
    var isLoading by mutableStateOf(true)

    var nextPray by mutableStateOf("-")
    var descNextPray by mutableStateOf("-")
    private lateinit var countDownTimer: CountDownTimer

    var locationAddress by mutableStateOf("-")
    fun getPrayerSchedule(lat: Double, long: Double, date: Timestamp) = viewModelScope.launch {
        repository.getSchedule(lat, long, date.month, date.year).collect {
            when (it) {
                is States.Loading -> {
                    Log.d("TAG", "getPrayerSchedule: loading")
                    isLoading = true
                }

                is States.Success -> {
                    isLoading = false
                    it.data.find { sc ->
                        sc.georgianDate.day == if (date.hour > 20) date.day + 1 else date.day
                    }?.let { schedule ->
                        viewModelScope.launch {
                            currentScheduleC.emit(schedule)
                            currentSchedule = schedule
                        }
                        getReminderPrayer(schedule.timingSchedule)
                        viewModelScope.launch {
                            timingSchedule = currentTimingSchedule
                            timingScheduleC.emit(currentTimingSchedule)
                        }
                    }
                }

                is States.Failed -> {
                    isLoading = false
                    Log.d("TAG", "getPrayerSchedule: failed = ${it.message}")
                }
            }
        }
    }
    private fun getReminderPrayer(timingSchedule: TimingSchedule) = viewModelScope.launch {
        repository.getAllReminder().collect {
            if (it.isEmpty()) repository.addAllReminders(PrayerReminder.EMPTY)
            else updateReminder(it, timingSchedule)
        }
    }

    private fun updateReminder(
        listOfReminder: List<PrayerReminder>,
        timingSchedule: TimingSchedule
    ) {
        val listSchedule = timingSchedule.toList()
        if (listSchedule.isNotEmpty()) {
            listOfReminder.forEach { reminder ->
                listSchedule[reminder.index].isReminded = reminder.isReminded
            }
            currentTimingSchedule = listSchedule.toTimingSchedule()
            viewModelScope.launch { this@HomeViewModel.timingSchedule = TimingSchedule.EMPTY }
            viewModelScope.launch { this@HomeViewModel.timingSchedule = currentTimingSchedule }
        }
    }
    fun getIntervalText(timingSchedule: TimingSchedule, prayer: Prayer) = viewModelScope.launch {
        val now = Timestamp.now()
        val diff: Long = Date(
            Calendar.getInstance().apply {
                set(
                    Calendar.DAY_OF_MONTH,
                    if (now.hour > timingSchedule.isha.time.hour) now.day + 1 else now.day
                )
                set(Calendar.HOUR_OF_DAY, prayer.time.hour)
                set(Calendar.MINUTE, prayer.time.minutes)
            }.time.time
        ).time - now.toDate().time
        if (this@HomeViewModel::countDownTimer.isInitialized) countDownTimer.cancel()
        countDownTimer = object : CountDownTimer(diff, 1000) {
            override fun onTick(progress: Long) {
                val seconds = progress / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                nextPray = "${hours}h ${minutes - (hours * 60)}m ${seconds - (minutes * 60)}s"
                descNextPray = buildString {
                    append(" to ")
                    append(timingSchedule.getScheduleName(prayer))
                }
            }

            override fun onFinish() {
                if (this@HomeViewModel::countDownTimer.isInitialized) countDownTimer.cancel()
                nextPray = "Now"
                descNextPray = buildString {
                    append(" it's time to pray ")
                    append(timingSchedule.getScheduleName(prayer))
                }
            }
        }.start()
    }

    fun getLocationAddress(context: Context, location: Location) {
        viewModelScope.launch {
            Geocoder(context, Locale.getDefault()).apply {
                getFromLocation(location.latitude, location.longitude, 1)?.first()
                    ?.let { address ->
                        locationAddress = buildString {
                            append(address.locality).append(", ")
                            append(address.subAdminArea)
                        }
                    }
            }
        }
    }
}