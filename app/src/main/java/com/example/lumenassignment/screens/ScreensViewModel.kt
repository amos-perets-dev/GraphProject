package com.example.lumenassignment.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import com.example.lumenassignment.assignment.lumen.me.model.GraphDetails
import com.example.lumenassignment.assignment.lumen.me.model.GraphState
import com.example.lumenassignment.assignment.lumen.me.model.MediaPlayerState
import com.example.lumenassignment.lumen_app.base.ViewModelBase
import com.example.lumenassignment.manager.media_player.IMediaPlayerManager
import com.example.lumenassignment.repo.IBreathRepo
import com.example.lumenassignment.subscribePro
import com.example.lumenassignment.util.graph.IGraphHelper
import io.reactivex.Observable

class ScreensViewModel(
    private val date: String,
    private val repo: IBreathRepo,
    private val graphHelper: IGraphHelper,
    private val mediaPlayerManager: IMediaPlayerManager
) :
    ViewModelBase() {

    val pointsGraph = MutableLiveData<Int>()
    val graphDetails = MutableLiveData<GraphDetails>()
    val currProgress = MutableLiveData<GraphState>()

    init {
        mediaPlayerManager.getProgress()
            ?.doOnNext(currProgress::postValue)
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

    fun getData(top: Int, bottom: Int) {
        repo.getPointsById(date)
            ?.map { points ->
                graphHelper.createGraphDetails(points, top, bottom)
            }
            ?.doOnNext {
                val size = it.flowYPoints.size
                mediaPlayerManager.addProgressLength(size)
                pointsGraph.postValue(size)
                graphDetails.postValue(it)
            }
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }

    }

    fun loadData(): Observable<List<BreathItem>>? {
        return repo.getDataList()
    }

    fun setMediaPlayerState(mediaPlayerState: MediaPlayerState) {
        mediaPlayerManager.setState(mediaPlayerState)
    }

    override fun onCleared() {
        mediaPlayerManager.dispose()
        super.onCleared()
    }

    fun setManualProgress(progress: Int) {

        val playFast = MediaPlayerState.PlayFast
        playFast.index = progress
        setMediaPlayerState(playFast)
    }

}