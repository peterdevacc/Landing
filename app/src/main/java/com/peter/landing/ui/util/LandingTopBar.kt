package com.peter.landing.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.navigation.LandingNavigationMenu

@Composable
fun LandingTopBar(
    currentDestination: LandingDestination.Main,
    navigateTo: (String) -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val navDialogState = remember { mutableStateOf(false) }

    if (navDialogState.value) {
        LandingNavigationMenu(
            currentDestination = currentDestination,
            navigateTo = navigateTo,
            onDismiss = { navDialogState.value = false }
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { navDialogState.value = true }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu_24dp),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        Text(
            text = stringResource(currentDestination.textId),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        actions()
    }
}

@Composable
fun LandingTopBar(
    currentDestination: LandingDestination.General,
    navigateUp: () -> Unit,
    leftSideContent: @Composable () -> Unit = {},
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val (icon, heading, leftSide) = createRefs()

        IconButton(
            onClick = navigateUp,
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = stringResource(currentDestination.textId),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.constrainAs(heading) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
        )

        Box(
            modifier = Modifier.constrainAs(leftSide) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }
        ) {
            leftSideContent()
        }
    }
}
