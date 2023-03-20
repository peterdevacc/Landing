package com.peter.landing.ui.study

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.theme.LandingAppTheme
import com.peter.landing.ui.util.ImageNotice

@Composable
fun WrongList(
    wrongList: List<Word>,
    openDictionaryDialog: (Word) -> Unit,
    modifier: Modifier
) {

    Column(
        modifier = modifier
    ) {
        if (wrongList.isNotEmpty()) {
            WrongListNotEmpty(
                wrongList = wrongList,
                openDictionaryDialog = openDictionaryDialog
            )
        } else {
            WrongListEmpty()
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColumnScope.WrongListNotEmpty(
    wrongList: List<Word>,
    openDictionaryDialog: (Word) -> Unit,
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "错词列表",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "点击单词可查看解释",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    FlowRow(
        modifier = Modifier
            .border(
                width = 1.dp,
                color =  MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.medium
            )
            .weight(1f)
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(scrollState)
    ) {
        wrongList.forEach { word ->

            OutlinedButton(
                onClick = { openDictionaryDialog(word) },
                contentPadding = PaddingValues(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = word.spelling)
            }

        }
    }
    Spacer(modifier = Modifier.padding(vertical = 4.dp))
    Text(
        text = "所有的错词都可以在单词笔记页面中找到",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun WrongListEmpty() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ImageNotice(
            imageId = if (isSystemInDarkTheme()) {
                R.drawable.all_correct_dark
            } else {
                R.drawable.all_correct_light
            },
            text = "所有练习回答正确，没有错词",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)

        )
    }
}

@Preview(name = "Wrong List Preview", showBackground = true)
@Composable
fun WrongListPreview() {
    LandingAppTheme {
        WrongList(
            wrongList = emptyList(),
            openDictionaryDialog = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
