package com.example.lumenassignment.util.graph

import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails

interface IGraphHelper {

    fun createGraphDetails(listPoint: List<Float>) : GraphDetails
}