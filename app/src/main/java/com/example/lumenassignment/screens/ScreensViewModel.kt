package com.example.lumenassignment.screens

import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails
import com.example.lumenassignment.lumen_app.base.ViewModelBase
import com.example.lumenassignment.repo.IBreathRepo
import com.example.lumenassignment.util.graph.IGraphHelper
import io.reactivex.Observable

class ScreensViewModel(
    private val date: String,
    private val repo: IBreathRepo,
    private val graphHelper: IGraphHelper
) :
    ViewModelBase() {

    fun getData(): Observable<GraphDetails>? {
        return repo.getPointsById(date)
            ?.map {
                graphHelper.createGraphDetails(it)
            }
    }

    fun loadData(): Observable<List<BreathItem>>? {
        return repo.getDataList()
    }

}