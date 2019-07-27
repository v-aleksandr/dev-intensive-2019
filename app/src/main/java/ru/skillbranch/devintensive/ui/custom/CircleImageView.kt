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
import ru.skillbranch.devintensive.R


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var mDrawableRect = RectF()
    private var mDrawableRadius: Float = 0f
    private var mBitmapPaint = Paint()
    private var mBorderRect = RectF()
    private var mBorderRadius: Float = 0f
    private var mBorderPaint = Paint()
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
//            setup()
        }
    }

    /* private fun setup() {

         mBitmapPaint.isAntiAlias = true
         mBitmapPaint.shader =
             BitmapShader(getBitmapFromDrawable(drawable), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

         mBorderPaint.style = Paint.Style.STROKE
         mBorderPaint.isAntiAlias = true
         mBorderPaint.color = borderColor
         mBorderPaint.strokeWidth = borderWidth.toFloat()

         mBorderRect.set(calculateBounds())
         mBorderRadius = min((mBorderRect.height() - 2 * borderWidth) / 2f, (mBorderRect.width() - 2 * borderWidth) / 2f)

         mDrawableRect.set(mBorderRect)
         mDrawableRect.inset(borderWidth.toFloat(), borderWidth.toFloat())
         mDrawableRadius = min(mDrawableRect.height() / 2f, mDrawableRect.width() / 2f)

         invalidate()
     }*/

    /* private fun calculateBounds(): RectF {
         val availableWidth = measuredWidth
         val availableHeight = measuredHeight

         val sideLength = min(availableWidth, availableHeight)

         val left = paddingLeft + (availableWidth - sideLength) / 2f
         val top = paddingTop + (availableHeight - sideLength) / 2f

         return RectF(left, top, left + sideLength, top + sideLength)
     }
 */
    /* private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
         if (drawable is BitmapDrawable) {
             return drawable.bitmap
         }

         val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
         val canvas = Canvas(bitmap)
         drawable.setBounds(0, 0, canvas.width, canvas.height)
         drawable.draw(canvas)
         return bitmap
     }*/

    override fun onDraw(canvas: Canvas) {
//        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
//        if (borderWidth > 0) {
//            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint)
//        }
//        super.onDraw(canvas)
        val drawable = drawable ?: return

        if (width == 0 || height == 0) {
            return
        }
        val b = (drawable as BitmapDrawable).bitmap
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)

        val roundBitmap = getRoundedCroppedBitmap(bitmap, min(width, height))
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val finalBitmap: Bitmap = if (bitmap.width != radius || bitmap.height != radius)
            Bitmap.createScaledBitmap(bitmap, radius, radius, false)
        else
            bitmap
        val output = Bitmap.createBitmap(finalBitmap.width, finalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, finalBitmap.width, finalBitmap.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = borderColor
        canvas.drawCircle(
            (finalBitmap.width + borderWidth)/ 2 + 0.7f,
            (finalBitmap.height + borderWidth) / 2 + 0.7f,
            finalBitmap.width / 2 + 0.1f,
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(finalBitmap, rect, rect, paint)

        return output
    }

    @Dimension
    fun getBorderWidth() : Int =borderWidth

    fun getBorderColor(): Int = borderColor

    fun setBorderWidth(dp: Int) {
        borderWidth = dp
        invalidate()
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = colorId
        invalidate()
    }
    /*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredWidth / aspectRatio).toInt()
        setMeasuredDimension(measuredWidth, newHeight)
    }*/
}