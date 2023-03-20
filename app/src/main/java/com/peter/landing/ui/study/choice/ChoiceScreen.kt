package com.peter.landing.ui.study.choice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.study.Counter
import com.peter.landing.ui.study.InputButtonRow
import com.peter.landing.ui.study.WrongList
import com.peter.landing.ui.util.DictionaryDialog
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.util.ProcessingDialog

@Composable
fun ChoiceScreen(
    isDarkMode: Boolean,
    viewModel: ChoiceViewModel,
    playPron: (String) -> Unit,
    navigateToSpelling: () -> Unit,
    navigateUp: () -> Unit
) {
    ChoiceContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        choose = viewModel::choose,
        submit = viewModel::submit,
        getNextWord = viewModel::getNextWord,
        playPron = playPron,
        showWrongList = viewModel::showWrongList,
        hideWrongList = viewModel::hideWrongList,
        openDictionaryDialog = viewModel::openDictionaryDialog,
        closeDialog = viewModel::closeDialog,
        navigateToSpelling = navigateToSpelling,
        navigateUp = navigateUp
    )
}

@Composable
private fun ChoiceContent(
    isDarkMode: Boolean,
    uiState: ChoiceUiState,
    choose: (Int) -> Unit,
    submit: () -> Unit,
    getNextWord: () -> Unit,
    playPron: (String) -> Unit,
    showWrongList: () -> Unit,
    hideWrongList: () -> Unit,
    openDictionaryDialog: (Word) -> Unit,
    closeDialog: () -> Unit,
    navigateToSpelling: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Choice,
            navigateUp = navigateUp,
            leftSideContent = {
                if (uiState is ChoiceUiState.Success &&
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
                is ChoiceUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is ChoiceUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is ChoiceUiState.Success -> {
                    val lastOne = (uiState.current + 1) == uiState.totalNum
                    val rightInfo = if (uiState.submitted && lastOne) {
                        R.drawable.ic_start_24dp to navigateToSpelling
                    } else {
                        R.drawable.ic_double_arrow_right_24dp to getNextWord
                    }

                    when (uiState.dialog) {
                        ChoiceUiState.Success.Dialog.Processing -> {
                            ProcessingDialog()
                        }
                        ChoiceUiState.Success.Dialog.Dictionary -> {
                            uiState.clickedWrongWord?.let {
                                DictionaryDialog(
                                    word = it,
                                    playPron = playPron,
                                    onDismiss = closeDialog
                                )
                            }
                        }
                        ChoiceUiState.Success.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        if (!uiState.showWrongList) {
                            ExerciseContent(isDarkMode, uiState, choose)
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
                            pronName = uiState.pronName,
                            pronButtonEnable = !uiState.showWrongList,
                            playPron = playPron,
                            leftButtonEnable = !uiState.submitted && uiState.chosenIndex != -2,
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
    isDarkMode: Boolean,
    uiState: ChoiceUiState.Success,
    choose: (Int) -> Unit,
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
            text = uiState.spelling,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = uiState.ipa,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            var index = 0
            repeat (2) { i ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    repeat (2) { j ->
                        ChoiceOption(
                            isDarkMode = isDarkMode,
                            index = index,
                            cnExplain = uiState.optionList[index].first,
                            enExplain = uiState.optionList[index].second,
                            choose = choose,
                            correctIndex = uiState.correctIndex,
                            chosenIndex = uiState.chosenIndex,
                            submitted = uiState.submitted,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(
                                    1.dp, MaterialTheme.colorScheme.tertiary
                                )
                        )
                        if (j == 0) {
                            Spacer(
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        index++
                    }
                }
                if (i == 0) {
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
