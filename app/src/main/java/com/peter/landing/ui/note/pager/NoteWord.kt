package com.peter.landing.ui.note.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word

@Composable
fun NoteWord(
    word: Word,
    openDictionaryDialog: (Word) -> Unit,
    removeNote: (String, Long) -> Unit,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = word.spelling,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(
                    text = word.ipa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { removeNote(word.spelling, word.id) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            IconButton(
                onClick = { openDictionaryDialog(word) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_dictionary_24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 1.dp))
            IconButton(
                onClick = { playPron(word.pronName) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sound_24dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    }
}
