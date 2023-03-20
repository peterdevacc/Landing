package com.peter.landing.util

import java.text.DateFormat
import java.util.*

fun getTodayDateTime() = getBeforeOrAfterToday(0)

fun getTomorrowDateTime() = getBeforeOrAfterToday(1)

fun getTodayDateTimeFull(): Calendar {
    val current = Calendar.getInstance()
    return current.apply {
        set(Calendar.MILLISECOND, 0)
    }
}

fun getDate(date: Date): String {
    return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(date)
}

fun calculateDate(calendar: Calendar?): String {
    if (calendar != null) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(calendar.time)
    }
    return ""
}

fun calculateEndDate(vocabularySize: Int, calendar: Calendar, wordListSize: Int): String {
    val days = vocabularySize / wordListSize
    val cal = calendar.clone() as Calendar
    val date = cal.apply { add(Calendar.DAY_OF_MONTH, days) }.time
    return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(date)
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
