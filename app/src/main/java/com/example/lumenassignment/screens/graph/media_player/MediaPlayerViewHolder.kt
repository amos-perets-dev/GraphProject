package com.example.lumenassignment.screens.graph.media_player

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
    private val inActiveColor = ContextCompat.getColor(view.context, R.color.black)

    private val buttonPlay = view.button_play
    private val buttonPause = view.button_pause
    private val buttonRewind = view.button_rewind

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
        return mediaPlayerState.hide()
            .distinctUntilChanged()
            .scan { prevClick, currClick ->
                setButtonState(prevClick.second, inActiveColor)
                currClick
            }
            .doOnNext { currClick ->
                setButtonState(currClick.second, activeColor)
            }
            .map { it.first }
    }

    private fun setButtonState(view: View, color: Int) {
        if (view is AppCompatImageView) {
            view.setColorFilter(color)
        }
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