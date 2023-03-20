package com.peter.landing.ui.affix

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun AffixScreen(
    viewModel: AffixViewModel,
    navigateTo: (String) -> Unit,
) {
    val affixCatalogTypeTypeMenuState = remember { mutableStateOf(false) }
    AffixContent(
        uiState = viewModel.uiState.value,
        setAffixCatalogType = viewModel::setAffixCatalogType,
        affixCatalogTypeTypeMenuState = affixCatalogTypeTypeMenuState,
        navigateTo = navigateTo
    )
}

@Composable
private fun AffixContent(
    uiState: AffixUiState,
    setAffixCatalogType: (AffixCatalog.Type) -> Unit,
    affixCatalogTypeTypeMenuState: MutableState<Boolean>,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Affix,
            navigateTo = navigateTo,
            actions = {
                Box {
                    IconButton(
                        onClick = { affixCatalogTypeTypeMenuState.value = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_list_24dp),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    DropdownMenu(
                        expanded = affixCatalogTypeTypeMenuState.value,
                        onDismissRequest = { affixCatalogTypeTypeMenuState.value = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = AffixCatalog.Type.PREFIX.cnValue) },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.PREFIX)
                                affixCatalogTypeTypeMenuState.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = AffixCatalog.Type.SUFFIX.cnValue) },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.SUFFIX)
                                affixCatalogTypeTypeMenuState.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = AffixCatalog.Type.MIXED.cnValue) },
                            onClick = {
                                setAffixCatalogType(AffixCatalog.Type.MIXED)
                                affixCatalogTypeTypeMenuState.value = false
                            }
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
                is AffixUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is AffixUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is AffixUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            items(uiState.affixMap.toList()) {
                                AffixSection(
                                    affixCatalog = it.first,
                                    affixList = it.second
                                )
                            }
                        }
                    }

                }
            }
        }
    }

}
