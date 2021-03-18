package com.dylansalim.moviecinema.dagger.module.viewModule


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dylansalim.moviecinema.ui.detail.DetailViewModel
import com.dylansalim.moviecinema.ui.list.ListViewModel
import com.dylansalim.moviecinema.ui.list.OrderBottomSheetViewModel
import com.dylansalim.moviecinema.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun bindDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    internal abstract fun bindListViewModel(viewModel: ListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(OrderBottomSheetViewModel::class)
    internal abstract fun bindOrderBottomSheetViewModel(viewModel: OrderBottomSheetViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}