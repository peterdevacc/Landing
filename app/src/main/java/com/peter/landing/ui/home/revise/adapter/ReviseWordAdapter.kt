package com.peter.landing.ui.home.revise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.databinding.ItemReviseWordBinding

class ReviseWordAdapter : RecyclerView.Adapter<ReviseWordViewHolder>() {

    private var soundListener: View.OnClickListener? = null
    private var wrongWordList = emptyList<WrongWord>()
    private lateinit var fragmentManager: FragmentManager

    fun setData(wordList: List<WrongWord>, fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
        this.wrongWordList = wordList
        notifyDataSetChanged()
    }

    fun getDataItem(position: Int): Word {
        return wrongWordList[position].word
    }

    fun setSoundOnClickListener(listener: View.OnClickListener) {
        this.soundListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviseWordViewHolder {
        val binding = ItemReviseWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReviseWordViewHolder(binding)
    }

    override fun getItemCount() = wrongWordList.size

    override fun onBindViewHolder(holder: ReviseWordViewHolder, position: Int) {
        holder.bind(wrongWordList[position].word)
        holder.setListener(soundListener, fragmentManager)
    }

}