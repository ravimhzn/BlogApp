package com.ravimhzn.blogapplication.ui.main

import com.ravimhzn.blogapplication.util.Result

interface DataStateListener {

    fun onDataStateChange(dataState: Result<*>?)
}