package com.ravimhzn.blogapplication.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.ravimhzn.blogapplication.R
import com.ravimhzn.blogapplication.ui.state.MainStateEvent
import com.ravimhzn.blogapplication.ui.viewmodels.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
        subscribeObservers()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun subscribeObservers() {
        viewModel.resultDataState.observe(viewLifecycleOwner, Observer { dataState ->

            dataState.data?.let { mainViewState ->
                mainViewState.blogPosts?.let {
                    viewModel.setBlogListData(it)
                }

                mainViewState.user?.let {
                    viewModel.setUser(it)
                }
            }

            //Handle Error
            dataState.message?.let {

            }

            //Handle Loading
            dataState.loading?.let {
                //show progress
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {
                println("debug -> Setting blogpost to RecyclerView:: $it")
            }

            viewState.user?.let {
                println("debug -> Setting User Data:: $it")//set User Data
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()

            R.id.action_get_blogs -> triggerGetBlogEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetUserEvent() {
        println("debug-> Clicked User Event")
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }

    private fun triggerGetBlogEvent() {
        println("debug-> Clicked Blog Event")
        viewModel.setStateEvent(MainStateEvent.GetBlogPostEvent())
    }

}
