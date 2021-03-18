package com.dylansalim.moviecinema.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dylansalim.moviecinema.R
import com.dylansalim.moviecinema.adapters.MovieListAdapter
import com.dylansalim.moviecinema.dagger.App
import com.dylansalim.moviecinema.dagger.module.viewModule.ViewModelFactory
import com.dylansalim.moviecinema.databinding.ListFragmentBinding
import com.dylansalim.moviecinema.utils.ViewStateEnum
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class ListFragment : Fragment(), SelectDialogListener {

    @Inject
    lateinit var listViewModelFactory: ViewModelFactory
    private lateinit var viewModel: ListViewModel
    private lateinit var binding: ListFragmentBinding

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: MovieListAdapter

    private var currentPage = 1
    private var maxPage = 1
    private var errorSnackbar: Snackbar? = null
    private var orderIndex: Int = 2
    private var fetchInitialData: Boolean = true

    private val TAG = "LF"

    private val options = listOf<String>(
        "Popularity (Lowest)",
        "Popularity (Highest)",
        "Release Date (Oldest)",
        "Release Date (Newest)",
        "Alphabetical (Ascending)",
        "Alphabetical (Descending)",
        "Rating (Lowest)",
        "Rating (Highest)"
    )
    private val optionsText = listOf<String>(
        "popularity.asc",
        "popularity.desc",
        "release_date.asc",
        "release_date.desc",
        "original_title.asc",
        "original_title.desc",
        "vote_average.asc",
        "vote_average.desc"
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.appComponent.inject(this)
        binding = ListFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, listViewModelFactory).get(ListViewModel::class.java)

        setupMovieListRecyclerView()



        setupSwipeRefreshListener()

        // Observe the current view state
        viewModel.listViewLoading.observe(viewLifecycleOwner, Observer {
            when (it?.currentState) {
                ViewStateEnum.FAILED.value -> onNetworkError()
                ViewStateEnum.SUCCESS.value -> hideProgressBar()
                ViewStateEnum.LOADING.value -> showProgressBar()
            }
        })

        binding.lifecycleOwner = this

        setupToolbar()
        return binding.root
    }

    private fun restoreRecyclerView() {
        adapter.clearMovies()
        adapter.appendMovies(viewModel.movie.value.orEmpty())
        attachPopularMoviesOnScrollListener()
    }

    private fun setupSwipeRefreshListener() {
        binding.swipeToRefreshList.setOnRefreshListener {
            refreshMovie()
            binding.swipeToRefreshList.isRefreshing = false
        }
    }

    private fun showProgressBar() {
        binding.progressBarList.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarList.visibility = View.GONE
    }

    private fun setupToolbar() {
        binding.toolbarList.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_order -> {
                    showOrderDialog()
                }
            }
            true
        }
    }

    private fun showOrderDialog() {
        OrderBottomSheetDialogFragment(options, this, orderIndex).show(childFragmentManager, TAG)
    }


    private fun onNetworkError() {
        errorSnackbar =
            Snackbar.make(binding.root, "Having some network issue", Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry) { viewModel.errorClickListener() }
        errorSnackbar?.show()
    }

    // Append Movie List to Recycler View
    private fun sendNewMovieList(sortBy: String? = null) {
        viewModel.getMovieList(currentPage, sortBy)
        attachPopularMoviesOnScrollListener()
    }

    // Register scroll listener that will listen whether the view reaches the end of the page,
    // if so, get movies from next page
    private fun attachPopularMoviesOnScrollListener() {
        binding.recyclerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                onLoadMore()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx == 0 && dy == 0) {
                    onLoadMore()
                }
            }
        })


    }

    // Getting movies from next page
    private fun onLoadMore() {
        val totalItemCount = layoutManager.itemCount
        val visibleItemCount = layoutManager.childCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (firstVisibleItem + visibleItemCount >= totalItemCount && currentPage <= maxPage) {
            currentPage++
            sendNewMovieList(optionsText[orderIndex])
        }
    }

    // Setup Recycler View
    private fun setupMovieListRecyclerView() {
        binding.recyclerList.adapter = MovieListAdapter(MovieListAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        }, mutableListOf())

        adapter = binding.recyclerList.adapter as MovieListAdapter

        layoutManager =
            GridLayoutManager(binding.root.context, 2, GridLayoutManager.VERTICAL, false)

        binding.recyclerList.layoutManager = layoutManager

        // Append Movie to RecyclerView if the movieResultViewModel changes
        viewModel.movieResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                adapter.appendMovies(it.networkMovie)
                maxPage = it.total_pages
            }
        })

        // Navigate to Movie Detail Screen
        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToDetailFragment(it.id)
                )
            }
        })


        if (fetchInitialData) {
            sendNewMovieList(optionsText[orderIndex])
        } else {
            restoreRecyclerView()
            // Restore recyclerview position when it is not empty
            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    // Disable fetching of new data if the list fragment is not destroy,
    override fun onPause() {
        super.onPause()
        fetchInitialData = false
    }

    // Trigger when user select one of the order criteria in the bottom navigation dialog
    override fun onOrderSelected(orderIndex: Int) {
        this.orderIndex = orderIndex
        refreshMovie()
    }

    // Clear the adapter and fetch the movies
    private fun refreshMovie() {
        adapter.clearMovies()
        viewModel.clearMovieList()
        currentPage = 1
        sendNewMovieList(optionsText[orderIndex])
    }
}
