package com.peter.landing.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog

@Composable
fun HelpSection(
    helpCatalog: HelpCatalog,
    helpList: List<Help>
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = helpCatalog.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                top = 4.dp, start =  16.dp, end = 16.dp
            )
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = helpCatalog.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal =  16.dp)
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            helpList.forEach {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = it.title,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    // for alphabet list
                    val content: String = if (it.content.contains("@")) {
                        it.content.replace("@", "\n")
                    } else {
                        it.content
                    }
                    Spacer(modifier = Modifier.padding(vertical = 1.dp))
                    Text(
                        text = content,
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
            }
        }

    }

}
