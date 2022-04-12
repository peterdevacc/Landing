package com.peter.landing.ui.common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemRearraggeLetterBinding

class RearrangeViewHolder(private val binding: ItemRearraggeLetterBinding)
    : RecyclerView.ViewHolder(binding.root)
{
    var letter = ' '
        private set

    fun setLetterListener(letterListener: View.OnClickListener?) {
        itemView.setOnClickListener(letterListener)
    }

    fun bind(rearrangeItem: RearrangeItem) {
        letter = rearrangeItem.letter
        binding.itemRearrangeWordLetter.text = letter.toString()
        itemView.isEnabled = !rearrangeItem.examine
    }

    init {
        itemView.tag = this
    }
}