package com.peter.landing.util

import java.text.DateFormat
import java.util.*

fun getYesterdayDateTime() = getBeforeOrAfterToday(-1)

fun getTodayDateTime() = getBeforeOrAfterToday(0)

fun getTomorrowDateTime() = getBeforeOrAfterToday(1)

fun getTwoDayAfterDateTime() = getBeforeOrAfterToday(2)

fun getThreeDayAfterDateTime() = getBeforeOrAfterToday(3)

fun getTodayDateTimeFull(): Calendar {
    val current = Calendar.getInstance()
    return current.apply {
        set(Calendar.MILLISECOND, 0)
    }
}

private fun getBeforeOrAfterToday(days: Int): Calendar {
    val todayDate = Calendar.getInstance()
    return todayDate.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_MONTH, days)
    }
}

fun getDate(date: Date): String {
    return DateFormat.getDateInstance().format(date)
}

fun getEndDate(vocabularySize: Int, calendar: Calendar, wordListSize: Int): String {
    val days = vocabularySize / wordListSize
    val cal = calendar.clone() as Calendar
    val date = cal.apply { add(Calendar.DAY_OF_MONTH, days) }.time
    return DateFormat.getDateInstance().format(date)
}
