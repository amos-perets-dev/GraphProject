package com.example.lumenassignment.screens.graph

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.GraphState
import com.example.lumenassignment.lumen_app.base.FragmentBase
import com.example.lumenassignment.screens.ScreensViewModel
import com.example.lumenassignment.screens.graph.media_player.MediaPlayerViewHolder
import com.example.lumenassignment.subscribePro
import com.example.lumenassignment.util.view.GraphView
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_graph.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GraphFragment : FragmentBase() {
    private val args: GraphFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.fragment_graph

    private val graphViewModel: ScreensViewModel by viewModel { parametersOf(args.breathDate) }

    private lateinit var graphView: GraphView
    private var mediaPlayerViewHolder: MediaPlayerViewHolder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        graphView = view.graph_view
        mediaPlayerViewHolder = MediaPlayerViewHolder(view)

        mediaPlayerViewHolder?.getSeekBarProgress()
            ?.doOnNext { graphViewModel.setManualProgress(it) }
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }

        compositeDisposable.add(
            RxView.layoutChanges(graphView)
                .flatMap {
                    graphViewModel.getData(graphView.top, graphView.bottom)
                }
                .doOnNext(graphView::setGraphDetails)
                .subscribePro()
        )

        graphViewModel.pointsGraph.observe(
            viewLifecycleOwner,
            Observer { mediaPlayerViewHolder?.setProgressLength(it) }
        )

        graphViewModel.currProgress.observe(
            viewLifecycleOwner,
            Observer(this::setProgress)
        )

        mediaPlayerViewHolder
            ?.getMediaPlayerState()
            ?.doOnNext(graphViewModel::setMediaPlayerState)
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }

    }

    private fun setProgress(graphState: GraphState) {
        mediaPlayerViewHolder?.setProgress(graphState.index)
        graphView.drawState(graphState)
    }

    override fun onDestroyView() {
//        graphView.dispose()
        super.onDestroyView()
    }
}