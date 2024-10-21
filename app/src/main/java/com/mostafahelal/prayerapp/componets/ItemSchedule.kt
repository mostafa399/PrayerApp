package com.mostafahelal.prayerapp.componets

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.mostafahelal.prayerapp.prayer.domin.model.Prayer
import com.mostafahelal.prayerapp.prayer.domin.model.TimingSchedule
import com.mostafahelal.prayerapp.prayer.domin.model.getNearestSchedule
import com.mostafahelal.prayerapp.prayer.domin.model.getScheduleName
import com.mostafahelal.prayerapp.ui.theme.Black10
import com.mostafahelal.prayerapp.ui.theme.Gray
import com.mostafahelal.prayerapp.ui.theme.PrayerThemes
import com.mostafahelal.prayerapp.ui.theme.Primary
import com.mostafahelal.prayerapp.ui.theme.Primary10


@Composable
fun ItemSchedule(
    prayer: Prayer,
    timingSchedule: TimingSchedule,
) {
    val nearestSchedule = timingSchedule.getNearestSchedule(Timestamp.now())
    val isNowSchedule = nearestSchedule.time == prayer.time
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            if (isNowSchedule) 2.dp else 1.dp,
            if (isNowSchedule) Primary else {
                if (isSystemInDarkTheme()) Gray else Black10
            }
        ),
        backgroundColor = if (isNowSchedule) Primary10 else PrayerThemes.Colors.surface,
        elevation = if (isNowSchedule) 0.dp else 1.dp
    ) {
        Row(
            Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            TextSubtitle(text = timingSchedule.getScheduleName(prayer))
            Spacer(modifier = Modifier.weight(5f))
            TextBody(text = prayer.time)
            Spacer(modifier = Modifier.width(24.dp))

        }
    }
}


@Preview(showBackground = false)
@Composable
private fun PreviewItemSchedule() {
    ItemSchedule(
        dummyTimingSchedule.fajr,
        dummyTimingSchedule
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewItemScheduleDark() {
    ItemSchedule(
        dummyTimingSchedule.fajr,
        dummyTimingSchedule
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewItemScheduleSelected() {
    ItemSchedule(
        dummyTimingSchedule.imsak,
        dummyTimingSchedule
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewItemScheduleSelectedDark() {
    ItemSchedule(
        dummyTimingSchedule.imsak,
        dummyTimingSchedule
    )
}