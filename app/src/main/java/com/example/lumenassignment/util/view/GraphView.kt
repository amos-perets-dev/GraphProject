package com.example.lumenassignment.util.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails
import com.example.lumenassignment.assignment.lumen.me.model.GraphState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class GraphView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    var mScroller = OverScroller(getContext(), FastOutLinearInInterpolator());

    private var graphDetails: GraphDetails? = null

    private var graphLineColor = ContextCompat.getColor(context, R.color.graph_line_color)
    private var graphLineWidth = context.resources.getDimension(R.dimen.graph_line_width)

    private var xAxisLineColor = ContextCompat.getColor(context, R.color.graph_x_axis_line_color)
    private var xAxisLineWidth = context.resources.getDimension(R.dimen.graph_line_x_axis_width)

    private var backgroundGraphColor =
        ContextCompat.getColor(context, R.color.graph_background_color)

    private var distance = 0F
    private var graphWidth = 0

    private var isValidDraw = false

    private var indexPoint = 0

    private val gestureDetector =
        GestureDetector(getContext(), object : SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent, e2: MotionEvent,
                distanceX: Float, distanceY: Float
            ): Boolean {
                // Note 0 as the x-distance to prevent horizontal scrolling
                val currDistanceX = distanceX.toInt()

                if ((distance + currDistanceX + width).toInt() in width..graphWidth) {
                    distance += distanceX
                    scrollBy(currDistanceX, 0)
                }
                return true
            }

            override fun onFling(
                e1: MotionEvent, e2: MotionEvent,
                velocityX: Float, velocityY: Float
            ): Boolean {

                val maxScrollX = graphWidth - width

                mScroller.forceFinished(true)

                mScroller.fling(
                    scrollX,  // No startX as there is no horizontal scrolling
                    0,
                    (-velocityX).toInt(),  // No velocityX as there is no horizontal scrolling
                    0,
                    0,
                    maxScrollX,
                    0,
                    0
                )

                invalidate()

                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                if (!mScroller.isFinished) {
                    mScroller.forceFinished(true)
                }
                return true
            }
        })


    private val graphLinePaint = Paint().apply {
        color = graphLineColor
        style = Paint.Style.STROKE
        strokeWidth = graphLineWidth
    }

    private val xAxisLinePaint = Paint().apply {
        color = xAxisLineColor
        style = Paint.Style.STROKE
        strokeWidth = xAxisLineWidth
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (graphDetails == null || isValidDraw.not()) return


        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY);
        }

        val xStep = graphDetails?.xStepDp ?: 0F
        val flowYPoints = graphDetails?.flowYPoints ?: arrayListOf()

        val newWidth = (flowYPoints.size * xStep).toInt()
        graphWidth = if (newWidth > this.width) {
            newWidth
        } else {
            width
        }

        val xAxis = graphDetails?.xAxis

        if (xAxis != null) {
            canvas.drawLine(0F, xAxis, graphWidth.toFloat(), xAxis, xAxisLinePaint)
        }

        val path = Path()
        path.moveTo(0F, (flowYPoints.firstOrNull() ?: 0F))

        for (index in 0..indexPoint) {

            val yPoint = flowYPoints[index]

            path.lineTo(((xStep * (index + 1))), yPoint)
        }
        canvas.drawPath(path, graphLinePaint)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    fun setGraphLineColor(@ColorRes color: Int): GraphView {
        this.graphLineColor = color
        return this
    }

    fun setBackgroundGraphColor(@ColorRes color: Int): GraphView {
        this.backgroundGraphColor = color
        return this
    }

    fun setXAxisLineColor(@ColorRes color: Int): GraphView {
        this.xAxisLineColor = color
        return this
    }

    fun setXAxisLineWidth(width: Float): GraphView {
        this.xAxisLineWidth = width
        return this
    }

    fun setGraphDetails(graphDetails: GraphDetails) {
        this.graphDetails = graphDetails
    }

    fun drawState(graphState: GraphState) {
        isValidDraw = true
        indexPoint = graphState.index
        graphLinePaint.xfermode = graphState.xfermode
        invalidate()
    }

    fun dispose(){
        isValidDraw = false
    }


}