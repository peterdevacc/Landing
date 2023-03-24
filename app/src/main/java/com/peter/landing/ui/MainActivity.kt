package com.peter.landing.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.peter.landing.data.util.ThemeMode
import com.peter.landing.ui.navigation.LandingNavGraphMain
import com.peter.landing.ui.theme.LandingAppTheme
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.Sound
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var sound: Sound

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value == MainUiState.Loading
        }

        WindowCompat.setDecorFitsSystemWindows(
            window, false
        )

        sound = Sound(this)

        setContent {
            val navHostController = rememberNavController()

            when (val uiState = mainViewModel.uiState.value) {
                is MainUiState.Success -> {
                    val isDarkMode = when (uiState.themeMode) {
                        ThemeMode.LIGHT -> false
                        ThemeMode.DARK -> true
                        ThemeMode.DEFAULT -> isSystemInDarkTheme()
                    }
                    LandingAppTheme(isDarkMode) {
                        LandingNavGraphMain(
                            isDarkMode = isDarkMode,
                            playPron = sound::playAudio,
                            navHostController = navHostController,
                            exitApp = this@MainActivity::finish,
                            startDestination = uiState.startDestination
                        )
                    }
                }
                is MainUiState.Error -> {
                    LandingAppTheme {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .windowInsetsPadding(insets = WindowInsets.systemBars)
                                .fillMaxSize()
                        ) {
                            ErrorNotice(code = uiState.code)
                        }
                    }
                }
                is MainUiState.Loading -> {
                    LandingAppTheme {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .windowInsetsPadding(insets = WindowInsets.systemBars)
                                .fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp, 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.pointerCount == 1 && super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        lifecycle.removeObserver(sound)
    }

}



