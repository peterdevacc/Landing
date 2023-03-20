package com.peter.landing.ui.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.plan.chart.ProgressReportChart
import com.peter.landing.ui.plan.chart.TotalReportChart
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.ImageNotice
import com.peter.landing.ui.util.LandingTopBar
import java.util.*

@Composable
fun PlanScreen(
    isDarkMode: Boolean,
    viewModel: PlanViewModel,
    navigateTo: (String) -> Unit,
) {
    PlanContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        openNewPlanDialog = viewModel::openNewPlanDialog,
        updateNewPlanVocabulary = viewModel::updateNewPlanVocabulary,
        updateNewPlanStartDate = viewModel::updateNewPlanStartDate,
        updateNewPlanWordListSize = viewModel::updateNewPlanWordListSize,
        completeNewPlan = viewModel::completeNewPlan,
        openDeleteDialog = viewModel::openDeleteDialog,
        deletePlan = viewModel::deletePlan,
        closeDeleteDialog = viewModel::closeDeleteDialog,
        closeNewPlanDialog = viewModel::closeNewPlanDialog,
        navigateTo = navigateTo
    )
}

@Composable
private fun PlanContent(
    isDarkMode: Boolean,
    uiState: PlanUiState,
    openNewPlanDialog: () -> Unit,
    updateNewPlanVocabulary: (Vocabulary) -> Unit,
    updateNewPlanStartDate: (Calendar?) -> Unit,
    updateNewPlanWordListSize: (Int) -> Unit,
    completeNewPlan: () -> Unit,
    openDeleteDialog: () -> Unit,
    deletePlan: () -> Unit,
    closeDeleteDialog: () -> Unit,
    closeNewPlanDialog: () -> Unit,
    navigateTo: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Plan,
            navigateTo = navigateTo,
            actions = {
                if (uiState is PlanUiState.Existed) {
                    IconButton(
                        onClick = openDeleteDialog
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_forever_24dp),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground
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
                is PlanUiState.Empty -> {

                    when (uiState.dialog) {
                        PlanUiState.Empty.Dialog.NewPlan -> {
                            NewPlanDialog(
                                vocabularyList = uiState.vocabularyList,
                                studyAmountList = integerArrayResource(
                                    R.array.word_list_num_entry_values
                                ).toList(),
                                updateNewPlanVocabulary = updateNewPlanVocabulary,
                                updateNewPlanStartDate = updateNewPlanStartDate,
                                updateNewPlanWordListSize = updateNewPlanWordListSize,
                                selectedVocabulary = uiState.vocabulary,
                                selectedWordListSize = uiState.wordListSize,
                                selectedStartDate = uiState.startDate,
                                endDate = uiState.endDate,
                                complete = completeNewPlan,
                                onDismiss = closeNewPlanDialog
                            )
                        }
                        PlanUiState.Empty.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        val image = if (isDarkMode) {
                            R.drawable.empty_img_dark
                        } else {
                            R.drawable.empty_img_light
                        }

                        Spacer(modifier = Modifier.weight(1.2f))
                        ImageNotice(
                            imageId = image,
                            text = stringResource(id = R.string.plan_empty_msg),
                            Modifier
                                .weight(3.5f)
                                .fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .weight(2.3f)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = openNewPlanDialog,
                                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
                is PlanUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is PlanUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is PlanUiState.Existed -> {

                    when (uiState.dialog) {
                        PlanUiState.Existed.Dialog.DeletePlan -> {
                            DeletePlanDialog(
                                deletePlan = deletePlan,
                                onDismiss = closeDeleteDialog
                            )
                        }
                        PlanUiState.Existed.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (uiState.progressReport.isNotEmpty()) {
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                ProgressReportChart(uiState.progressReport)
                            }
                        }

                        if (uiState.totalReport.isNotEmpty()) {
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                TotalReportChart(
                                    uiState.studyPlan.vocabularyName,
                                    uiState.totalReport
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}
