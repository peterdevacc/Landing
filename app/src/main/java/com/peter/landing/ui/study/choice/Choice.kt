package com.peter.landing.ui.study.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.ui.util.ExplainSection

@Composable
fun ChoiceOption(
    isDarkMode: Boolean,
    index: Int,
    cnExplain: Map<String, List<String>>,
    enExplain: Map<String, List<String>>,
    choose: (Int) -> Unit,
    submitted: Boolean,
    chosenIndex: Int,
    correctIndex: Int,
    modifier: Modifier
) {
    val optionTitle = when (index) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        else -> "D"
    }

    val titleColor = if (index == chosenIndex) {
        Pair(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
        )
    } else {
        Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }

    val explainBackgroundColor = if (submitted) {
        if (index == correctIndex) {
            if (isDarkMode) {
                Color(0xFF2A742D)
            } else {
                Color(0xFFc2eac2)
            }
        } else {
            if (isDarkMode) {
                Color(0xFF92312A)
            } else {
                Color(0xFFffd2d0)
            }
        }
    } else {
        MaterialTheme.colorScheme.background
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = optionTitle,
            style = MaterialTheme.typography.titleMedium,
            color = titleColor.second,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(!submitted) { choose(index) }
                .background(titleColor.first)
                .padding(4.dp)
                .fillMaxWidth()
        )
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        ExplainSection(
            cnExplain = cnExplain,
            enExplain = enExplain,
            modifier = Modifier
                .background(explainBackgroundColor)
                .padding(4.dp)
                .weight(1f)
                .fillMaxWidth()
        )
    }
}
