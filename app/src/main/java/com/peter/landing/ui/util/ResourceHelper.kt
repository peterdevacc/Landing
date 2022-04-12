package com.peter.landing.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun bitmapFromVectorDrawable(
    context: Context, @DrawableRes id: Int, @ColorRes tintColor: Int?
): Bitmap {
    val vectorDrawable = AppCompatResources.getDrawable(context, id)
    val width = vectorDrawable!!.intrinsicWidth
    val height = vectorDrawable.intrinsicHeight
    vectorDrawable.setBounds(0, 0, width, height)
    if (tintColor != null) {
        DrawableCompat.setTint(vectorDrawable, ContextCompat.getColor(context, tintColor))
    }
    val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    vectorDrawable.draw(canvas)
    return bm
}

fun getDrawableId(name: String, context: Context): Int {
    return context.resources.getIdentifier(
        name, "drawable", context.packageName
    )
}
