package com.ravimhzn.blogapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ravimhzn.blogapplication.ui.state.MainViewState
import com.ravimhzn.blogapplication.ui.state.MainViewStateEvent
import com.ravimhzn.blogapplication.ui.state.MainViewStateEvent.*
import com.ravimhzn.blogapplication.util.AbsentLiveData

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainViewStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()
    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<MainViewState> = Transformations.switchMap(_stateEvent) { stateEvent ->
        stateEvent?.let {
            handleStateEvent(stateEvent)
        }
    }

    private fun handleStateEvent(stateEvent: MainViewStateEvent): LiveData<MainViewState> {
        return when (stateEvent) {
            is GetBlogPostEvent -> {
                AbsentLiveData.create()
            }
            is GetUserEvent -> {
                AbsentLiveData.create()
            }
            is None -> {
                AbsentLiveData.create()
            }
        }
    }
}