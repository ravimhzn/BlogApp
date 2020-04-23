package com.ravimhzn.blogapplication.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User
import com.ravimhzn.blogapplication.repository.Repository
import com.ravimhzn.blogapplication.ui.main.state.MainStateEvent
import com.ravimhzn.blogapplication.ui.main.state.MainStateEvent.*
import com.ravimhzn.blogapplication.ui.main.state.MainViewState
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
        var list = blogPosts.filter { blogPost ->
            filterInitialList(blogPost)
        }
        // it.rows?.filter { it?.let { it1 -> checkIfValuesNotNull(it1) }!! }
        update.blogPosts = list
        _viewState.value = update
    }

    /**
     * Since I have no control over api and I don't want that initial list.
     */
    private fun filterInitialList(it: BlogPost): Boolean {
        return it?.pk != 0
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