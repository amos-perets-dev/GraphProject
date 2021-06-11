package com.example.lumenassignment.util.graph

import android.content.res.Resources
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails
import kotlin.math.abs

class GraphHelper(
    private val resources: Resources,
    private val xStep: Float
) : IGraphHelper {

    override fun createGraphDetails(listPoint: List<Float>): GraphDetails {
        val heightPixels = (resources
            .displayMetrics.heightPixels).toFloat()
        val gap = (heightPixels * 0.02).toFloat()
        val heightPixelsMinusGap = (heightPixels - gap)


        var min = listPoint.minOrNull() ?: 0F
        var max = listPoint.maxOrNull() ?: 0F
        val offset = (abs(min))

        val proportion = when {
            min < 0 -> {
                heightPixelsMinusGap / (max - min + offset)
            }
            max > heightPixelsMinusGap -> {
                (max - heightPixelsMinusGap) / max
            }
            else -> {
                1F
            }
        }

        val listsYPoints = getListsYPoints(proportion, listPoint, min, offset)

        min = listsYPoints.minOrNull() ?: 0F
        max = listsYPoints.maxOrNull() ?: 0F

        val xAxis = (min + max) / 2

        return GraphDetails(
            xStep,
            xAxis,
            listsYPoints
        )

    }

    private fun getListsYPoints(
        proportion: Float,
        listPoint: List<Float>,
        min: Float,
        offset: Float
    ): List<Float> {
        return listPoint.map { point ->
            return@map if (proportion != 1F) {
                if (min < 0) {
                    (point + offset) * proportion
                } else {
                    point * proportion
                }
            } else {
                point
            }
        }
    }

}