package com.dylansalim.moviecinema.ui.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.dylansalim.moviecinema.dagger.App
import com.dylansalim.moviecinema.dagger.module.viewModule.ViewModelFactory
import com.dylansalim.moviecinema.databinding.OrderBottomSheetModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class OrderBottomSheetDialogFragment(
    private val options: List<String>,
    private val selectDialogListener: SelectDialogListener,
    private val orderIndex: Int
) : BottomSheetDialogFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: OrderBottomSheetViewModel
    private lateinit var binding: OrderBottomSheetModalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        App.appComponent.inject(this)

        binding = OrderBottomSheetModalBinding.inflate(inflater)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(OrderBottomSheetViewModel::class.java)

        binding.listViewOrderOptions.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_single_choice,
            options
        )

        // Set Selected item
        binding.listViewOrderOptions.setItemChecked(orderIndex, true)
        viewModel.selectOrder.value?.let { binding.listViewOrderOptions.setItemChecked(it, true) }

        binding.listViewOrderOptions.setOnItemClickListener { adapter, view, position, id ->
            viewModel.setSelectOrder(position)
            selectDialogListener.onOrderSelected(position)
            dismiss()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


}

interface SelectDialogListener {
    fun onOrderSelected(orderIndex: Int)
}