package com.example.lumenassignment.manager.parse

import io.reactivex.disposables.Disposable

interface IParseJsonManager {

    /**
     * Init the data project
     */
    fun initData(): Disposable?
}