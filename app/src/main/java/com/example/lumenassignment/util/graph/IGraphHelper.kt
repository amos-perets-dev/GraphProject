package com.example.lumenassignment.util.graph

import android.graphics.Rect
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails

interface IGraphHelper {

    /**
     * Create details for the grph view [GraphView]
     *@param listPoint values on the line
     * @param top graph
     * @param bottom graph
     * @return [GraphDetails]
     */
    fun createGraphDetails(listPoint: List<Float>, top: Int, bottom: Int): GraphDetails
}