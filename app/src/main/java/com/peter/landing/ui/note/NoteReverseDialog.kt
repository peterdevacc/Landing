package com.peter.landing.ui.note

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.landing.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteReverseDialog(
    removedNoteList: List<Pair<String, Long>>,
    addNote: (String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                    text = "临时记录",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(288.dp)
                ) {
                    if (removedNoteList.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "点击对应的单词可以恢复生词记录",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                            )
                            FlowRow(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
                            ) {
                                removedNoteList.forEach {
                                    OutlinedButton(
                                        onClick = { addNote(it.first, it.second) },
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(text = it.first)
                                    }
                                }
                            }
                        }

                    } else {
                        Text(
                            text = "已删除生词的会出现在这里",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
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
