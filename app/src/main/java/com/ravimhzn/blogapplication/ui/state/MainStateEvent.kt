package com.ravimhzn.blogapplication.ui.state

sealed class MainStateEvent {

    class GetBlogPostEvent : MainStateEvent()

    class GetUserEvent(val userId: String) : MainStateEvent()

    class None : MainStateEvent()
}