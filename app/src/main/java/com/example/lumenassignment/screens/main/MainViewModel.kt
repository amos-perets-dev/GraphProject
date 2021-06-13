package com.example.lumenassignment.screens.main

import com.example.lumenassignment.lumen_app.base.ViewModelBase
import com.example.lumenassignment.manager.parse.IParseJsonManager

class MainViewModel(
    private val manager: IParseJsonManager
) :
    ViewModelBase() {

    fun initData() {
        manager.initData()?.let {
            compositeDisposable.add(
                it
            )
        }
    }
}