package com.peter.landing.ui.study.learn

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.ui.util.ExplainSection

@Composable
fun LearnPager(
    spelling: String,
    ipa: String,
    cnExplain: Map<String, List<String>>,
    enExplain: Map<String, List<String>>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = spelling,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = ipa,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ExplainSection(
                spelling = spelling,
                explain = cnExplain,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ExplainSection(
                spelling = spelling,
                explain = enExplain,
                isCN = false,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}
