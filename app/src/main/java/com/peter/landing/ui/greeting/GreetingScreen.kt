package com.peter.landing.ui.greeting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.R
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.util.ErrorDialog
import com.peter.landing.ui.util.ProcessingDialog

@Composable
fun GreetingScreen(
    viewModel: GreetingViewModel,
    exitApp: () -> Unit,
    navigateToHome: () -> Unit,
) {
    GreetingContent(
        uiState = viewModel.uiState.value,
        loadTerms = viewModel::loadTerms,
        acceptTerms = viewModel::acceptedTerms,
        dismissDialog = viewModel::dismissDialog,
        exitApp = exitApp,
        navigateToHome = navigateToHome
    )
}

@Composable
private fun GreetingContent(
    uiState: GreetingUiState,
    loadTerms: (Terms.Type) -> Unit,
    acceptTerms: () -> Unit,
    dismissDialog: () -> Unit,
    exitApp: () -> Unit,
    navigateToHome: () -> Unit,
) {
    when (uiState) {
        GreetingUiState.Accepted -> {
            LaunchedEffect(Unit) {
                navigateToHome()
            }
        }
        GreetingUiState.Default -> Unit
        is GreetingUiState.ErrorDialog -> {
            ErrorDialog(
                code = uiState.code,
                onDismiss = dismissDialog
            )
        }
        GreetingUiState.Processing -> {
            ProcessingDialog()
        }
        is GreetingUiState.TermsDialog -> {
            TermsDialog(
                terms = uiState.terms,
                onDismiss = dismissDialog
            )
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.screen_greeting),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.landing_logo),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(192.dp)
            )
        }
        Text(
            text = stringResource(R.string.app_cn_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.greeting_slogan),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        LegalText(
            loadTerms = loadTerms,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(
            onClick = acceptTerms,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        ) {
            Text(
                text = stringResource(R.string.greeting_accept),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        Button(
            onClick = exitApp,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
        ) {
            Text(
                text = stringResource(R.string.greeting_reject),
                color = MaterialTheme.colorScheme.onError,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(name = "Greeting Content", showBackground = true)
@Composable
fun GreetingContentPreview() {
    GreetingContent(GreetingUiState.Default, {}, {}, {}, {}, {})
}
