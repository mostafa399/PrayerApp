package com.mostafahelal.prayerapp.componets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.Timestamp
import com.mostafahelal.prayerapp.R
import com.mostafahelal.prayerapp.prayer.domin.model.Prayer
import com.mostafahelal.prayerapp.prayer.domin.model.TimingSchedule
import com.mostafahelal.prayerapp.prayer.domin.model.getNearestSchedule
import com.mostafahelal.prayerapp.ui.theme.Primary
import com.mostafahelal.prayerapp.ui.theme.White
import com.mostafahelal.prayerapp.utils.TimeUtils.hour

@Composable
fun ItemTimingSchedule(
    timingSchedule: TimingSchedule,
    timeNextPray: String,
    descNextPray: String,
    locationAddress: String,
    getInterval: (timingSchedule: TimingSchedule, nearestSchedule: Prayer) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = Primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        val nearestSchedule = timingSchedule.getNearestSchedule(Timestamp.now())
        if (nearestSchedule.time != "-") {
            if (timeNextPray == "-") getInterval(timingSchedule, nearestSchedule)
        }

        Box {
            Image(
                painterResource(id = R.drawable.ic_bg_schedule),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            ConstraintLayout(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                val (textTimeNextPray, textNextPray, textNearestScheduleName, textNearestScheduleTime, animation, textLocation) = createRefs()

                TextSubtitle(
                    modifier = Modifier.constrainAs(textTimeNextPray) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end)
                    }, text = timeNextPray, textColor = White
                )
                TextBody(
                    modifier = Modifier.constrainAs(textNearestScheduleName) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 16.dp)
                    }, text = descNextPray, textColor = White
                )
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        if (Timestamp.now().hour in (19..23) || Timestamp.now().hour in (0..5)) R.raw.an_night else R.raw.an_day
                    )
                )
                LottieAnimation(composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.constrainAs(animation) {
                        top.linkTo(textTimeNextPray.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.ratio("1:1")
                        height = Dimension.value(150.dp)
                    })

                TextSubtitle(
                    modifier = Modifier.constrainAs(textNextPray) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start)
                    }, text = "Next Prayer Time", textColor = White
                )

                TextHeadingXLarge(
                    modifier = Modifier.constrainAs(textNearestScheduleTime) {
                        top.linkTo(animation.top, margin = 64.dp)
                        bottom.linkTo(textLocation.top, margin = 16.dp)
                        start.linkTo(parent.start)
                    }, text = nearestSchedule.time, textColor = White
                )
                Row(modifier = Modifier.constrainAs(textLocation) {
                    bottom.linkTo(animation.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(animation.start)
                    width = Dimension.fillToConstraints
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "",
                        modifier = Modifier.size(16.dp)
                    )
                    TextSubtitle(
                        text = locationAddress, textColor = White
                    )
            }
        }
    }
}
}
val dummyTimingSchedule = TimingSchedule(
    Prayer("04:40 (WIB)", false),
    Prayer("04:50 (WIB)", true),
    Prayer("05:51 (WIB)", false),
    Prayer("11:44 (WIB)", false),
    Prayer("15:06 (WIB)", true),
    Prayer("17:37 (WIB)", false),
    Prayer("18:38 (WIB)", false),
)
    const val dummyLocationAddress = "Kecamatan Suranenggala, Kabupaten Cirebon"
    const val dummyNextPray = "1h 18m 3s "
    const val dummyDescNextPray = "Asr"

@SuppressLint("MissingPermission")
@Preview
@Composable
private fun PreviewItemTimingSchedule() {
    LazyColumn {
        item {
            ItemTimingSchedule(
                dummyTimingSchedule,
                dummyNextPray,
                dummyDescNextPray,
                dummyLocationAddress,
            ) { _, _ -> }
        }
    }
}