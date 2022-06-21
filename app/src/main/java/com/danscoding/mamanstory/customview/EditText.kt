package com.danscoding.mamanstory.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.danscoding.mamanstory.R

class EditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var icWarning: Drawable
    private lateinit var icPassword: Drawable

    private fun init(){
        icWarning = ContextCompat.getDrawable(context, R.drawable.ic_warning) as Drawable
        icPassword = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_open_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 6){
                    showWarning()
                }else{
                    hideWarning()
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null){
            val cleanBtnStart: Float
            val cleanBtnEnd: Float
            var isCleanButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL){
                cleanBtnEnd = (icWarning.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < cleanBtnEnd -> isCleanButtonClicked = true
                }
            } else {
                cleanBtnStart = ( width - paddingEnd - icWarning.intrinsicWidth ).toFloat()
                when {
                    event.x > cleanBtnStart -> isCleanButtonClicked = true
                }
            }
            if (isCleanButtonClicked){
                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        icWarning = ContextCompat.getDrawable(context, R.drawable.ic_warning) as Drawable
                        showWarning()
                        if (v != null){
                            Toast.makeText(v.context, "maman", Toast.LENGTH_SHORT).show()
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        icWarning = ContextCompat.getDrawable(context, R.drawable.ic_warning) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideWarning()
                        return true
                    }
                    else -> return false
                }
            }
        }
        return false
    }

    private fun showWarning() {
        setWarningDrawables(startOfTheText = icPassword, endOfTheText = icWarning)
    }
    private fun hideWarning() {
        setWarningDrawables(startOfTheText = icPassword, endOfTheText = null)
    }

    private fun setWarningDrawables(startOfTheText: Drawable? = null, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

}