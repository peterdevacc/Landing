package com.peter.landing.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.util.DataResult

@Composable
fun ErrorNotice(code: DataResult.Error.Code) {
    val reason = when (code) {
        DataResult.Error.Code.UNKNOWN -> stringResource(R.string.error_unknown)
        DataResult.Error.Code.IO -> stringResource(R.string.error_io)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.error)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.error),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onError,
            modifier = Modifier
                .size(108.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Text(
            text = reason,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onError,
            maxLines = 3
        )
    }
}
