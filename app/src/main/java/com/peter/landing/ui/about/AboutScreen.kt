package com.peter.landing.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun AboutScreen(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
) {
    AboutContent(
        navigateToTerms = navigateToTerms,
        navigateTo = navigateTo
    )
}

@Composable
private fun AboutContent(
    navigateToTerms: (Terms.Type) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.About,
            navigateTo = navigateTo,
        )
        val version = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_version_title),
                text = stringResource(id = R.string.app_version),
            )
        }
        val lastUpdate = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_date_title),
                text = stringResource(id = R.string.app_last_update),
            )
        }
        AboutInfoSection(
            heading = stringResource(id = R.string.about_app_name),
            infoList = listOf(version, lastUpdate)
        )

        val service = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_service_terms),
                text = stringResource(id = R.string.about_service_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.SERVICE) }
            )
        }
        val privacy = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_privacy_policy_terms),
                text = stringResource(id = R.string.about_privacy_policy_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.PRIVACY) }
            )
        }
        val acknowledgement = @Composable {
            AboutInfoItem(
                title = stringResource(id = R.string.about_acknowledge_terms),
                text = stringResource(id = R.string.about_acknowledge_text),
                clickable = true,
                clickAction = { navigateToTerms(Terms.Type.ACKNOWLEDGE) }
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        AboutInfoSection(
            heading = stringResource(id = R.string.about_terms),
            infoList = listOf(service, privacy, acknowledgement)
        )

        Spacer(modifier = Modifier.weight(1f))

        AboutConnectInfo(
            motto = stringResource(id = R.string.about_advisement),
            website = stringResource(id = R.string.about_contact)
        )
    }
}
