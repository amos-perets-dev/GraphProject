package com.example.lumenassignment.repo

import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import io.reactivex.Observable

interface IBreathRepo {

    /**
     * Get the data list
     *
     * @return [Observable][List][BreathItem]
     */
    fun getDataList(): Observable<List<BreathItem>>?

    /**
     * Add data to the repo[BreathRepo]
     */
    fun addData(breathItemsMap: HashMap<String, BreathItem>)

    /**
     * Get breath points
     *
     * @return [Observable][ArrayList][Float] values
     */
    fun getPointsById(date : String): Observable<ArrayList<Float>>?
}