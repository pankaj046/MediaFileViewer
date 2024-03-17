package com.github.pankaj046.library

import android.R.attr.radius
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


@SuppressLint("Recycle")
class RoundedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    val DEFAULT_STROKE_COLOR = Color.BLACK
    val DEFAULT_BACKGROUND_COLOR = Color.WHITE
    val DEFAULT_TEXT_COLOR = Color.BLACK

    fun setStyleBackground(typedArray: TypedArray) {
        val bgColor = typedArray.getColor(R.styleable.media_selectorBackgroundColor, DEFAULT_BACKGROUND_COLOR)
        val strokeColor = typedArray.getColor(R.styleable.media_selectorStrokeColor, DEFAULT_STROKE_COLOR)
        val textColor = typedArray.getColor(R.styleable.media_selectorTextColor, DEFAULT_TEXT_COLOR)

        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius.toFloat())
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.setStroke(1.0f, strokeColor)
        shapeDrawable.fillColor = ColorStateList.valueOf(bgColor)
        this.setTextColor(textColor)

        ViewCompat.setBackground(this, shapeDrawable)
    }
}