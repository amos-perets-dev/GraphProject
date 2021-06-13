package com.example.lumenassignment.screens.graph.media_player

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.MediaPlayerState
import com.jakewharton.rxbinding2.widget.RxSeekBar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_graph.view.*


class MediaPlayerViewHolder(private val view: View) {

    private val activeColor = ContextCompat.getColor(view.context, R.color.teal_200)

    private val buttonPlay = view.button_play
    private val buttonPause = view.button_pause
    private val buttonRewind = view.button_rewind

    private var prevButton: AppCompatImageView? = null

    private val seekBar = view.seek_bar_graph_progress

    private val mediaPlayerState =
        PublishSubject.create<Pair<MediaPlayerState, View>>()

    init {

        buttonPlay.setOnClickListener {
            mediaPlayerState.onNext(Pair(MediaPlayerState.Play, buttonPlay))
        }

        buttonPause.setOnClickListener {
            mediaPlayerState.onNext(Pair(MediaPlayerState.Pause, buttonPause))

        }

        buttonRewind.setOnClickListener {
            mediaPlayerState.onNext(Pair(MediaPlayerState.Rewind, buttonRewind))
        }
    }

    fun getMediaPlayerState(): Observable<MediaPlayerState> {
        return mediaPlayerState.hide().distinctUntilChanged()
            .doOnNext {
                val view = it.second
                if (view is AppCompatImageView) {
                    prevButton?.setColorFilter(Color.BLACK)
                    view.setColorFilter(activeColor)
                    prevButton = view
                }
            }
            .map { it.first }
    }

    fun setProgressLength(progressLength: Int) {
        this.seekBar.max = progressLength - 1
    }

    fun setProgress(progress: Int) {
        this.seekBar.progress = progress
    }

    fun getSeekBarProgress(): Observable<Int> =
        RxSeekBar.userChanges(seekBar)
            .skipInitialValue()
            .distinctUntilChanged()
            .doOnNext { mediaPlayerState.onNext(Pair(MediaPlayerState.None, seekBar)) }

}