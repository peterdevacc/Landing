package com.peter.landing.ui.plan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.util.calculateDate
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import java.util.*

@Composable
fun NewPlanDialog(
    vocabularyList: List<Vocabulary>,
    studyAmountList: List<Int>,
    updateNewPlanVocabulary: (Vocabulary)-> Unit,
    updateNewPlanStartDate: (Calendar?)-> Unit,
    updateNewPlanWordListSize: (Int)-> Unit,
    selectedStartDate: Calendar?,
    selectedVocabulary: Vocabulary,
    selectedWordListSize: Int,
    endDate: String,
    complete: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(insets = WindowInsets.systemBars)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onDismiss
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Text(
                    text = stringResource(R.string.new_plan_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .fillMaxSize()
            ) {
                NewPlanSectionTitle(
                    iconId = R.drawable.ic_vocabulary_24dp,
                    textId = R.string.new_plan_vocabulary_title,
                )
                LazyRow(
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                ) {
                    items(vocabularyList) {
                        VocabularyItem(
                            vocabulary = it,
                            updateNewPlanVocabulary = updateNewPlanVocabulary
                        )
                        Spacer(modifier = Modifier.padding(end = 8.dp))
                    }
                }
                NewPlanSectionTitle(
                    iconId = R.drawable.ic_word_list_size_24dp,
                    textId = R.string.new_plan_word_list_size_title,
                )
                LazyRow(
                    contentPadding = PaddingValues(vertical = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(studyAmountList) {
                        OutlinedButton(
                            onClick = { updateNewPlanWordListSize(it) },
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.padding(
                                end = 16.dp,
                                top = 4.dp, bottom = 4.dp
                            )
                        ) {
                            Text(
                                text = it.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                NewPlanSectionTitle(
                    iconId = R.drawable.ic_date_24dp,
                    textId = R.string.new_plan_start_date_title,
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { updateNewPlanStartDate(getTodayDateTime()) },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    ) {
                        Text(
                            text = stringResource(R.string.new_plan_today_btn),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    OutlinedButton(
                        onClick = { updateNewPlanStartDate(getTomorrowDateTime()) },
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    ) {
                        Text(
                            text = stringResource(R.string.new_plan_tomorrow_btn),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                CurrentSelectedDetail(
                    selectedVocabulary = selectedVocabulary,
                    selectedWordListSize = selectedWordListSize,
                    selectedStartDate = selectedStartDate,
                    endDate = endDate,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .weight(1f)
                        .fillMaxSize()
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth()
                        .height(46.dp),
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Button(
                        onClick = complete,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        Text(
                            text = stringResource(R.string.complete),
                            fontSize = 18.sp
                        )
                    }
                }

            }
        }

    }
}

@Composable
private fun CurrentSelectedDetail(
    selectedVocabulary: Vocabulary,
    selectedWordListSize: Int,
    selectedStartDate: Calendar?,
    endDate: String,
    modifier: Modifier
) {
    val vocabulary = if (selectedVocabulary == Vocabulary()) {
        "未选择"
    } else {
        "${selectedVocabulary.name.cnValue} - ${selectedVocabulary.size}个"
    }
    val wordListSize = if (selectedWordListSize == 0) {
        "未选择"
    } else {
        "$selectedWordListSize 个"
    }
    val start = if (selectedStartDate == null) {
        "未选择"
    } else {
        calculateDate(selectedStartDate)
    }
    val end = endDate.ifEmpty {
        "无"
    }

    Column(modifier = modifier) {
        NewPlanSectionTitle(
            iconId = R.drawable.ic_plan_current_24dp,
            textId = R.string.new_plan_current_title,
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        PlanDetailText(text = "当前词库：$vocabulary")
        PlanDetailText(text = "每日词量：$wordListSize")
        PlanDetailText(text = "开始日期：$start")
        PlanDetailText(text = "预计结束：$end")
    }
}

@Composable
private fun NewPlanSectionTitle(
    iconId: Int,
    textId: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(end = 6.dp)
        )
        Text(
            text = stringResource(textId),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PlanDetailText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    )
}
