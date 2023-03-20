package com.peter.landing.ui.affix

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog

@Composable
fun AffixSection(
    affixCatalog: AffixCatalog,
    affixList: List<Affix>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = affixCatalog.description,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(12.dp)
        )
        Divider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            AffixItemTitle(
                title = "词缀",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f)
            )
            Divider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            if (affixList.first().meaning.isNotBlank()) {
                AffixItemTitle(
                    title = "含义",
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(2f)
                )
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
            AffixItemTitle(
                title = "例子",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(3f)
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )
        affixList.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            ) {
                AffixItemContent(
                    content = it.text,
                    modifier = Modifier.weight(1f)
                )
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
                if (it.meaning.isNotBlank()) {
                    AffixItemContent(
                        content = it.meaning,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(2f)
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                }
                AffixItemContent(
                    content = it.example,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(3f)
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )
        }
    }
}
