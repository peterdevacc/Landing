package com.peter.landing.ui.study.learn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizPager(
    quiz: String,
    answer: String,
    ipa: String,
    input: String,
    write: (Char) -> Unit,
    remove: () -> Unit,
    submitted: Boolean,
    learned: Boolean,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (submitted) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "正确拼写：",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.padding(vertical = 28.dp))
                    Text(
                        text = answer,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = ipa,
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    val resultIcon = if (learned) {
                        R.drawable.ic_correct_24dp
                    } else {
                        R.drawable.ic_close_24dp
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(resultIcon),
                            contentDescription = "",
                            tint = Color(0xFFde8acc),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            } else {
                val image = if (isSystemInDarkTheme()) {
                    R.drawable.learn_quiz_dark
                } else {
                    R.drawable.learn_quiz_light
                }

                Image(
                    painter = painterResource(image),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 8.dp)
                        .weight(1f)
                        .fillMaxWidth()
                )
                Text(
                    text = "请拼写出单词的正确字母排序",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Text(
                    text = input,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
            Button(
                enabled = !submitted,
                onClick = remove,
                contentPadding = PaddingValues(4.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close_24dp),
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(218.dp)
                .border(
                    1.dp, MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.medium
                )
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {
            quiz.forEach { alphabet ->
                OutlinedButton(
                    onClick = { write(alphabet) },
                    contentPadding = PaddingValues(4.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(text = "$alphabet")
                }
            }
        }
    }
}
