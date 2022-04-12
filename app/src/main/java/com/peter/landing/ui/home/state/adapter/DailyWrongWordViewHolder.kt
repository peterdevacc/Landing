package com.peter.landing.ui.home.state.adapter

import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.databinding.ItemDailyWrongBinding

class DailyWrongWordViewHolder(
    private val binding: ItemDailyWrongBinding,
    @ColorInt val noted: Int, @ColorInt val unNoted: Int
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var wrongWord: WrongWord

    fun setListener(
        noteListener: View.OnClickListener?,
        detailListener: View.OnClickListener?
    ) {
        itemView.setOnClickListener(noteListener)
        binding.itemDailyWrongZoomButton.setOnClickListener(detailListener)
    }

    fun bind(data: WrongWord) {
        wrongWord = data
        binding.itemDailyWrongSpelling.text = data.word.spelling
        if (data.wrong.isNoted) {
            binding.itemDailyWrongSpelling.setTextColor(noted)
        } else {
            binding.itemDailyWrongSpelling.setTextColor(unNoted)
        }
    }

    init {
        itemView.tag = this
        binding.itemDailyWrongZoomButton.tag = this
    }
}