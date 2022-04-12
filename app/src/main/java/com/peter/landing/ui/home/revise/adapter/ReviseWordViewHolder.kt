package com.peter.landing.ui.home.revise.adapter

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.ItemReviseWordBinding
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.showExplainZoomDialog

class ReviseWordViewHolder(private val binding: ItemReviseWordBinding)
    : RecyclerView.ViewHolder(binding.root)
{
    private val cnAdapter = ExplainAdapter()
    private val enAdapter = ExplainAdapter()
    private lateinit var spelling: String
    private var cnExplain = emptyList<ExplainItem>()
    private var enExplain = emptyList<ExplainItem>()

    fun setListener(
        soundListener: View.OnClickListener?,
        fragmentManager: FragmentManager
    ) {
        binding.pronFab.setOnClickListener(soundListener)

        binding.explainContent.cnExplain.itemExplainZoomButton.setOnClickListener {
            showExplainZoomDialog(
                spelling,
                ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                fragmentManager
            )
        }
        binding.explainContent.enExplain.itemExplainZoomButton.setOnClickListener {
            showExplainZoomDialog(
                spelling,
                ExplainItem.addItemLanguageTypeHeaderEN(enExplain),
                fragmentManager
            )
        }
    }

    fun bind(word: Word) {
        binding.explainContent.spelling.text = word.spelling
        this.spelling = word.spelling
        binding.explainContent.ipa.text = word.ipa

        val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
        cnAdapter.setData(cnExplain)
        this.cnExplain = cnExplain

        val enExplain = ExplainItem.getWordExplainSingleType(word.en)
        enAdapter.setData(enExplain)
        this.enExplain = enExplain
    }

    init {
        binding.pronFab.tag = this
        itemView.context.run {
            binding.explainContent.cnExplain.itemExplainTitle.text = getString(R.string.language_cn)
            binding.explainContent.enExplain.itemExplainTitle.text = getString(R.string.language_en)
        }
        cnAdapter.setSystemDensity(itemView.resources.displayMetrics.densityDpi)
        enAdapter.setSystemDensity(itemView.resources.displayMetrics.densityDpi)
        binding.explainContent.cnExplain.itemExplainList
            .addItemDecoration(cnAdapter.itemSpaceDecor)
        binding.explainContent.enExplain.itemExplainList
            .addItemDecoration(enAdapter.itemSpaceDecor)
        binding.explainContent.cnExplain.itemExplainList.adapter = cnAdapter
        binding.explainContent.enExplain.itemExplainList.adapter = enAdapter
    }
}