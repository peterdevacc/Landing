package com.peter.landing.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.peter.landing.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchHistoryDialog(
    searchHistory: Flow<PagingData<SearchHistoryItem>>,
    setWord: (String) -> Unit,
    removeSearchHistory: () -> Unit,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "查询记录",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = removeSearchHistory
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_forever_24dp),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(348.dp)
                ) {
                    val pagingItems = searchHistory.collectAsLazyPagingItems()
                    if (pagingItems.itemCount != 0) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(pagingItems) { item ->
                                if (item != null) {
                                    when (item) {
                                        is SearchHistoryItem.ItemSearchHistory -> {
                                            SearchHistorySpelling(
                                                spelling = item.searchHistory.input,
                                                onClick = {
                                                    setWord(item.searchHistory.input)
                                                }
                                            )
                                        }
                                        is SearchHistoryItem.SeparatorSearchDate -> {
                                            SearchHistoryDateHeader(date = item.searchDate)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "查询过的单词会出现在这里",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline,
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
