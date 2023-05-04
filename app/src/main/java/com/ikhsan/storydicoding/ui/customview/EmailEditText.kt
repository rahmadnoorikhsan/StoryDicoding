package com.ikhsan.storydicoding.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ikhsan.storydicoding.R

class EmailEditText: AppCompatEditText {

    private lateinit var bgEditText: Drawable
    private lateinit var icEmail: Drawable
    private var colorHint: Int? = null
    private var hintText: String = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = bgEditText
        setHintTextColor(colorHint as Int)
        setButtonDrawables(startOfTheText = icEmail)
        compoundDrawablePadding = 40
        hint = hintText
    }

    private fun init() {
        bgEditText = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        icEmail = ContextCompat.getDrawable(context, R.drawable.custom_email_icon) as Drawable
        colorHint = ContextCompat.getColor(context, R.color.gray)
        hintText = context.getString(R.string.email)
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !isValidEmail(s)) error = context.getString(R.string.email_error)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun isValidEmail(email: CharSequence) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}