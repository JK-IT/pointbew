package desoft.studio.webpoint.kustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import desoft.studio.webpoint.R

class ViewFinderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)
{
    private var mMaskPaint: Paint? = null
    private var mFramePaint: Paint? = null
    private var mPath: Path? = null
    private var mFrameRect: Rect? = null
    private var mFrameCornersSize = 0
    private var mFrameCornersRadius = 0
    private var mFrameRatioWidth = 1f
    private var mFrameRatioHeight = 1f
    private var mFrameSize = 0.75f


    init {
        mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint?.style = Paint.Style.FILL;
        //mMaskPaint?.color = ContextCompat.getColor(context, R.color.miniTitle);
        mFramePaint = Paint(Paint.ANTI_ALIAS_FLAG);
        mFramePaint?.style = Paint.Style.STROKE;
        //mFramePaint?.color = ContextCompat.getColor(context, R.color.common_butt_bg);
        mPath = Path().also {
            it.fillType = Path.FillType.EVEN_ODD;
        }
    }


    override fun onDraw(canvas: Canvas?) {
        val frame = mFrameRect ?: return
        val width = width
        val height = height
        val top: Float = frame.top.toFloat();
        val left: Float = frame.left.toFloat();
        val right: Float = frame.right.toFloat();
        val bottom: Float = frame.bottom.toFloat();
        val frameCornersSize = mFrameCornersSize.toFloat()
        val frameCornersRadius = mFrameCornersRadius.toFloat()
        val path = mPath !!
        if (frameCornersRadius > 0) {
            val normalizedRadius = Math.min(frameCornersRadius, Math.max(frameCornersSize - 1, 0f))
            path.reset()
            path.moveTo(left, top + normalizedRadius)
            path.quadTo(left, top, left + normalizedRadius, top)
            path.lineTo(right - normalizedRadius, top)
            path.quadTo(right, top, right, top + normalizedRadius)
            path.lineTo(right, bottom - normalizedRadius)
            path.quadTo(right, bottom, right - normalizedRadius, bottom)
            path.lineTo(left + normalizedRadius, bottom)
            path.quadTo(left, bottom, left, bottom - normalizedRadius)
            path.lineTo(left, top + normalizedRadius)
            path.moveTo(0f, 0f)
            path.lineTo(width.toFloat(), 0f)
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.lineTo(0f, 0f)
            canvas !!.drawPath(path, mMaskPaint !!)
            path.reset()
            path.moveTo(left, top + frameCornersSize)
            path.lineTo(left, top + normalizedRadius)
            path.quadTo(left, top, left + normalizedRadius, top)
            path.lineTo(left + frameCornersSize, top)
            path.moveTo(right - frameCornersSize, top)
            path.lineTo(right - normalizedRadius, top)
            path.quadTo(right, top, right, top + normalizedRadius)
            path.lineTo(right, top + frameCornersSize)
            path.moveTo(right, bottom - frameCornersSize)
            path.lineTo(right, bottom - normalizedRadius)
            path.quadTo(right, bottom, right - normalizedRadius, bottom)
            path.lineTo(right - frameCornersSize, bottom)
            path.moveTo(left + frameCornersSize, bottom)
            path.lineTo(left + normalizedRadius, bottom)
            path.quadTo(left, bottom, left, bottom - normalizedRadius)
            path.lineTo(left, bottom - frameCornersSize)
            canvas !!.drawPath(path, mFramePaint !!)
        } else {
            path.reset()
            path.moveTo(left, top)
            path.lineTo(right, top)
            path.lineTo(right, bottom)
            path.lineTo(left, bottom)
            path.lineTo(left, top)
            path.moveTo(0f, 0f)
            path.lineTo(width.toFloat(), 0f)
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.lineTo(0f, 0f)
            canvas !!.drawPath(path, mMaskPaint !!)
            path.reset()
            path.moveTo(left, top + frameCornersSize)
            path.lineTo(left, top)
            path.lineTo(left + frameCornersSize, top)
            path.moveTo(right - frameCornersSize, top)
            path.lineTo(right, top)
            path.lineTo(right, top + frameCornersSize)
            path.moveTo(right, bottom - frameCornersSize)
            path.lineTo(right, bottom)
            path.lineTo(right - frameCornersSize, bottom)
            path.moveTo(left + frameCornersSize, bottom)
            path.lineTo(left, bottom)
            path.lineTo(left, bottom - frameCornersSize)
            canvas !!.drawPath(path, mFramePaint !!)
        }
    }

    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int,
        bottom: Int
    ) {
        invalidateFrameRect(right - left, bottom - top)
    }

    // + --------->>-------->>--------->>*** -->>----------->>>>

    fun getFrameRect(): Rect? {
        return mFrameRect
    }

    fun setFrameAspectRatio(
        @FloatRange(from = 0.0, fromInclusive = false) ratioWidth: Float,
        @FloatRange(from = 0.0, fromInclusive = false) ratioHeight: Float
    ) {
        mFrameRatioWidth = ratioWidth
        mFrameRatioHeight = ratioHeight
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.0, fromInclusive = false)
    fun getFrameAspectRatioWidth(): Float {
        return mFrameRatioWidth
    }

    fun setFrameAspectRatioWidth(
        @FloatRange(from = 0.0, fromInclusive = false) ratioWidth: Float
    ) {
        mFrameRatioWidth = ratioWidth
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.0, fromInclusive = false)
    fun getFrameAspectRatioHeight(): Float {
        return mFrameRatioHeight
    }

    fun setFrameAspectRatioHeight(
        @FloatRange(from = 0.0, fromInclusive = false) ratioHeight: Float
    ) {
        mFrameRatioHeight = ratioHeight
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @ColorInt
    fun getMaskColor(): Int {
        return mMaskPaint !!.color
    }

    fun setMaskColor(@ColorInt color: Int) {
        mMaskPaint !!.color = color
        if (isLaidOut) {
            invalidate()
        }
    }

    @ColorInt
    fun getFrameColor(): Int {
        return mFramePaint !!.color
    }

    fun setFrameColor(@ColorInt color: Int) {
        mFramePaint !!.color = color
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameThickness(): Int {
        return mFramePaint !!.strokeWidth.toInt()
    }

    fun setFrameThickness(@Px thickness: Int) {
        mFramePaint !!.strokeWidth = thickness.toFloat()
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameCornersSize(): Int {
        return mFrameCornersSize
    }

    fun setFrameCornersSize(@Px size: Int) {
        mFrameCornersSize = size
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameCornersRadius(): Int {
        return mFrameCornersRadius
    }

    fun setFrameCornersRadius(@Px radius: Int) {
        mFrameCornersRadius = radius
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.1, to = 1.0)
    fun getFrameSize(): Float {
        return mFrameSize
    }

    fun setFrameSize(@FloatRange(from = 0.1, to = 1.0) size: Float) {
        mFrameSize = size
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    private fun invalidateFrameRect() {
        invalidateFrameRect(width, height)
    }

    private fun invalidateFrameRect(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            val viewAR = width.toFloat() / height.toFloat()
            val frameAR = mFrameRatioWidth / mFrameRatioHeight
            val frameWidth: Int
            val frameHeight: Int
            if (viewAR <= frameAR) {
                frameWidth = Math.round(width * mFrameSize)
                frameHeight = Math.round(frameWidth / frameAR)
            } else {
                frameHeight = Math.round(height * mFrameSize)
                frameWidth = Math.round(frameHeight * frameAR)
            }
            val frameLeft = (width - frameWidth) / 2
            val frameTop = (height - frameHeight) / 2
            mFrameRect = Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight)
        }
    }
}