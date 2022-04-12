package com.peter.landing.ui.util

import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun getPxFromDp(dp: Float, densityDpi: Int): Int {
    return ((dp * densityDpi) / DisplayMetrics.DENSITY_DEFAULT).toInt()
}

// prevent adapter memory leak
val listStateChangeListener = object : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(v: View?) {}

    override fun onViewDetachedFromWindow(v: View?) {
        v?.let {
            (it as RecyclerView).adapter = null
        }
    }
}

fun Fragment.isDarkTheme(): Boolean {
    return resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
