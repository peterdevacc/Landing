package com.peter.landing.ui.plan

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary

@Composable
fun VocabularyItem(
    vocabulary: Vocabulary,
    updateNewPlanVocabulary: (Vocabulary) -> Unit,
) {
    val vocabularyImgId = when (vocabulary.name) {
        Vocabulary.Name.BEGINNER -> R.drawable.vocabulary_beginner
        else -> R.drawable.vocabulary_intermediate
    }

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { updateNewPlanVocabulary(vocabulary) }
            .border(
                width = 1.dp,
                color = Color(0xFF5559B6),
                shape = MaterialTheme.shapes.medium
            )
            .width(256.dp)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(vocabularyImgId),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1.3f)
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .weight(1.7f)
                    .fillMaxSize()
            ) {
                Text(
                    text = vocabulary.name.cnValue,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Text(
                    text = vocabulary.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }


}
