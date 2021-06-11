package com.example.lumenassignment.util.graph

import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails

interface IGraphHelper {

    /**
     * Create details for the grph view [GraphView]
     *
     * @return [GraphDetails]
     */
    fun createGraphDetails(listPoint: List<Float>) : GraphDetails
}