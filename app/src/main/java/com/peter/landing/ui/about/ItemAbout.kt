package com.peter.landing.ui.about

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.peter.landing.R
import com.peter.landing.databinding.ItemAboutBinding

class ItemAbout : ConstraintLayout {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private lateinit var binding: ItemAboutBinding

    private fun init (attrs: AttributeSet?) {
        binding = ItemAboutBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ItemAbout,
            0, 0).apply {
            try {
                binding.itemAboutTitle.text = getString(R.styleable.ItemAbout_title)
                binding.itemAboutDescription.text = getString(R.styleable.ItemAbout_description)
            } finally {
                recycle()
            }
        }
    }

}