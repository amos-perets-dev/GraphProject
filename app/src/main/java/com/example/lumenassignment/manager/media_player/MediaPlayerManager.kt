package com.example.lumenassignment.manager.media_player

import android.util.Log
import com.example.lumenassignment.assignment.lumen.me.model.GraphState
import com.example.lumenassignment.assignment.lumen.me.model.MediaPlayerState
import com.example.lumenassignment.subscribePro
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MediaPlayerManager(private val intervalGraphDraw: Long) : IMediaPlayerManager {

    private val intervalGraphDrawFast = 0L

    private var progressLength = 0
    private var indexPoint = 0

    private var disposable: Disposable? = null

    private val progressPoint = PublishSubject.create<GraphState>()

    private val drawGraph = GraphState.DrawGraph
    private val eraseGraph = GraphState.EraseGraph

    override fun addProgressLength(progressLength: Int) {
        this.progressLength = progressLength
    }

    override fun setState(mediaPlayerState: MediaPlayerState) {
        when (mediaPlayerState) {
            is MediaPlayerState.Play -> play(intervalGraphDraw)
            is MediaPlayerState.Pause -> stop()
            is MediaPlayerState.Rewind -> rewind(intervalGraphDraw)
            is MediaPlayerState.PlayFast -> playFast(mediaPlayerState.index)
        }
    }

    private fun playFast(index: Int) {
        dispose()
        if (index <= indexPoint) {

            eraseGraph.index = index
            notifyState(eraseGraph)
            indexPoint = index

        } else {

            drawGraph.index = index
            notifyState(drawGraph)
            indexPoint = index
        }
    }

    override fun play(interval: Long) {

        dispose()
        disposable = getInterval(intervalGraphDraw)
            .filter { indexPoint + 1 < progressLength }
            .doOnNext {
                indexPoint++
                drawGraph.index = indexPoint
                notifyState(drawGraph)
            }
            ?.subscribePro()
    }

    private fun getInterval(intervalGraphDraw: Long): Observable<Long> {
        return Observable.interval(
            intervalGraphDraw,
            TimeUnit.MILLISECONDS,
            AndroidSchedulers.mainThread()
        )
    }

    override fun stop() {
        disposable?.dispose()
    }

    override fun rewind(intervalGraphDraw: Long) {
        dispose()

        if (indexPoint - 1 < 0) return
        disposable = getInterval(intervalGraphDraw)
            .filter { indexPoint >= 0 }
            .doOnNext {
                indexPoint--
                eraseGraph.index = indexPoint
                notifyState(eraseGraph)
            }
            .subscribePro()
    }

    private fun notifyState(graphState: GraphState) {
        graphState.index = indexPoint
        progressPoint.onNext(graphState)
    }

    override fun getProgress(): Observable<GraphState>? =
        progressPoint.hide()

    override fun dispose() {
        disposable?.dispose()
    }

}