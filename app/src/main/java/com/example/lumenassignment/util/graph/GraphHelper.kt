package com.example.lumenassignment.util.graph

import android.content.res.Resources
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails

class GraphHelper(
    private val intervalGraphDraw: Int,
    private val xStep: Float
) : IGraphHelper {
    private var newMin = 0F
    private var newMax = 0F

    override fun createGraphDetails(listPoint: List<Float>, top: Int, bottom: Int): GraphDetails {

        newMax = bottom.toFloat()
        newMin = top.toFloat()

        var min = listPoint.minByOrNull { -it } ?: 0F
        var max = listPoint.maxByOrNull { -it } ?: 0F

        val listsYPoints = getListsYPoints(listPoint, min, max)

        min = listsYPoints.minOrNull() ?: 0F
        max = listsYPoints.maxOrNull() ?: 0F

        val xAxis = ((max - min) / 2)

        return GraphDetails(
            xStep,
            xAxis,
            listsYPoints,
            intervalGraphDraw.toInt()
        )

    }

    private fun scalePoint(oldValue: Float, oldMin: Float, oldMax: Float): Float {

        val newValue: Float
        val newRange: Float
        val oldRange = (oldMax - oldMin)
        if (oldRange == 0F)
            newValue = newMin
        else {
            newRange = (newMax - newMin)
            newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin
        }

        return newValue
    }

    private fun getListsYPoints(listPoint: List<Float>, min: Float, max: Float): List<Float> {
        return listPoint.map { point ->
            val newPoint = scalePoint(point, min, max)

            return@map newPoint
        }
    }

}