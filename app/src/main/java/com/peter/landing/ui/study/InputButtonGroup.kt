package com.peter.landing.ui.study

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R

@Composable
fun InputButtonRow(
    pronName: String,
    pronButtonEnable: Boolean = true,
    playPron: (String) -> Unit,
    leftButtonEnable: Boolean,
    leftButtonIconId: Int,
    leftButtonAction: () -> Unit,
    rightButtonEnable: Boolean,
    rightButtonIconId: Int,
    rightButtonAction: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Button(
            enabled = leftButtonEnable,
            onClick = leftButtonAction,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                painter = painterResource(leftButtonIconId),
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Button(
            enabled = pronButtonEnable,
            onClick = { playPron(pronName) },
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sound_24dp),
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Button(
            enabled = rightButtonEnable,
            onClick = rightButtonAction,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                painter = painterResource(rightButtonIconId),
                contentDescription = "",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
