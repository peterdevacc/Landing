package com.peter.landing.ui.util

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.ui.theme.LandingAppTheme

@Composable
fun LandingKeyboard(
    write: (String) -> Unit,
    remove: () -> Unit,
    modifier: Modifier
) {
    val caseState = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.extraSmall,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TextCell(
                text = "'",
                write = write,
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
            )
            Divider(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            TextCell(
                text = "'",
                write = write,
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
            )
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            alphabetRowA.forEach {
                val text = if (caseState.value) {
                    it.uppercase()
                } else {
                    it.lowercase()
                }
                TextCell(
                    text = text,
                    write = write,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            alphabetRowB.forEach {
                val text = if (caseState.value) {
                    it.uppercase()
                } else {
                    it.lowercase()
                }
                TextCell(
                    text = text,
                    write = write,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
        Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        caseState.value = !caseState.value
                    }
            ) {
                val caseIcon = if (caseState.value) {
                    Icons.Filled.KeyboardArrowDown
                } else {
                    Icons.Filled.KeyboardArrowUp
                }
                Icon(
                    imageVector = caseIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            alphabetRowC.forEach {
                val text = if (caseState.value) {
                    it.uppercase()
                } else {
                    it.lowercase()
                }
                TextCell(
                    text = text,
                    write = write,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable {
                        remove()
                    }
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TextCell(
    text: String,
    write: (String) -> Unit,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable { write(text) }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp
        )
    }
}

private val alphabetRowA =
    listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")

private val alphabetRowB =
    listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")

private val alphabetRowC =
    listOf("z", "x", "c", "v", "b", "n", "m")

@Preview(name = "LandingKeyboard", showBackground = true)
@Composable
fun LandingKeyboardPreview() {
    LandingAppTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LandingKeyboard(
                write = {},
                remove = {},
                modifier = Modifier
                    .size(350.dp)
            )
        }
    }
}
