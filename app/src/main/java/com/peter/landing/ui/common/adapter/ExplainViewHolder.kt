package com.peter.landing.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.databinding.ComponentRearrangeBinding
import com.peter.landing.databinding.ItemExplainLanguageBinding
import com.peter.landing.databinding.ItemExplainWordClassBinding
import com.peter.landing.databinding.ItemExplainWordExplainBinding

sealed class ExplainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(explainItemData: ExplainItem.Data, isZoom: Boolean) {}

    class ItemLanguageViewHolder(private val binding: ItemExplainLanguageBinding)
        : ExplainViewHolder(binding.root)
    {
        override fun bind(explainItemData: ExplainItem.Data, isZoom: Boolean) {
            if (isZoom) {
                binding.explainItemLanguage.textSize = 16f
            }
            val data = explainItemData as ExplainItem.Data.ItemLanguage
            when (data.language) {
                ExplainItem.Data.ItemLanguage.Type.CN ->
                    binding.explainItemLanguage.text =
                        view.resources.getString(R.string.language_cn)
                ExplainItem.Data.ItemLanguage.Type.EN ->
                    binding.explainItemLanguage.text =
                        view.resources.getString(R.string.language_en)
            }
        }
    }

    class ItemWordClassViewHolder(private val binding: ItemExplainWordClassBinding)
        : ExplainViewHolder(binding.root)
    {
        override fun bind(explainItemData: ExplainItem.Data, isZoom: Boolean) {
            if (isZoom) {
                binding.explainItemWordClass.textSize = 16f
            }
            val data = explainItemData as ExplainItem.Data.ItemWordClass
            binding.explainItemWordClass.text = data.wordClass
        }
    }

    class ItemWordExplainViewHolder(private val binding: ItemExplainWordExplainBinding)
        : ExplainViewHolder(binding.root)
    {
        override fun bind(explainItemData: ExplainItem.Data, isZoom: Boolean) {
            if (isZoom) {
                binding.explainItemWordExplain.textSize = 16f
            }
            val data = explainItemData as ExplainItem.Data.ItemWordExplain
            binding.explainItemWordExplain.text = data.wordExplain
        }
    }

    class ItemWordRearrangeExerciseViewHolder(val binding: ComponentRearrangeBinding) :
        ExplainViewHolder(binding.root)
    {
        val adapter: RearrangeAdapter = RearrangeAdapter()

        fun setSubmitListener(submitListener: View.OnClickListener?) {
            binding.submitButton.setOnClickListener(submitListener)
        }

        fun setResetListener(resetListener: View.OnClickListener?) {
            binding.resetButton.setOnClickListener(resetListener)
        }

        override fun bind(explainItemData: ExplainItem.Data, isZoom: Boolean) {
            val data = explainItemData as ExplainItem.Data.ItemWordRearrangeExercise
            binding.rearrangeSpellingAnswer.visibility = View.GONE
            binding.rearrangeSpellingAnswer.text = data.wordSpelling

            binding.rearrangeTips.text =
                itemView.context.getString(R.string.rearrange_spelling_tips)

            binding.rearrangeInput.text = ""
            val textDefaultColor = itemView.context.getColor(R.color.color_on_surface)
            binding.rearrangeInput.setTextColor(textDefaultColor)

            val letterList = data.wordSpelling.toList()
            val letterListSize = letterList.size
            adapter.setData(letterList)
            adapter.setLetterListener {
                val viewHolder = it.tag as RearrangeViewHolder
                val current = binding.rearrangeInput.text.toString()
                if (current.length < letterListSize) {
                    val result = current + viewHolder.letter.toString()
                    binding.rearrangeInput.text = result
                }
                adapter.disableRearrangeItem(viewHolder.bindingAdapterPosition)
            }
            binding.rearrangeDelButton.setOnClickListener {
                val current = binding.rearrangeInput.text.toString()
                if (current.isNotEmpty() && current.isNotBlank()) {
                    adapter.enableRearrangeItem()
                    val result = current.dropLast(1)
                    binding.rearrangeInput.text = result
                }
            }
            binding.rearrangeList.adapter = adapter

            binding.rearrangeDelButton.isEnabled = true
            binding.submitButton.isEnabled = true
        }

        init {
            itemView.tag = this
            binding.submitButton.tag = this
            binding.resetButton.tag = this
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int)
                : ExplainViewHolder {
            return when (ExplainItem.Type.values()[viewType]) {
                ExplainItem.Type.ItemLanguage -> {
                    val binding = ItemExplainLanguageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemLanguageViewHolder(binding)
                }
                ExplainItem.Type.ItemWordClass -> {
                    val binding = ItemExplainWordClassBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemWordClassViewHolder(binding)
                }
                ExplainItem.Type.ItemWordExplain -> {
                    val binding = ItemExplainWordExplainBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemWordExplainViewHolder(binding)
                }
                ExplainItem.Type.ItemWordRearrangeExercise -> {
                    val binding = ComponentRearrangeBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemWordRearrangeExerciseViewHolder(binding)
                }
            }
        }
    }

}