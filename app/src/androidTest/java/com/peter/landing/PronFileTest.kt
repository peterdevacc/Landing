package com.peter.landing

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.peter.landing.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PronFileTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun pronFileTest() = runBlocking {
//        onView(withId(R.id.agreement_agree_button)).perform(ViewActions.click())
        delay(2000)
        onView(withId(R.id.home_fab)).perform(ViewActions.click())
//        onView(withId(R.id.word_list_start_button)).perform(ViewActions.click())
        repeat(3000) {
            onView(withId(R.id.middle_fab)).perform(ViewActions.click())
            delay(1000)
            onView(withId(R.id.right_button)).perform(ViewActions.click())
        }
    }
}