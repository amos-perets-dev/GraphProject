package com.example.lumenassignment.repo

import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class BreathRepo : IBreathRepo {

    private val dataList = BehaviorSubject.create<HashMap<String, BreathItem>>()

    override fun getDataList(): Observable<List<BreathItem>>? {
        return dataList
            .map {
                it.values.toList()
            }
    }

    override fun addData(breathItemsMap: HashMap<String, BreathItem>) {
        dataList.onNext(breathItemsMap)
    }

    override fun getPointsById(date: String): Observable<ArrayList<Float>>? {
        return dataList
            .map {
                it[date]
            }
            .map(BreathItem::flow)
    }
}