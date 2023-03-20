package com.peter.landing.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutInfoSection(
    heading: String,
    infoList: List<(@Composable () -> Unit)>
) {
    Column(modifier = Modifier) {
        Text(
            text = heading,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        infoList.forEach { infoItem ->
            infoItem()
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
