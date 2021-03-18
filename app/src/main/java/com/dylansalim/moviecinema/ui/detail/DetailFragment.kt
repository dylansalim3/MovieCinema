package com.dylansalim.moviecinema.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dylansalim.moviecinema.R
import com.dylansalim.moviecinema.dagger.App
import com.dylansalim.moviecinema.dagger.module.viewModule.ViewModelFactory
import com.dylansalim.moviecinema.databinding.DetailFragmentBinding
import com.dylansalim.moviecinema.utils.ViewStateEnum
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import kotlin.properties.Delegates

class DetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: DetailViewModel
    lateinit var binding: DetailFragmentBinding

    private var args by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.appComponent.inject(this)

        binding = DetailFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        args = DetailFragmentArgs.fromBundle(requireArguments()).id

        viewModel.getSelectedMovieById(args)

        binding.toolbarDetail.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnBookMovieDetail.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToWebviewfragment()
            )
        }

        // Observe the current view state
        viewModel.detailViewLoading.observe(viewLifecycleOwner, Observer {
            when (it?.currentState) {
                ViewStateEnum.FAILED.value -> onNetworkError()
                ViewStateEnum.SUCCESS.value -> hideProgressBar()
                ViewStateEnum.LOADING.value -> showProgressBar()
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun showProgressBar() {
        binding.progressBarDetail.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarDetail.visibility = View.GONE
    }

    private fun onNetworkError() {
        val errorSnackbar =
            Snackbar.make(binding.root, "Having some network issue", Snackbar.LENGTH_INDEFINITE)
        errorSnackbar.setAction(R.string.retry) { viewModel.getSelectedMovieById(args) }
        errorSnackbar.show()
    }


}
