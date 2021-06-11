package com.example.lumenassignment.repo

import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import io.reactivex.Observable

interface IBreathRepo {
    fun getDataList(): Observable<List<BreathItem>>?
    fun addData(breathItemsMap: HashMap<String, BreathItem>)
    fun getPointsById(date : String): Observable<ArrayList<Float>>?
}