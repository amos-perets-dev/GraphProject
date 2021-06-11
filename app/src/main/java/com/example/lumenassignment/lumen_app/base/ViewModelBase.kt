package com.example.lumenassignment.lumen_app.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class ViewModelBase :
    ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}