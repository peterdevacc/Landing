package com.peter.landing.ui.ipa

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.R
import com.peter.landing.data.local.ipa.Ipa

@Composable
fun IpaListItem(
    ipa: Ipa,
    playPron: (String) -> Unit,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = ipa.text,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.padding(end = 16.dp))
        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.padding(start = 32.dp))
        Column(
            modifier = Modifier.weight(3f)
        ) {
            Text(
                text = ipa.exampleWordSpelling,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
            Text(
                text = ipa.exampleWordIpa,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(
            onClick = { playPron(ipa.exampleWordPronName) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sound_24dp),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
