package com.example.lumenassignment.lumen_app

import android.content.Context
import com.example.lumenassignment.BuildConfig
import com.example.lumenassignment.R
import com.example.lumenassignment.manager.ParseJsonManager
import com.example.lumenassignment.manager.IParseJsonManager
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

class LumenModules {
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

            factory<IGraphHelper> {
                val resources = context.resources
                val xStep = resources.getDimension(R.dimen.graph_line_x_axis_step)
                return@factory GraphHelper(resources, xStep)
            }


        }
    }

    private fun createHomePageModule(): Module {
        return module {
            viewModel { (date: String) -> ScreensViewModel(date, get(), get()) }
        }
    }

    private fun createMainModule(): Module {
        return module {
            viewModel { MainViewModel(get()) }
        }
    }

}
