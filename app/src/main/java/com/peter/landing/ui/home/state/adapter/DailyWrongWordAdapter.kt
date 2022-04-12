package com.peter.landing.ui.home.state.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.databinding.ItemDailyWrongBinding
import com.peter.landing.ui.util.getPxFromDp

class DailyWrongWordAdapter(
    @ColorInt val noted: Int, @ColorInt val unNoted: Int
) : RecyclerView.Adapter<DailyWrongWordViewHolder>()
{
    private var detailListener: View.OnClickListener? = null
    private var noteListener: View.OnClickListener? = null
    private var wrongWordList = emptyList<WrongWord>()
    private var systemDensity: Int = -1

    fun setData(wrongWordList: List<WrongWord>) {
        this.wrongWordList = wrongWordList
        notifyDataSetChanged()
    }

    fun setListener(
        noteListener: View.OnClickListener?,
        detailListener: View.OnClickListener?
    ) {
        this.noteListener = noteListener
        this.detailListener = detailListener
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
            val spacing = getPxFromDp(8f, systemDensity)
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0) {
                outRect.top = spacing
                outRect.left = spacing
                outRect.right = spacing
                if (position == wrongWordList.lastIndex) {
                    outRect.bottom = spacing
                } else {
                    outRect.bottom = 0
                }
            } else {
                outRect.top = 0
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWrongWordViewHolder {
        val binding = ItemDailyWrongBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DailyWrongWordViewHolder(binding, noted, unNoted)
    }

    override fun getItemCount() = wrongWordList.size

    override fun onBindViewHolder(holder: DailyWrongWordViewHolder, position: Int) {
        holder.bind(wrongWordList[position])
        holder.setListener(noteListener, detailListener)
    }

}