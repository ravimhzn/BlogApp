package com.ravimhzn.blogapplication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User
import com.ravimhzn.blogapplication.repository.Repository
import com.ravimhzn.blogapplication.ui.state.MainStateEvent
import com.ravimhzn.blogapplication.ui.state.MainStateEvent.*
import com.ravimhzn.blogapplication.ui.state.MainViewState
import com.ravimhzn.blogapplication.util.AbsentLiveData
import com.ravimhzn.blogapplication.util.Result

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState


    val resultDataState: LiveData<Result<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: MainStateEvent): LiveData<Result<MainViewState>> {
        println("DEBUG: New StateEvent detected: $stateEvent")
        return when (stateEvent) {

            is GetBlogPostEvent -> {
                Repository.getBlogPosts()
            }

            is GetUserEvent -> {
                Repository.getUser(stateEvent.userId)
            }

            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew() //initialize
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value?.let {
            it
        } ?: MainViewState()
    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }
}