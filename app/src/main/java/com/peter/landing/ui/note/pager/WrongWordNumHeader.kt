package com.peter.landing.ui.note.pager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WrongWordNumHeader(num: Long) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "课时 - $num",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    }
}
