package com.dylansalim.moviecinema.dagger.component

import com.dylansalim.moviecinema.MainActivity
import com.dylansalim.moviecinema.dagger.module.networkModule.NetworkModule
import com.dylansalim.moviecinema.dagger.module.viewModule.ViewModelModule
import com.dylansalim.moviecinema.ui.detail.DetailFragment
import com.dylansalim.moviecinema.ui.list.ListFragment
import com.dylansalim.moviecinema.ui.list.OrderBottomSheetDialogFragment
import com.dylansalim.moviecinema.ui.webview.WebViewFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(listFragment: ListFragment)
    fun inject(detailFragment: DetailFragment)
    fun inject(orderBottomSheetDialogFragment: OrderBottomSheetDialogFragment)
    fun inject(webViewFragment: WebViewFragment)
}