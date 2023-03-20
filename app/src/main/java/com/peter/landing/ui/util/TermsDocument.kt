package com.peter.landing.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.terms.Terms

@Composable
fun TermsDocument(
    terms: Terms,
    modifier: Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .then(Modifier)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        itemsIndexed(terms.data) { key, data ->
            when (data.type) {
                Terms.Item.Type.TITLE -> {
                    val paddingValues = if (key == 0) {
                        PaddingValues(bottom = 4.dp)
                    } else {
                        PaddingValues(top = 8.dp, bottom = 4.dp)
                    }
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth()
                    )
                }
                Terms.Item.Type.TEXT -> {
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    )
                }
                Terms.Item.Type.SIGNATURE -> {
                    Text(
                        text = data.text,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
