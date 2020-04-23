package com.ravimhzn.blogapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ravimhzn.blogapplication.R
import com.ravimhzn.blogapplication.model.BlogPost
import com.ravimhzn.blogapplication.model.User
import com.ravimhzn.blogapplication.ui.main.state.MainStateEvent
import com.ravimhzn.blogapplication.ui.main.viewmodels.MainViewModel
import com.ravimhzn.blogapplication.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(), BlogListRecyclerAdapter.Interaction {

    lateinit var viewModel: MainViewModel

    lateinit var dataStateListener: DataStateListener

    lateinit var blogListRecyclerAdapter: BlogListRecyclerAdapter

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("Debug -> RecyclerView Clicked -> Position :: $position")
        println("Debug -> RecyclerView Clicked -> ITEM :: $item")
    }

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
        initRecyclerView()
        subscribeObservers()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            blogListRecyclerAdapter = BlogListRecyclerAdapter(this@MainFragment)
            adapter = blogListRecyclerAdapter
        }
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun subscribeObservers() {
        viewModel.resultDataState.observe(viewLifecycleOwner, Observer { dataState ->

            dataStateListener.onDataStateChange(dataState)

            dataState.data?.let { event ->

                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.blogPosts?.let {
                        viewModel.setBlogListData(it)
                    }

                    mainViewState.user?.let {
                        viewModel.setUser(it)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {
                println("debug -> Setting blogpost to RecyclerView:: $it")
                blogListRecyclerAdapter.submitList(it)
            }

            viewState.user?.let {
                setUserProperties(it)
                println("debug -> Setting User Data:: $it")//set User Data
            }
        })
    }

    private fun setUserProperties(user: User) {
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(it.context)
                .load(user.image)
                .into(image)
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateListener = context as DataStateListener
        } catch (e: ClassCastException) {
            println("Debug -> $context must implement DataStateListener")
        }
    }
}
