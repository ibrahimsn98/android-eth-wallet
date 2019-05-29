package me.ibrahimsn.wallet.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import me.ibrahimsn.wallet.R

class WaveView : View {

    private val waveGap = 90f
    private var maxRadius = 0f
    private var center = PointF(0f, 0f)
    private var initialRadius = 0f

    private var icon: Drawable? = null
    private var iconSize = 0f

    private val green = Color.parseColor("#1f212f")
    private val gradientColors = intArrayOf(modifyAlpha(green, 0.8f), modifyAlpha(green, 0.9f), green, green)

    private var waveAnimator: ValueAnimator? = null
    private var waveRadiusOffset = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    private val wavePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#f5f5f5")
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val gradientPaint = Paint(ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WaveView, 0, 0)
        val iconRes = typedArray.getResourceId(R.styleable.WaveView_icon, 0)
        iconSize = typedArray.getDimension(R.styleable.WaveView_iconSize, this.iconSize)
        typedArray.recycle()

        if (iconRes != 0)
            icon = ContextCompat.getDrawable(context, iconRes)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.set(w / 2f, h / 2f)
        maxRadius = Math.hypot(center.x.toDouble(), center.y.toDouble()).toFloat()
        initialRadius = w / waveGap

        gradientPaint.shader = RadialGradient(center.x, center.y, maxRadius,
                gradientColors, null, Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentRadius = initialRadius + waveRadiusOffset
        while (currentRadius < maxRadius) {
            canvas.drawCircle(center.x, center.y, currentRadius, wavePaint)
            currentRadius += waveGap
        }

        canvas.drawPaint(gradientPaint)

        if (icon != null) {
            icon!!.setBounds((center.x - iconSize/2).toInt(),
                    (center.y - iconSize/2).toInt(),
                    (center.x + iconSize/2).toInt(),
                    (center.y + iconSize/2).toInt())

            icon!!.draw(canvas)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        waveAnimator = ValueAnimator.ofFloat(waveGap, 0f).apply {
            addUpdateListener {
                waveRadiusOffset = it.animatedValue as Float
            }
            duration = 2000L
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    override fun onDetachedFromWindow() {
        waveAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    private fun modifyAlpha(color: Int, alpha: Float): Int {
        return color and 0x00ffffff or ((alpha * 255).toInt() shl 24)
    }
}