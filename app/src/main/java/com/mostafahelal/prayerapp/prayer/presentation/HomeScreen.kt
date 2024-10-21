package com.mostafahelal.prayerapp.prayer.presentation

import IconTextButton
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mostafahelal.prayerapp.prayer.domin.model.DateSchedule
import com.mostafahelal.prayerapp.prayer.domin.model.Prayer
import com.mostafahelal.prayerapp.prayer.domin.model.TimingSchedule
import com.mostafahelal.prayerapp.componets.ItemMainDate
import com.mostafahelal.prayerapp.componets.ItemSchedule
import com.mostafahelal.prayerapp.componets.ItemTimingSchedule
import com.mostafahelal.prayerapp.componets.dummyCalendar
import com.mostafahelal.prayerapp.componets.dummyDescNextPray
import com.mostafahelal.prayerapp.componets.dummyLocationAddress
import com.mostafahelal.prayerapp.componets.dummyNextPray
import com.mostafahelal.prayerapp.componets.dummyTimingSchedule

@Composable
fun HomePage(
    calendar: DateSchedule,
    timingSchedule: TimingSchedule,
    goToNextDay:()->Unit,
    goToPreviousDay:()->Unit,
    timeNextPray: String,
    descNextPray: String,
    locationAddress: String,
    getInterval: (timingSchedule: TimingSchedule, nearestSchedule: Prayer) -> Unit,
    onShowQiblaDirection: () -> Unit
    ) {
    Scaffold(Modifier.padding(16.dp)){
        LazyColumn(contentPadding = it){
            item { ItemMainDate(calendar, onbackpreess = {

            }, onforwardpress = {

            }) }
            item {
                ItemTimingSchedule(
                    timingSchedule,
                    timeNextPray,
                    descNextPray,
                    locationAddress,
                    getInterval
                )
            }
            item { ItemSchedule(timingSchedule.imsak, timingSchedule) }
            item { ItemSchedule(timingSchedule.fajr, timingSchedule) }
            item { ItemSchedule(timingSchedule.dhuhr, timingSchedule) }
            item { ItemSchedule(timingSchedule.asr, timingSchedule) }
            item { ItemSchedule(timingSchedule.maghrib, timingSchedule) }
            item { ItemSchedule(timingSchedule.isha, timingSchedule) }
            item { IconTextButton(){
                onShowQiblaDirection()
            } }

        }
    }

}
@SuppressLint("MissingPermission")
@Preview
@Composable
private fun PreviewHomePage() {
    HomePage(
        dummyCalendar, dummyTimingSchedule, goToNextDay = {}, goToPreviousDay = {},
        dummyNextPray, dummyDescNextPray, dummyLocationAddress, { _, _ -> }, onShowQiblaDirection = {}
    )
}