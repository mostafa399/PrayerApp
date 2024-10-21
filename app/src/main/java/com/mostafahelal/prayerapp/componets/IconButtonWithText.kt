import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mostafahelal.prayerapp.R
import com.mostafahelal.prayerapp.prayer.domin.model.getScheduleName
import com.mostafahelal.prayerapp.componets.ActionButton
import com.mostafahelal.prayerapp.componets.TextBody
import com.mostafahelal.prayerapp.componets.TextSubtitle
import com.mostafahelal.prayerapp.ui.theme.Black10
import com.mostafahelal.prayerapp.ui.theme.Gray
import com.mostafahelal.prayerapp.ui.theme.PrayerThemes
import com.mostafahelal.prayerapp.ui.theme.Primary
import com.mostafahelal.prayerapp.ui.theme.Primary10

@Composable
fun IconTextButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .padding(8.dp).clickable {
                                     onClick()
            },
        shape = RoundedCornerShape(16.dp),
        backgroundColor = PrayerThemes.Colors.primary,
        elevation = 1.dp
    ) {
        Row(
            Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            TextBody(text = "Show Qibla Direction On Map", textColor = Color.White)
            Spacer(modifier = Modifier.weight(5f))
            ActionButton(icon = R.drawable.ic_compas, type = 1) {
                
            }

        }
    }
}
@Preview
@Composable
fun PreviewButton(){
    IconTextButton(){

    }
}
