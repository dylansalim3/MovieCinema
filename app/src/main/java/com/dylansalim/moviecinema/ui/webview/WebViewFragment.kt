package com.dylansalim.moviecinema.ui.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dylansalim.moviecinema.R
import com.dylansalim.moviecinema.dagger.App
import com.dylansalim.moviecinema.dagger.module.viewModule.ViewModelFactory
import com.dylansalim.moviecinema.databinding.DetailFragmentBinding
import com.dylansalim.moviecinema.databinding.FragmentWebViewBinding
import com.dylansalim.moviecinema.databinding.ListFragmentBinding
import com.dylansalim.moviecinema.ui.list.ListViewModel
import com.dylansalim.moviecinema.utils.WEBVIEW_URL
import javax.inject.Inject

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.appComponent.inject(this)

        binding = FragmentWebViewBinding.inflate(inflater)
        binding.webview.loadUrl(WEBVIEW_URL)

        binding.toolbarWebView.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}