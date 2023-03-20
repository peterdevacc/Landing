package com.peter.landing.ui.greeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.peter.landing.R
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.util.TermsDocument

@Composable
fun TermsDialog(
    terms: Terms,
    onDismiss: () -> Unit
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.65f)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            TermsDocument(
                terms = terms,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 16.dp))
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 12.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        }
    }
}
