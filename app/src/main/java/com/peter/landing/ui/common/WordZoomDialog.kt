package com.peter.landing.ui.common

import androidx.fragment.app.FragmentManager
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.common.adapter.ExplainItem

fun showExplainZoomDialog(
    wordSpelling: String,
    wordExplain: List<ExplainItem>,
    fragmentManager: FragmentManager
) {
    val dialog = ExplainDialogFragment()
    dialog.setContent(wordExplain, wordSpelling)
    dialog.show(fragmentManager, "ExplainDialogFragment")
}

fun showDefinitionZoomDialog(
    word: Word,
    fragmentManager: FragmentManager
) {
    val dialog = DefinitionDialogFragment()
    dialog.setWord(word)
    dialog.show(fragmentManager, "DefinitionDialogFragment")
}