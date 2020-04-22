package com.ravimhzn.blogapplication.ui.main.state

import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User

data class MainViewState(
    var blogPosts: List<BlogPost>? = null,
    var user: User? = null
)