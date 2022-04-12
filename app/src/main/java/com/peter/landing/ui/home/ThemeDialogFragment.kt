package com.peter.landing.ui.home

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.peter.landing.R
import com.peter.landing.ui.util.THEME_CHECKED
import com.peter.landing.ui.util.THEME_REQ

class ThemeDialogFragment : DialogFragment() {

    private var onDismissCallback: (() -> Unit)? = null
    private var themeChecked = 2

    fun setThemeChecked(themeChecked: Int) {
        this.themeChecked = themeChecked
    }

    fun setOnDismissCallback(onDismissCallback: () -> Unit) {
        this.onDismissCallback = onDismissCallback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.home_pick_theme))
                setSingleChoiceItems(R.array.theme_entries, themeChecked)
                { _, which ->
                    setFragmentResult(THEME_REQ, bundleOf(THEME_CHECKED to which))
                    dismiss()
                }
                setNegativeButton(R.string.cancel) { _, _ -> }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }
}