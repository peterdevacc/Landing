package com.peter.landing.ui.plan.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemWordListNumBinding
import com.peter.landing.ui.util.getPxFromDp

class WordListNumAdapter : RecyclerView.Adapter<WordListNumViewHolder>() {

    private var itemListener: View.OnClickListener? = null
    private var wordListNumItemList = emptyList<Pair<String, Int>>()
    private var systemDensity: Int = -1

    fun setItemListener(listener: View.OnClickListener) {
        this.itemListener = listener
    }

    fun setData(wordListNumItemList: List<Pair<String, Int>>) {
        this.wordListNumItemList = wordListNumItemList
        notifyDataSetChanged()
    }

    fun setSystemDensity(systemDensity: Int) {
        this.systemDensity = systemDensity
    }

    val itemSpaceDecor = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spacing4 = getPxFromDp(4f, systemDensity)
            val spacing16 = getPxFromDp(16f, systemDensity)
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0) {
                outRect.top = spacing4
                outRect.bottom = spacing4
                outRect.left = 0
                outRect.right = spacing16
            } else {
                outRect.top = 0
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListNumViewHolder {
        val binding = ItemWordListNumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WordListNumViewHolder(binding)
    }

    override fun getItemCount() = wordListNumItemList.size

    override fun onBindViewHolder(holder: WordListNumViewHolder, position: Int) {
        holder.bind(wordListNumItemList[position])
        holder.setListener(itemListener)
    }

}