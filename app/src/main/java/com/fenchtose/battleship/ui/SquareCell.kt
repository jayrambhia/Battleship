package com.fenchtose.battleship.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.fenchtose.battleship.R
import com.fenchtose.battleship.models.Direction

class SquareCell: View {

    private var cell: Cell? = null

    private val shipPaint: Paint
    private val shipHitPaint: Paint
    private val hitPaint: Paint
    private val missedPaint: Paint
    private val opponentHitPaint: Paint
    private val opponentMissedPaint: Paint
    private val borderPaint: Paint

    private val rect: Rect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        shipPaint = initPaint(R.color.cell_my_ship, cap = Paint.Cap.ROUND)
        shipHitPaint = initPaint(R.color.cell_my_ship_hit)
        hitPaint = initPaint(R.color.cell_ship_hit)
        opponentMissedPaint = initPaint(R.color.cell_opponent_miss)
        opponentHitPaint = initPaint(R.color.cell_ship_hit)
        missedPaint = initPaint(R.color.cell_ship_miss)
        borderPaint = initPaint(R.color.cell_border, Paint.Style.STROKE, 2f)
    }

    private fun initPaint(@ColorRes color: Int, style: Paint.Style = Paint.Style.FILL, width: Float? = null, cap: Paint.Cap? = null): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = ContextCompat.getColor(context, color)
            this.style = style
            width?.let { this.strokeWidth = it }
            cap?.let { this.strokeCap = it }
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
        cell?.apply {
            if (hasShip && direction != null) {
                when (direction) {
                    Direction.HORIZONTAL -> {
                        rect.bottom = canvas.height - 64
                        rect.right = canvas.width
                    }

                    Direction.VERTICAL -> {
                        rect.bottom = canvas.height
                        rect.right = canvas.width - 64
                    }
                }
                val paint = if (!opponentHit) shipPaint else shipHitPaint
                canvas.drawRect(rect, paint)
            }

            if (userHit) {
                canvas.drawCircle((canvas.width - 24).toFloat(), (canvas.height - 24).toFloat(), 16f, hitPaint)
            } else if (userMissed) {
                canvas.drawCircle((canvas.width - 24).toFloat(), (canvas.height - 24).toFloat(), 16f, missedPaint)
            }

            if (opponentHit) {
                canvas.drawCircle(24f, 24f, 16f, opponentHitPaint)
            } else if (opponentMissed) {
                canvas.drawCircle(24f, 24f, 16f, opponentMissedPaint)
            }
        }


    }

    fun bind(cell: Cell) {
        this.cell = cell
        invalidate()
    }
}