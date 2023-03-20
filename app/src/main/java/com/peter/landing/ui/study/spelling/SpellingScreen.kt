package com.peter.landing.ui.study.spelling

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.study.Counter
import com.peter.landing.ui.study.InputButtonRow
import com.peter.landing.ui.study.WrongList
import com.peter.landing.ui.util.*

@Composable
fun SpellingScreen(
    viewModel: SpellingViewModel,
    playPron: (String) -> Unit,
    navigateUp: () -> Unit
) {
    SpellingContent(
        uiState = viewModel.uiState.value,
        write = viewModel::write,
        remove = viewModel::remove,
        getNextWord = viewModel::getNextWord,
        submit = viewModel::submit,
        playPron = playPron,
        showWrongList = viewModel::showWrongList,
        hideWrongList = viewModel::hideWrongList,
        openDictionaryDialog = viewModel::openDictionaryDialog,
        closeDialog = viewModel::closeDialog,
        navigateUp = navigateUp
    )
}

@Composable
private fun SpellingContent(
    uiState: SpellingUiState,
    write: (String) -> Unit,
    remove: () -> Unit,
    getNextWord: () -> Unit,
    submit: () -> Unit,
    playPron: (String) -> Unit,
    showWrongList: () -> Unit,
    hideWrongList: () -> Unit,
    openDictionaryDialog: (Word) -> Unit,
    closeDialog: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Spelling,
            navigateUp = navigateUp,
            leftSideContent = {
                if (uiState is SpellingUiState.Success &&
                    (uiState.current + 1) == uiState.totalNum
                    && uiState.submitted
                ) {
                    val actionInfo = if (!uiState.showWrongList) {
                        showWrongList to R.drawable.ic_check_wrong_list_24dp
                    } else {
                        hideWrongList to R.drawable.ic_hide_24dp
                    }
                    IconButton(
                        onClick = actionInfo.first
                    ) {
                        Icon(
                            painter = painterResource(actionInfo.second),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is SpellingUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is SpellingUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is SpellingUiState.Success -> {
                    val lastOne = (uiState.current + 1) == uiState.totalNum
                    val rightInfo = if (uiState.submitted && lastOne) {
                        R.drawable.ic_start_24dp to navigateUp
                    } else {
                        R.drawable.ic_double_arrow_right_24dp to getNextWord
                    }

                    when (uiState.dialog) {
                        SpellingUiState.Success.Dialog.Processing -> {
                            ProcessingDialog()
                        }
                        SpellingUiState.Success.Dialog.Dictionary -> {
                            uiState.clickedWrongWord?.let {
                                DictionaryDialog(
                                    word = it,
                                    playPron = playPron,
                                    onDismiss = closeDialog
                                )
                            }
                        }
                        SpellingUiState.Success.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        if (!uiState.showWrongList) {
                            ExerciseContent(uiState, write, remove)
                        } else {
                            WrongList(
                                wrongList = uiState.wrongList,
                                openDictionaryDialog = openDictionaryDialog,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        }
                        InputButtonRow(
                            pronName = uiState.word.pronName,
                            pronButtonEnable = uiState.submitted && !uiState.showWrongList,
                            playPron = playPron,
                            leftButtonEnable = !uiState.submitted,
                            leftButtonIconId = R.drawable.ic_submit_24dp,
                            leftButtonAction = submit,
                            rightButtonEnable = uiState.submitted,
                            rightButtonIconId = rightInfo.first,
                            rightButtonAction = rightInfo.second,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ExerciseContent(
    uiState: SpellingUiState.Success,
    write: (String) -> Unit,
    remove: () -> Unit,
) {
    Counter(
        current = uiState.current + 1,
        totalNum = uiState.totalNum,
        modifier = Modifier.fillMaxWidth()
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
    ) {
        Text(
            text = "请根据所给的解释拼写出单词",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .height(168.dp)
        ) {
            ExplainSection(
                explain = uiState.word.cn,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.secondary)
                    .padding(4.dp)
                    .weight(1f)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            ExplainSection(
                explain = uiState.word.en,
                isCN = false,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.secondary)
                    .padding(4.dp)
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
                .padding(12.dp)
        ) {
            Text(
                text = uiState.input,
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        if (!uiState.submitted) {
            LandingKeyboard(
                write = write,
                remove = remove,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "正确拼写：",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = uiState.word.spelling,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = uiState.word.ipa,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                val resultIcon = if (uiState.input == uiState.word.spelling) {
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
        }
    }
}
