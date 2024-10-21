package com.mostafahelal.prayerapp.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.firebase.Timestamp
import com.mostafahelal.prayerapp.R
import com.mostafahelal.prayerapp.prayer.domin.model.DateSchedule
import com.mostafahelal.prayerapp.utils.TimeUtils.fullDate

@Composable
fun ItemMainDate(
    hijriDate: DateSchedule,
    onbackpreess:()->Unit,
    onforwardpress:()->Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (iconBack, title, iconForward) = createRefs()
        ActionButton(modifier = Modifier.constrainAs(iconBack) {
            start.linkTo(parent.start, margin = 8.dp)
            bottom.linkTo(parent.bottom)
            top.linkTo(parent.top)
        }, icon = R.drawable.ic_back) {
            onbackpreess()
        }
        Column(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(iconBack.end)
                end.linkTo(iconForward.start)
                width = Dimension.fillToConstraints
            }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            TextHeadingLarge(text = Timestamp.now().fullDate)
            TextBody(text = buildString {
                append("${hijriDate.day} ${hijriDate.monthDesignation} ${hijriDate.year} ${hijriDate.yearDesignation}")
            })
            Spacer(modifier = Modifier.height(32.dp))
        }

        ActionButton(modifier = Modifier.constrainAs(iconForward) {
            end.linkTo(parent.end, margin = 8.dp)
            bottom.linkTo(parent.bottom)
            top.linkTo(parent.top)
        }, icon = R.drawable.ic_forward) {
            onforwardpress()
        }
    }
}
val dummyCalendar = DateSchedule(
    1,
    7,
    "Ramadhan",
    1443,
    "AH",
    "Sunday",
    "01-09-1443",
    listOf("1st Day of Ramadhan")
)
@Preview(showBackground = true)
@Composable
private fun PreviewItemMainDate() {
    ItemMainDate(dummyCalendar,{},{})
}