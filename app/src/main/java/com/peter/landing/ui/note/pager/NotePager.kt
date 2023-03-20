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
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.util.ImageNotice
import kotlinx.coroutines.flow.Flow

@Composable
fun NotePager(
    isDarkMode: Boolean,
    noteList: Flow<PagingData<NoteItem>>,
    openDictionaryDialog: (Word) -> Unit,
    removeNote: (String, Long) -> Unit,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val pagingItems = noteList.collectAsLazyPagingItems()
        if (pagingItems.itemCount != 0) {
            NoteList(
                pagingItems = pagingItems,
                openDictionaryDialog = openDictionaryDialog,
                removeNote = removeNote,
                playPron = playPron,
            )
        } else {
            NoteListEmpty(isDarkMode)
        }
    }
}

@Composable
private fun NoteList(
    pagingItems: LazyPagingItems<NoteItem>,
    openDictionaryDialog: (Word) -> Unit,
    removeNote: (String, Long) -> Unit,
    playPron: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = pagingItems,
            key = { it.hashCode() }
        ) { noteItem ->
            if (noteItem != null) {
                when (noteItem) {
                    is NoteItem.AlphabetHeader -> {
                        NoteAlphabetHeader(alphabet = noteItem.alphabet)
                    }
                    is NoteItem.ItemNote -> {
                        NoteWord(
                            word = noteItem.word,
                            playPron = playPron,
                            openDictionaryDialog = openDictionaryDialog,
                            removeNote = removeNote
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun NoteListEmpty(isDarkMode: Boolean) {
    val image = if (isDarkMode) {
        R.drawable.empty_img_dark
    } else {
        R.drawable.empty_img_light
    }

    Column {
        Spacer(modifier = Modifier.weight(1.5f))
        ImageNotice(
            imageId = image,
            text = stringResource(R.string.note_empty_msg),
            Modifier
                .padding(16.dp)
                .weight(3.5f)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(2f))
    }
}
