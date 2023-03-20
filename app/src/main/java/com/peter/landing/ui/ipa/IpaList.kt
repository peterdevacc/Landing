package com.peter.landing.ui.ipa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.ipa.Ipa

@Composable
fun IpaList(
    ipaList: List<Ipa>,
    playPron: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(ipaList) {
            IpaListItem(
                ipa = it,
                playPron = playPron,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            )
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
