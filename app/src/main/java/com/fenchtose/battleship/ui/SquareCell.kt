package com.fenchtose.battleship.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.fenchtose.battleship.R
import com.fenchtose.battleship.models.Direction

class SquareCell: View {

    var hasShip: Boolean = false
    var shipDirection: Direction? = null
    var isHit: Boolean = false
    var opponentDidMiss: Boolean = false
    var userDidHit: Boolean = false
    var userDidMiss: Boolean = false

    var sPaint: Paint? = null
    var hPaint: Paint? = null
    var uhPaint: Paint? = null
    var umPaint: Paint? = null
    var ohPaint: Paint? = null
    var omPaint: Paint? = null

    var borderPaint: Paint? = null

    val rect: Rect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val sPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sPaint.color = ContextCompat.getColor(context, R.color.cell_my_ship)
        sPaint.style = Paint.Style.FILL
        sPaint.strokeCap = Paint.Cap.ROUND

        val hPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        hPaint.color = ContextCompat.getColor(context, R.color.cell_my_ship_hit)
        hPaint.style = Paint.Style.FILL

        val uhPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        uhPaint.color = ContextCompat.getColor(context, R.color.cell_ship_hit)
        uhPaint.style = Paint.Style.FILL

        val omPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        omPaint.color = ContextCompat.getColor(context, R.color.cell_opponent_miss)
        omPaint.style = Paint.Style.FILL

        val ohPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        ohPaint.color = ContextCompat.getColor(context, R.color.cell_ship_hit)
        ohPaint.style = Paint.Style.FILL

        val umPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        umPaint.color = ContextCompat.getColor(context, R.color.cell_ship_miss)
        umPaint.style = Paint.Style.FILL

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.color = ContextCompat.getColor(context, R.color.cell_border)
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 2f

        this.sPaint = sPaint
        this.hPaint = hPaint
        this.uhPaint = uhPaint
        this.umPaint = umPaint
        this.ohPaint = ohPaint
        this.omPaint = omPaint
        this.borderPaint = borderPaint

        if (isInEditMode) {
            hasShip = true
            shipDirection = Direction.HORIZONTAL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        rect.left = 0
        rect.top = 0
        rect.bottom = canvas.height
        rect.right = canvas.width

        canvas.drawRect(rect, borderPaint)

        if (hasShip && shipDirection != null) {
            when (shipDirection) {
                Direction.HORIZONTAL -> {
                    rect.bottom = canvas.height - 64
                    rect.right = canvas.width
                }

                Direction.VERTICAL -> {
                    rect.bottom = canvas.height
                    rect.right = canvas.width - 64
                }
            }
            val paint = if (!isHit) sPaint else hPaint
            canvas.drawRect(rect, paint)
        }

        if (userDidHit) {
            canvas.drawCircle((canvas.width - 24).toFloat(), (canvas.height - 24).toFloat(), 16f, uhPaint)
        } else if (userDidMiss) {
            canvas.drawCircle((canvas.width - 24).toFloat(), (canvas.height - 24).toFloat(), 16f, umPaint)
        }

        if (isHit) {
            canvas.drawCircle(24f, 24f, 16f, ohPaint)
        } else if (opponentDidMiss) {
            canvas.drawCircle(24f, 24f, 16f, omPaint)
        }
    }
}