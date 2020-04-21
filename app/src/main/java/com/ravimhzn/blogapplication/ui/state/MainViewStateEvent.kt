package com.ravimhzn.blogapplication.ui.state

sealed class MainViewStateEvent {

    class GetBlogPostEvent : MainViewStateEvent()
    class GetUserEvent(val userId: String) : MainViewStateEvent()
    class None : MainViewStateEvent()
}