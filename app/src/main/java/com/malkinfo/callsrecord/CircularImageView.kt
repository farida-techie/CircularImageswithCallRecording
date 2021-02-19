package com.malkinfo.callsrecord

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView


class CircularImageView : androidx.appcompat.widget.AppCompatImageView {
    private var borderWidth = 5
    private var viewWidth = 0
    private var viewHeight = 0
    private var image: Bitmap? = null
    private var paint: Paint? = null
    private var paintBorder: Paint? = null
    private var shader: BitmapShader? = null

    constructor(context: Context?) : super(context!!) {
        setup()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        setup()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        setup()
    }

    private fun setup() {
        // init paint
        paint = Paint()
        paint!!.isAntiAlias = true
        paintBorder = Paint()
        setBorderColor(Color.GREEN)
        paintBorder!!.isAntiAlias = true
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        this.invalidate()
    }

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null) paintBorder!!.color = borderColor
        this.invalidate()
    }

    private fun loadBitmap() {
        val bitmapDrawable = this.drawable as BitmapDrawable
        if (bitmapDrawable != null) image = bitmapDrawable.bitmap
    }

    @SuppressLint("DrawAllocation")
    public override fun onDraw(canvas: Canvas) {
        //load the bitmap
        loadBitmap()

        // init shader
        if (image != null) {
            shader = BitmapShader(Bitmap.createScaledBitmap(image!!, canvas.width, canvas.height, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint!!.shader = shader
            val circleCenter = viewWidth / 2

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
            canvas.drawCircle((circleCenter + borderWidth).toFloat(), (circleCenter + borderWidth).toFloat(), (circleCenter + borderWidth).toFloat(), paintBorder!!)
            canvas.drawCircle((circleCenter + borderWidth).toFloat(), (circleCenter + borderWidth).toFloat(), circleCenter.toFloat(), paint!!)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec, widthMeasureSpec)
        viewWidth = width - borderWidth * 2
        viewHeight = height - borderWidth * 2
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else {
            // Measure the text
            viewWidth
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int, measureSpecWidth: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            viewHeight
        }
        return result
    }
}