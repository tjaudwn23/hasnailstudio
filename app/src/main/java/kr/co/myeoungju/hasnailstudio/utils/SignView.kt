package kr.co.myeoungju.hasnailstudio.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.cardview.widget.CardView


class SignView : View, View.OnTouchListener {
    var changed = false
    var mCanvas: Canvas? = null
    var mBitmap: Bitmap? = null
    var mPaint: Paint? = null
    var lastX = 0f
    var lastY = 0f
    var mPath: Path = Path()
    var mCurveEndX = 0f
    var mCurveEndY = 0f
    var mInvalidateExtraBorder = 10
    var baseStrockWidth = 5.0f
    var isDraw = false
    var mContext: Context
    var width = 0f
    var height = 0f
    var scrollView:ScrollView? = null
    constructor(context: Context) : super(context) {
        mContext = context
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init(context)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    fun init(context: Context?) {
        setOnTouchListener(this)
        mPaint = Paint()
        mPaint?.setAntiAlias(true)
        mPaint?.setColor(Color.BLACK)
        mPaint?.setStyle(Paint.Style.STROKE)
        mPaint?.setStrokeJoin(Paint.Join.ROUND)
        mPaint?.setStrokeCap(Paint.Cap.ROUND)
        mPaint?.setStrokeWidth(baseStrockWidth)
        lastX = -1f
        lastY = -1f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = w.toFloat()
        height = h.toFloat()
        val density: Float = mContext.getResources().getDisplayMetrics().density
        val img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas()
        canvas.setBitmap(img)
        canvas.drawColor(Color.WHITE)
        mBitmap = img
        mCanvas = canvas
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap!!, 0f, 0f, null)
        } else {
            Log.e("SignView", "여기들어오냐?")
            mBitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
            mCanvas?.setBitmap(mBitmap)
            mCanvas?.drawColor(Color.WHITE)
            mCanvas?.drawBitmap(mBitmap!!, 0f, 0f, null)
        }
    }
    private fun touchDown(event: MotionEvent): Rect {
        scrollView?.requestDisallowInterceptTouchEvent(true)
        val x = event.x
        val y = event.y
        lastX = x
        lastY = y
        val mInvalidRect = Rect()
        mPath.moveTo(x, y)
        val border = mInvalidateExtraBorder
        mInvalidRect.set(
            x.toInt() - border,
            y.toInt() - border,
            x.toInt() + border,
            y.toInt() + border
        )
        mCurveEndX = x
        mCurveEndY = y
        mCanvas?.drawPath(mPath, mPaint!!)
        return mInvalidRect
    }

    private fun touchMove(event: MotionEvent): Rect {
        return processMove(event)

    }

    private fun touchUp(event: MotionEvent, cancel: Boolean): Rect {
        scrollView?.requestDisallowInterceptTouchEvent(false)
        return processMove(event)

    }

    private fun processMove(event: MotionEvent): Rect {
        Log.i("processMove", "processMove: " + event.pressure)
        val x = event.x
        val y = event.y
        val dx = Math.abs(x - lastX)
        val dy = Math.abs(x - lastY)
        val mInvalidRect = Rect()
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            val border = mInvalidateExtraBorder
            mInvalidRect.set(
                mCurveEndX.toInt() - border,
                mCurveEndY.toInt() - border,
                mCurveEndX.toInt() + border,
                mCurveEndY.toInt() + border
            )
            mCurveEndX = (x + lastX) / 2
            val cX = mCurveEndX
            mCurveEndY = (y + lastY) / 2
            val cY = mCurveEndY
            mPath.quadTo(lastX, lastY, cX, cY)
            mInvalidRect.union(
                lastX.toInt() - border,
                lastY.toInt() - border,
                lastX.toInt() + border,
                lastY.toInt() + border
            )
            mInvalidRect.union(
                cX.toInt() - border,
                cY.toInt() - border,
                cX.toInt() + border,
                cY.toInt() + border
            )
            lastX = x
            lastY = y
            mCanvas?.drawPath(mPath, mPaint!!)
        }
        return mInvalidRect
    }

    fun ClearSign() {
        //  mBitmap =  Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        //  mCanvas.setBitmap(mBitmap);
        //  mCanvas.drawBitmap(mBitmap,0,0,null);
        mCanvas?.drawColor(Color.WHITE)
        mBitmap = null
    }

    override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                Log.i("onTouchEvent", motionEvent.action.toString() + "")
                changed = true
                isDraw = false
                val rect: Rect = touchUp(motionEvent, false)
                if (rect != null) {
                    invalidate()
                }
                mPath.rewind()
                return true
            }
            MotionEvent.ACTION_DOWN -> {
                Log.i("onTouchEvent", motionEvent.action.toString() + "")
                isDraw = true
                val rect = touchDown(motionEvent)
                if (rect != null) {
                    invalidate(rect)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i("onTouchEvent", motionEvent.action.toString() + "")
                val rect = touchMove(motionEvent)
                if (rect != null) {
                    invalidate(rect)
                }
                return false
            }
        }
        return false
    }

    companion object {
        const val TOUCH_TOLERANCE = 8f
    }
}