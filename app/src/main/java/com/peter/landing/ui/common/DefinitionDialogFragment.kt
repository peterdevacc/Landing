package com.peter.landing.ui.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.FragmentDefinitionDialogBinding
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener

class DefinitionDialogFragment : DialogFragment() {

    private var definitionDialogBinding: FragmentDefinitionDialogBinding? = null
    private val binding get() = definitionDialogBinding!!
    private val explainAdapter = ExplainAdapter()
    private lateinit var word: Word
    private lateinit var sound: Sound

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            definitionDialogBinding = FragmentDefinitionDialogBinding.inflate(
                layoutInflater, null, false
            )

            sound = Sound(binding.root.context)

            binding.spelling.text = word.spelling
            binding.ipa.text = word.ipa

            explainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
            explainAdapter.setData(
                ExplainItem.getWordExplain(word.cn, word.en)
            )
            binding.explainList.adapter = explainAdapter
            binding.explainList.addItemDecoration(explainAdapter.itemSpaceDecor)
            binding.explainList.addOnAttachStateChangeListener(listStateChangeListener)

            binding.pronButton.setOnClickListener {
                sound.playAudio(word.pronName)
            }

            builder.setView(binding.root)
            builder.setNegativeButton(R.string.cancel) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        lifecycle.removeObserver(sound)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        definitionDialogBinding = null
    }

    fun setWord(word: Word) {
        this.word = word
    }

}