package com.peter.landing.ui.study

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Counter(
    current: Int,
    totalNum: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$current / $totalNum",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleSmall
        )
    }
}
