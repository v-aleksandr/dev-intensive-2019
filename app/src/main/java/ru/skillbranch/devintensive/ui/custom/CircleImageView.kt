package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.RectF
import androidx.annotation.ColorRes
import kotlin.math.min
import android.graphics.Bitmap
import android.graphics.PorterDuffXfermode
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import androidx.constraintlayout.solver.widgets.WidgetContainer.getBounds
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.graphics.toColorLong


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private val scale = resources.displayMetrics.scaledDensity
    private var bitmapPaint = Paint()
    private var borderPaint = Paint()
    private var bitmapRadius = 0f
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH * scale

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH * scale)
            a.recycle()
            setup()
        }
    }

    private fun setup() {
        viewTreeObserver.addOnGlobalLayoutListener {
            bitmapRadius = measuredWidth.toFloat() / 2 // or ... = width or ... = this.layoutParams.width

            setBitmapPaint()
            setBorderPaint()

            viewTreeObserver.removeOnGlobalLayoutListener {}
        }
    }

    private fun setBitmapPaint() {
        /*val paddingRect = Rect()
        drawable.getPadding(paddingRect)*/
        /*val bitmap = Bitmap.createScaledBitmap( //scaling after change border width
            drawable.toBitmap(),
            (2 * (bitmapRadius - borderWidth)).toInt(),
            (2 * (bitmapRadius - borderWidth)).toInt(),
            false
        )*/
        val bitmap = Bitmap.createScaledBitmap( // no scaling after change border width
            drawable.toBitmap(),
            (2 * bitmapRadius).toInt(),
            (2 * bitmapRadius).toInt(),
            false
        )
        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun setBorderPaint() {
        val bitmap = Bitmap.createBitmap(
            (2 * bitmapRadius).toInt(),
            (2 * bitmapRadius).toInt(),
            Bitmap.Config.ARGB_8888
        )
        bitmap.eraseColor(borderColor)
        borderPaint.isAntiAlias = true
        borderPaint.shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(bitmapRadius, bitmapRadius, bitmapRadius, borderPaint)
        canvas.drawCircle(bitmapRadius, bitmapRadius, bitmapRadius - borderWidth, bitmapPaint)
    }

    @Dimension
    fun getBorderWidth(): Int = (borderWidth / scale).toInt()

    @ColorInt
    fun getBorderColor(): Int = borderColor

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp * scale
//        setBitmapPaint() //only for scaling bitmap(does not work properly
        invalidate()
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        setBorderPaint()
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = colorId
        setBorderPaint()
        invalidate()
    }

}