package com.example.lumenassignment.screens.details

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.lumenassignment.R
import com.example.lumenassignment.lumen_app.base.FragmentBase
import com.example.lumenassignment.screens.ScreensViewModel
import com.example.lumenassignment.screens.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_graph.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GraphFragment : FragmentBase() {
    private val args: GraphFragmentArgs by navArgs()

    override fun getLayoutRes(): Int = R.layout.fragment_graph

    private val detailViewModel: ScreensViewModel by viewModel { parametersOf(args.breathDate) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val graphView = view.graph_view
        detailViewModel.getData()
            ?.doOnNext(graphView::setYPoints)
            ?.subscribe({}, Throwable::printStackTrace)?.let {
                compositeDisposable.add(
                    it
                )
            }

    }
}