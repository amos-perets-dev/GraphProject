package com.example.lumenassignment.util.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


class GraphView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val finishDraw = PublishSubject.create<Rect>()

    init {
        if (context != null) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.graph_background_color))
        }
        invalidate()
    }

    private var graphDetails: GraphDetails? = null

    private var colorGraphLine =
        context?.let { ContextCompat.getColor(it, R.color.graph_line_color) }

    private val paint = Paint().apply {
        if (context == null) return@apply
        color = ContextCompat.getColor(context, R.color.graph_line_color)
        style = Paint.Style.STROKE
        strokeWidth = 2.5F
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TEST_GAME", "GraphView onDraw")
        val clipBounds = canvas.clipBounds
        finishDraw.onNext(clipBounds)

        val xStep = graphDetails?.xStepDp ?: 0F
        val flowYPoints = graphDetails?.flowYPoints ?: arrayListOf()

        val lp = layoutParams

        val newWidth = (flowYPoints.size * xStep).toInt()
        if (newWidth > this.width) {
            lp.width = newWidth
            layoutParams = lp
            requestLayout()
        }

        val width = canvas.clipBounds.width()

        val xAxis = graphDetails?.xAxis

        if (xAxis != null) {
            paint.color = Color.RED
            canvas.drawLine(0F, xAxis, width.toFloat(), xAxis, paint)
        }

        paint.color = colorGraphLine ?: Color.BLACK
        paint.strokeWidth = 1F

        val path = Path()
        path.moveTo(0F, (flowYPoints.firstOrNull() ?: 0F))

        for (i in flowYPoints.indices) {

            val yPoint = flowYPoints[i]

            path.lineTo(((xStep * (i + 1))), yPoint)
        }
        canvas.drawPath(path, paint)
    }

    fun isFinishDraw(): Single<Rect>? {
        return finishDraw.hide()
            .firstOrError()
    }

    fun setColorGraphLine(@ColorRes color: Int) {
        this.colorGraphLine = color
    }

    fun setYPoints(graphDetails: GraphDetails) {
        this.graphDetails = graphDetails
        invalidate()
    }

}