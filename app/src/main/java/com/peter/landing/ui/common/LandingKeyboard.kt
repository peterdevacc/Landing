package com.peter.landing.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.peter.landing.R
import com.peter.landing.databinding.ComponentKeyboardBinding
import java.util.*

class LandingKeyboard : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    private lateinit var binding: ComponentKeyboardBinding
    private lateinit var letterButtonList: List<MaterialButton>
    private lateinit var symbolButtonList: List<MaterialButton>
    private var textView: AppCompatTextView? = null
    @ColorInt
    private var textColor: Int = -1
    private var upperCase = false
    private var textHint = "输入框"

    fun setInputTarget(inputTextView: AppCompatTextView, textHint: String = "输入框") {
        this.textView = inputTextView
        this.textHint = textHint
        this.textView?.apply {
            text = textHint
        }
    }

    fun getTextHint(): String {
        return textHint
    }

    fun setEnable(value: Boolean) {
        if (value) {
            letterButtonList.forEach {
                it.isEnabled = true
            }
            upperCase = false
            symbolButtonList.forEach {
                it.isEnabled = true
            }
            binding.caseBtn.isEnabled = true
            binding.delBtn.isEnabled = true
        } else {
            letterButtonList.forEach {
                it.isEnabled = false
                it.text = it.text.toString().lowercase(Locale.ENGLISH)
            }
            symbolButtonList.forEach {
                it.isEnabled = false
            }
            binding.caseBtn.isEnabled = false
            binding.caseBtn.icon = ContextCompat
                .getDrawable(context, R.drawable.ic_keyboard_arrow_up_24dp)
            binding.delBtn.isEnabled = false
        }
    }

    private fun init() {
        binding = ComponentKeyboardBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        textColor = context.getColor(R.color.color_on_surface)
        letterButtonList = listOf(
            // first row
            binding.qBtn, binding.wBtn, binding.eBtn, binding.rBtn, binding.tBtn,
            binding.yBtn, binding.uBtn, binding.iBtn, binding.oBtn, binding.pBtn,
            // second row
            binding.aBtn, binding.sBtn, binding.dBtn, binding.fBtn, binding.gBtn,
            binding.hBtn, binding.jBtn, binding.kBtn, binding.lBtn,
            // third row
            binding.zBtn, binding.xBtn, binding.cBtn, binding.vBtn,
            binding.bBtn, binding.nBtn, binding.mBtn
        )
        letterButtonList.forEach {
            it.setOnClickListener(letterListener)
        }
        // symbol
        symbolButtonList = listOf(
            binding.hyphenBtn, binding.apostropheBtn
        )
        symbolButtonList.forEach {
            it.setOnClickListener(symbolListener)
        }
        // function
        binding.caseBtn.setOnClickListener(caseListener)
        binding.delBtn.setOnClickListener(deleteListener)
        binding.delBtn.setOnLongClickListener(deleteLongClickListener)
    }

    private val letterListener = OnClickListener { buttonView ->
        textView?.apply {
            textSize = 24f
            val button = buttonView as MaterialButton
            var letter = button.text.toString()
            if (upperCase) {
                letter = letter.uppercase(Locale.ENGLISH)
            }
            if (text == textHint) {
                text = letter
                setTextColor(textColor)
            } else {
                val current = text.toString()
                val result = current + letter
                text = result
            }
        }
    }

    private val symbolListener = OnClickListener { buttonView ->
        textView?.apply {
            val button = buttonView as MaterialButton
            val symbol = button.text.toString()
            if (text == textHint) {
                text = symbol
                setTextColor(textColor)
            } else {
                val current = text.toString()
                val result = current + button.text.toString()
                text = result
            }
        }
    }

    private val caseListener = OnClickListener { buttonView ->
        upperCase = !upperCase
        val button = buttonView as MaterialButton
        if (upperCase) {
            button.icon = ContextCompat
                .getDrawable(context, R.drawable.ic_keyboard_arrow_down_24dp)
        } else {
            button.icon = ContextCompat
                .getDrawable(context, R.drawable.ic_keyboard_arrow_up_24dp)
        }
        letterButtonList.forEach {
            val buttonName = context.resources.getResourceEntryName(it.id)
            val letter = buttonName.first().toString()
            if (upperCase) {
                it.text = letter.uppercase(Locale.ENGLISH)
            } else {
                it.text = letter.lowercase(Locale.ENGLISH)
            }
        }
    }

    private val deleteListener = OnClickListener {
        textView?.apply {
            if (text == textHint) {
                text = ""
                setTextColor(textColor)
            } else {
                val current = text.toString()
                val result = current.dropLast(1)
                text = result
            }
        }
    }

    private val deleteLongClickListener = OnLongClickListener {
        textView?.apply {
            if (text == textHint) {
                text = ""
                setTextColor(textColor)
            } else {
                text = ""
            }
        }
        true
    }

}