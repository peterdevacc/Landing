package com.peter.landing.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LandingNavigationMenu(
    currentDestination: LandingDestination.Main,
    navigateTo: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val navList = LandingDestination.Main.getNavigationList()
    Dialog(
        onDismissRequest = onDismiss
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(16.dp),
            columns = GridCells.Adaptive(minSize = 86.dp),
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
        ) {
            items(navList) {
                LandingNavigationItem(
                    mainDestination = it,
                    isCurrent = currentDestination == it,
                    navigateTo = navigateTo,
                    onDismiss = onDismiss
                )
            }
        }
    }
}

@Composable
private fun LandingNavigationItem(
    mainDestination: LandingDestination.Main,
    isCurrent: Boolean,
    navigateTo: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val contentColor: Color
    val containerColor: Color
    if (isCurrent) {
        contentColor = MaterialTheme.colorScheme.onPrimary
        containerColor = MaterialTheme.colorScheme.primary
    } else {
        contentColor = MaterialTheme.colorScheme.onSurface
        containerColor = MaterialTheme.colorScheme.surface
    }

    Box(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(containerColor)
                .clickable {
                    onDismiss()
                    navigateTo(mainDestination.route)
                }
                .padding(4.dp)
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(mainDestination.iconId),
                contentDescription = stringResource(mainDestination.iconCd),
                tint = contentColor,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(mainDestination.textId),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }

}
