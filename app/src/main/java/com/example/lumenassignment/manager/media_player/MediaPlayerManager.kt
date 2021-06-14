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
        } else {
            drawGraph.index = index
            notifyState(drawGraph)
        }
        indexPoint = index
    }

    override fun play(intervalGraphDraw: Long) {

        dispose()
        disposable = getInterval(intervalGraphDraw)
            .filter { indexPoint + 1 < progressLength }
            .map { indexPoint++ }
            .map { currIndex ->
                drawGraph.index = currIndex
                drawGraph
            }
            .doOnNext(this::notifyState)
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
            .map { indexPoint-- }
            .map { currIndex ->
                eraseGraph.index = currIndex
                eraseGraph
            }
            .doOnNext(this::notifyState)
            .subscribePro()
    }

    private fun notifyState(graphState: GraphState) {
        progressPoint.onNext(graphState)
    }

    override fun getProgress(): Observable<GraphState>? =
        progressPoint.hide()

    override fun dispose() {
        disposable?.dispose()
    }

}