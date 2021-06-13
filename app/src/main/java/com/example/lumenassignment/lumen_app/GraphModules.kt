package com.example.lumenassignment.lumen_app

import android.content.Context
import com.example.lumenassignment.BuildConfig
import com.example.lumenassignment.R
import com.example.lumenassignment.lumen_app.configuration.GraphConfiguration
import com.example.lumenassignment.lumen_app.configuration.IGraphConfiguration
import com.example.lumenassignment.manager.media_player.IMediaPlayerManager
import com.example.lumenassignment.manager.media_player.MediaPlayerManager
import com.example.lumenassignment.manager.parse.ParseJsonManager
import com.example.lumenassignment.manager.parse.IParseJsonManager
import com.example.lumenassignment.repo.BreathRepo
import com.example.lumenassignment.repo.IBreathRepo
import com.example.lumenassignment.screens.home_page.BreathAdapter
import com.example.lumenassignment.screens.ScreensViewModel
import com.example.lumenassignment.screens.main.MainViewModel
import com.example.lumenassignment.util.graph.GraphHelper
import com.example.lumenassignment.util.graph.IGraphHelper
import com.google.gson.Gson
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class GraphModules {
    fun createModules(context: Context): List<Module> {

        val appModules = createAppModules(context)
        val homePageModule = createHomePageModule()
        val mainModule = createMainModule()

        return listOf(appModules, mainModule, homePageModule)
    }

    private fun createAppModules(context: Context): Module {

        return module {
            single<IBreathRepo> {
                BreathRepo()
            }
            single<IParseJsonManager> {
                ParseJsonManager(context, BuildConfig.JSON_FILE_NAME, get(), Gson())
            }
            factory {
                BreathAdapter()
            }

            factory<IGraphConfiguration> {
                GraphConfiguration(context)
            }

            factory<IGraphHelper> {
                val resources = context.resources
                val xStep = resources.getDimension(R.dimen.graph_line_x_axis_step)
                val intervalGraphDraw = resources.getInteger(R.integer.interval_graph_draw)
                return@factory GraphHelper(intervalGraphDraw, xStep)
            }

            factory<IMediaPlayerManager> {
                val resources = context.resources
                val intervalGraphDraw = resources.getInteger(R.integer.interval_graph_draw)
                return@factory MediaPlayerManager(intervalGraphDraw.toLong())
            }


        }
    }

    private fun createHomePageModule(): Module {
        return module {
            viewModel { (date: String) -> ScreensViewModel(date, get(), get(), get()) }
        }
    }

    private fun createMainModule(): Module {
        return module {
            viewModel { MainViewModel(get()) }
        }
    }

}
