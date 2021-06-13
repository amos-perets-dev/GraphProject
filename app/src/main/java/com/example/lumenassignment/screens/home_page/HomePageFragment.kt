package com.example.lumenassignment.screens.home_page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lumenassignment.R
import com.example.lumenassignment.lumen_app.base.FragmentBase
import com.example.lumenassignment.screens.ScreensViewModel
import com.example.lumenassignment.screens.home_page.HomePageFragmentDirections.Companion.actionHomePageFragmentToGraphFragment
import com.example.lumenassignment.subscribePro
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.functions.Functions
import kotlinx.android.synthetic.main.fragment_home_page.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomePageFragment : FragmentBase() {

    private val breathAdapter by inject<BreathAdapter>()

    private val viewModel: ScreensViewModel by viewModel { parametersOf("") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.progress_bar_loader.visibility = View.VISIBLE

        view.recycler_view_breaths_items.apply {
            adapter = breathAdapter
        }

        viewModel
            .loadData()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnNext(breathAdapter::submitList)
            ?.doOnNext(Functions.actionConsumer(this::hideLoader))
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }

        breathAdapter
            .onClick()
            ?.map { actionHomePageFragmentToGraphFragment(it) }
            ?.doOnNext(findNavController()::navigate)
            ?.subscribePro()
            ?.let {
                compositeDisposable.add(
                    it
                )
            }


    }

    private fun hideLoader() {
        view?.progress_bar_loader?.visibility = View.INVISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_home_page
}