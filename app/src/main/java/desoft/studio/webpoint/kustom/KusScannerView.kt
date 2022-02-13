package desoft.studio.webpoint.kustom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.ImageView
import desoft.studio.webpoint.R

class KusScannerView : ViewGroup
{
    private val DEFAULT_AUTO_FOCUS_BUTTON_VISIBLE = true
    private val DEFAULT_FLASH_BUTTON_VISIBLE = true
    private val DEFAULT_AUTO_FOCUS_BUTTON_VISIBILITY = VISIBLE
    private val DEFAULT_FLASH_BUTTON_VISIBILITY = VISIBLE
    private val DEFAULT_MASK_COLOR = 0x77000000
    private val DEFAULT_FRAME_COLOR = Color.WHITE
    private val DEFAULT_AUTO_FOCUS_BUTTON_COLOR = Color.WHITE
    private val DEFAULT_FLASH_BUTTON_COLOR = Color.WHITE
    private val DEFAULT_FRAME_THICKNESS_DP = 2f
    private val DEFAULT_FRAME_ASPECT_RATIO_WIDTH = 1f
    private val DEFAULT_FRAME_ASPECT_RATIO_HEIGHT = 1f
    private val DEFAULT_FRAME_CORNER_SIZE_DP = 50f
    private val DEFAULT_FRAME_CORNERS_RADIUS_DP = 0f
    private val DEFAULT_FRAME_SIZE = 0.75f
    private val BUTTON_SIZE_DP = 56f
    private val FOCUS_AREA_SIZE_DP = 20f
    private var mPreviewView: SurfaceView? = null
    private var mViewFinderView: ViewFinderView? = null
    private var mAutoFocusButton: ImageView? = null
    private var mFlashButton: ImageView? = null
    private val mPreviewSize: Point? = null
    //private val mSizeListener: com.bytesbee.qrcode.scanner.CodeScannerView.SizeListener? = null
    //private val mCodeScanner: CodeScanner? = null
    private var mButtonSize = 0
    private val mAutoFocusButtonColor = 0
    private val mFlashButtonColor = 0
    private var mFocusAreaSize = 0

    constructor(context : Context) : super(context)
    {
        initialize(context, null, 0, 0);
    }
    constructor(context: Context, attrs : AttributeSet) : super(context, attrs)
    {
        initialize(context, attrs, 0, 0);
    }

    constructor(context: Context, attrs : AttributeSet, defstyleattr: Int) : super(context, attrs, defstyleattr)
    {
        initialize(context, attrs, defstyleattr, 0);
    }

    private fun initialize(context : Context, attrs : AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
    {
        mPreviewView = SurfaceView(context);
        mPreviewView?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewFinderView = ViewFinderView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        };
        var density : Float = context.resources.displayMetrics.density;
        mButtonSize = Math.round(density * BUTTON_SIZE_DP);
        mFocusAreaSize = Math.round(density * FOCUS_AREA_SIZE_DP);
        mAutoFocusButton = ImageView(context);
        mAutoFocusButton?.apply {
            layoutParams = LayoutParams(mButtonSize, mButtonSize);
            scaleType = ImageView.ScaleType.CENTER;
            setImageResource(R.drawable.ic_code_scanner_auto_focus_on);
            setOnClickListener {

            }
        }

        mFlashButton = ImageView(context)
        mFlashButton?.apply {
            setLayoutParams(LayoutParams(mButtonSize, mButtonSize));
            setScaleType(ImageView.ScaleType.CENTER);
            setImageResource(R.drawable.ic_flash);
            setOnClickListener {

            }
        };

        if(attrs ==null) {
            mViewFinderView?.apply {
                setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH, DEFAULT_FRAME_ASPECT_RATIO_HEIGHT);
                setMaskColor(DEFAULT_MASK_COLOR);
                setFrameColor(DEFAULT_FRAME_COLOR);
                setFrameThickness(Math.round(DEFAULT_FRAME_THICKNESS_DP * density));
                setFrameCornersSize(Math.round(DEFAULT_FRAME_CORNER_SIZE_DP * density));
                setFrameCornersRadius(Math.round(DEFAULT_FRAME_CORNERS_RADIUS_DP * density));
                setFrameSize(DEFAULT_FRAME_SIZE);
            }
            mAutoFocusButton?.setColorFilter(DEFAULT_AUTO_FOCUS_BUTTON_COLOR);
            mFlashButton?.setColorFilter(DEFAULT_FLASH_BUTTON_COLOR);
            mAutoFocusButton?.visibility = (DEFAULT_AUTO_FOCUS_BUTTON_VISIBILITY);
            mFlashButton?.visibility = DEFAULT_FLASH_BUTTON_VISIBILITY;
        } else {
            val a: TypedArray? = null;
            try {

            } finally {
                if(a != null) {
                    a.recycle();
                }
            }
        }
        addView(mPreviewView)
        addView(mViewFinderView)
        addView(mAutoFocusButton)
        addView(mFlashButton)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        performLayout(r-l, b-t);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        performLayout(w, h);
    }

    private fun performLayout(width: Int, height: Int) {
        val previewSize = mPreviewSize
        if (previewSize == null) {
            mPreviewView !!.layout(0, 0, width, height)
        } else {
            var frameLeft = 0
            var frameTop = 0
            var frameRight = width
            var frameBottom = height
            val previewWidth: Int = previewSize.x;
            if (previewWidth > width) {
                val d = (previewWidth - width) / 2
                frameLeft -= d
                frameRight += d
            }
            val previewHeight: Int = previewSize.y;
            if (previewHeight > height) {
                val d = (previewHeight - height) / 2
                frameTop -= d
                frameBottom += d
            }
            mPreviewView !!.layout(frameLeft, frameTop, frameRight, frameBottom)
        }
        mViewFinderView !!.layout(0, 0, width, height)
        val buttonSize = mButtonSize
        mAutoFocusButton !!.layout(0, 0, buttonSize, buttonSize)
        mFlashButton !!.layout(width - buttonSize, 0, width, buttonSize)
    }
}