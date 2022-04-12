package com.peter.landing.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemRearraggeLetterBinding

class RearrangeAdapter : RecyclerView.Adapter<RearrangeViewHolder>() {

    private var letterListener: View.OnClickListener? = null
    private var letters = emptyList<RearrangeItem>()
    private var answer = emptyList<Char>()
    private val removedLetterIndexList = mutableListOf<Int>()

    fun setData(letters: List<Char>) {
        answer = letters
        this.letters = letters.shuffled().map {
            RearrangeItem(it)
        }
        notifyDataSetChanged()
    }

    fun checkData() {
        letters.forEach {
            it.examine = true
        }
        notifyDataSetChanged()
    }

    fun resetData() {
        letters.forEach {
            it.examine = false
        }
        this.letters = letters.shuffled()
        removedLetterIndexList.clear()
        notifyDataSetChanged()
    }

    fun enableRearrangeItem() {
        letters[removedLetterIndexList.last()].examine = false
        removedLetterIndexList.removeLast()
        notifyDataSetChanged()
    }

    fun disableRearrangeItem(itemLetterIndex: Int) {
        letters[itemLetterIndex].examine = true
        removedLetterIndexList.add(itemLetterIndex)
        notifyDataSetChanged()
    }

    fun setLetterListener(letterListener: View.OnClickListener) {
        this.letterListener = letterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RearrangeViewHolder {
        val binding = ItemRearraggeLetterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RearrangeViewHolder(binding)
    }

    override fun getItemCount() = letters.size

    override fun onBindViewHolder(holder: RearrangeViewHolder, position: Int) {
        holder.bind(letters[position])
        holder.setLetterListener(letterListener)
    }

}