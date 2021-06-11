package com.example.lumenassignment.manager

import android.content.Context
import android.util.Log
import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import com.example.lumenassignment.repo.IBreathRepo
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.io.InputStream


class ParseJsonManager(
    private val context: Context,
    private val jsonName: String,
    private val repo: IBreathRepo,
    private val gson: Gson
) : IParseJsonManager {

    override fun initData(): Disposable? {
        return Observable.fromCallable { context.assets.open(jsonName) }
            .subscribeOn(Schedulers.io())
            .map(this::inputStreamToString)
            .map(this::parseJson)
            .map { caretMap(it) }
            .doOnNext(repo::addData)
            .subscribe({}, Throwable::printStackTrace)
    }

    private fun parseJson(json: String): Array<BreathItem>? {
        return gson.fromJson(json, Array<BreathItem>::class.java)
    }

    private fun caretMap(dataList: Array<BreathItem>): HashMap<String, BreathItem> {
        return dataList.associateBy(
            { it.date.toString() },
            { it }) as HashMap<String, BreathItem>
    }

    private fun inputStreamToString(jsonInputStream: InputStream): String? {
        return try {
            val bytes = ByteArray(jsonInputStream.available())
            jsonInputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            null
        }
    }


}