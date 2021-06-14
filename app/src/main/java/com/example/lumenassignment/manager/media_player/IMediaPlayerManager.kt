package com.example.lumenassignment.manager.media_player

import com.example.lumenassignment.assignment.lumen.me.model.GraphState
import com.example.lumenassignment.assignment.lumen.me.model.MediaPlayerState
import io.reactivex.Observable

interface IMediaPlayerManager {

    /**
     * Call when the user click on the rewind button
     */
    fun rewind(intervalGraphDraw: Long)

    /**
     * Call when the user click on the play button
     */
    fun play(intervalGraphDraw: Long)

    /**
     * Call when the user click on the stop button
     */
    fun stop()

    /**
     * Get the progress's graph
     */
    fun getProgress(): Observable<GraphState>?
    fun dispose()
    fun addProgressLength(progressLength: Int)
    fun setState(mediaPlayerState: MediaPlayerState)
}