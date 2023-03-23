package com.peter.landing.ui.ipa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun IpaScreen(
    viewModel: IpaViewModel,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    val ipaTypeMenuState = remember { mutableStateOf(false) }
    IpaContent(
        uiState = viewModel.uiState.value,
        setIpaType = viewModel::setIpaType,
        ipaTypeMenuState = ipaTypeMenuState,
        playPron = playPron,
        navigateTo = navigateTo
    )
}

@Composable
private fun IpaContent(
    uiState: IpaUiState,
    setIpaType: (Ipa.Type) -> Unit,
    ipaTypeMenuState: MutableState<Boolean>,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Ipa,
            navigateTo = navigateTo,
            actions = {
                if (uiState is IpaUiState.Success) {
                    Box {
                        IconButton(
                            onClick = { ipaTypeMenuState.value = true }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter_list_24dp),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        DropdownMenu(
                            expanded = ipaTypeMenuState.value,
                            onDismissRequest = { ipaTypeMenuState.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = Ipa.Type.CONSONANTS.cnValue) },
                                onClick = {
                                    setIpaType(Ipa.Type.CONSONANTS)
                                    ipaTypeMenuState.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = Ipa.Type.VOWELS.cnValue) },
                                onClick = {
                                    setIpaType(Ipa.Type.VOWELS)
                                    ipaTypeMenuState.value = false
                                }
                            )
                        }
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
                is IpaUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is IpaUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "当前类型：${uiState.ipaType.cnValue}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                        IpaList(
                            ipaList = uiState.ipaList,
                            playPron = playPron
                        )
                    }
                }
            }
        }
    }

}
