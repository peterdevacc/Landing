package com.peter.landing.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.landing.R

@Composable
fun ExplainSection(
    spelling: String,
    explain: Map<String, List<String>>,
    isCN: Boolean = true,
    modifier: Modifier
) {
    val title = if (isCN) {
        "中文："
    } else {
        "英文："
    }
    val zoomState = remember { mutableStateOf(false) }

    if (zoomState.value) {
        ExplainZoomInDialog(
            spelling = spelling,
            explain = explain,
            onDismiss = { zoomState.value = false }
        )
    }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { zoomState.value = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_zoom_in_24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            explainList(explain)
        }
    }

}

@Composable
fun ExplainSection(
    explain: Map<String, List<String>>,
    isCN: Boolean = true,
    modifier: Modifier
) {
    val title = if (isCN) {
        "中文："
    } else {
        "英文："
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            explainList(explain)
        }
    }

}

@Composable
fun ExplainSection(
    cnExplain: Map<String, List<String>>,
    enExplain: Map<String, List<String>>,
    modifier: Modifier
) {

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                text = "中文：",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
        }
        explainList(cnExplain)

        item {
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Text(
                text = "英文：",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
        }
        explainList(enExplain)

    }

}

@Composable
fun ExplainZoomInDialog(
    spelling: String,
    explain: Map<String, List<String>>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = spelling,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(328.dp)
                ) {
                    explainList(explain)
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
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
}

private fun LazyListScope.explainList(
    explain: Map<String, List<String>>
) = items(explain.toList()) { part ->
    Text(
        text = part.first,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(top = 2.dp, bottom = 1.dp)
    )
    part.second.forEachIndexed { index, explain ->
        Text(
            text = "${index + 1}. $explain",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}


