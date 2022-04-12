package com.peter.landing.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class DecentDrawerLayout(
    context: Context, attrs: AttributeSet?, defStyle: Int
) : DrawerLayout(context, attrs, defStyle) {

    constructor (
        context: Context
    ) : this(context, null, 0)

    constructor (
        context: Context, attrs: AttributeSet
    ) : this(context, attrs, 0)

    override fun open() {
        openDrawer(GravityCompat.START)
    }
}