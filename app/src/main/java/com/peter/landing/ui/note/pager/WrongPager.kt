package com.peter.landing.ui.note.pager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.peter.landing.R
import com.peter.landing.ui.util.ImageNotice
import kotlinx.coroutines.flow.Flow

@Composable
fun WrongPager(
    isDarkMode: Boolean,
    wrongWordList: Flow<PagingData<WrongWordItem>>,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagingItems = wrongWordList.collectAsLazyPagingItems()
        if (pagingItems.itemCount != 0) {
            WrongList(
                pagingItems = pagingItems,
                openExplainDialog = openExplainDialog,
                playPron = playPron
            )
        } else {
            WrongListEmpty(isDarkMode)
        }
    }
}

@Composable
private fun WrongList(
    pagingItems: LazyPagingItems<WrongWordItem>,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    playPron: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = pagingItems,
            key = { it.hashCode() }
        ) {wrongWordItem ->
            if (wrongWordItem != null) {
                when (wrongWordItem) {
                    is WrongWordItem.ItemWrongWord -> {
                        WrongWord(
                            progressWrongWord = wrongWordItem.progressWrongWord,
                            openExplainDialog = openExplainDialog,
                            playPron = playPron
                        )
                    }
                    is WrongWordItem.NumHeader -> {
                        WrongWordNumHeader(num = wrongWordItem.num)
                    }
                }
            }
        }
    }
}

@Composable
private fun WrongListEmpty(isDarkMode: Boolean) {
    val image = if (isDarkMode) {
        R.drawable.empty_img_dark
    } else {
        R.drawable.empty_img_light
    }

    Column {
        Spacer(modifier = Modifier.weight(1.5f))
        ImageNotice(
            imageId = image,
            text = stringResource(R.string.wrong_list_empty_msg),
            Modifier
                .padding(16.dp)
                .weight(3.5f)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}
