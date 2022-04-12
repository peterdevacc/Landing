package com.peter.landing.ui.dict

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.peter.landing.R
import com.peter.landing.databinding.FragmentDefinitionBinding
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.showExplainZoomDialog
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DefinitionFragment : Fragment() {

    private var definitionBinding: FragmentDefinitionBinding? = null
    private val binding get() = definitionBinding!!
    private val viewModel: DefinitionViewModel by viewModels()
    private val safeArgs: DefinitionFragmentArgs by navArgs()
    private val cnExplainAdapter = ExplainAdapter()
    private val enExplainAdapter = ExplainAdapter()
    private lateinit var sound: Sound

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        definitionBinding = FragmentDefinitionBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.search(safeArgs.wordSpelling)

        initExplainAdapterUI()

        sound = Sound(view.context)

        viewLifecycleOwner.lifecycleScope.launch {
            val word = viewModel.getWord()
            if (word != null) {
                binding.definitionExplain.spelling.text = word.spelling
                binding.definitionExplain.ipa.text = word.ipa
                val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
                cnExplainAdapter.setData(cnExplain)
                binding.definitionExplain.cnExplain.itemExplainZoomButton.setOnClickListener {
                    showExplainZoomDialog(
                        word.spelling,
                        ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                        childFragmentManager
                    )
                }
                val enExplain = ExplainItem.getWordExplainSingleType(word.en)
                enExplainAdapter.setData(enExplain)
                binding.definitionExplain.enExplain.itemExplainZoomButton.setOnClickListener {
                    showExplainZoomDialog(
                        word.spelling,
                        ExplainItem.addItemLanguageTypeHeaderEN(enExplain),
                        childFragmentManager
                    )
                }
                val isNoted = viewModel.isWordNoted(word.id)
                if (isNoted) {
                    binding.definitionToggleGroup.check(R.id.definition_note_btn)
                }
                binding.definitionToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                    if (checkedId == R.id.definition_note_btn) {
                        if (isChecked) {
                            viewModel.addNote(word.id)
                        } else {
                            viewModel.removeNote(word.id)
                        }
                    }
                }

                binding.definitionPronFab.setOnClickListener {
                    sound.playAudio(word.pronName)
                }

            } else {
                searchWordNotFound()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        viewLifecycleOwner.lifecycle.removeObserver(sound)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        definitionBinding = null
    }

    private fun initExplainAdapterUI() {
        binding.definitionExplain.cnExplain.itemExplainTitle.text = getString(R.string.language_cn)
        binding.definitionExplain.enExplain.itemExplainTitle.text = getString(R.string.language_en)

        cnExplainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        enExplainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.definitionExplain.cnExplain.itemExplainList.adapter = cnExplainAdapter
        binding.definitionExplain.cnExplain.itemExplainList
            .addItemDecoration(cnExplainAdapter.itemSpaceDecor)
        binding.definitionExplain.cnExplain.itemExplainList.addOnAttachStateChangeListener(
            listStateChangeListener
        )
        binding.definitionExplain.enExplain.itemExplainList.adapter = enExplainAdapter
        binding.definitionExplain.enExplain.itemExplainList
            .addItemDecoration(enExplainAdapter.itemSpaceDecor)
        binding.definitionExplain.enExplain.itemExplainList.addOnAttachStateChangeListener(
            listStateChangeListener
        )
    }

    private fun searchWordNotFound() {
        val msg = getString(R.string.search_not_exist_hint, safeArgs.wordSpelling)
        binding.definitionExplainContainer.visibility = View.GONE
        binding.definitionButtonSection.visibility = View.GONE
        binding.definitionNotFoundCard.visibility = View.VISIBLE
        binding.definitionNotFoundMsg.visibility = View.VISIBLE
        binding.definitionNotFoundMsg.text = msg
    }
}
