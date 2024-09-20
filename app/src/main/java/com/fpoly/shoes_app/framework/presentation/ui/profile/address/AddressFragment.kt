package com.fpoly.shoes_app.framework.presentation.ui.profile.address

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressBinding
import com.fpoly.shoes_app.framework.adapter.address.AddressAdapter
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.RequestKey
import com.fpoly.shoes_app.utility.ResultKey
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding, AddressViewModel>(
    FragmentAddressBinding::inflate, AddressViewModel::class.java
) {
    private lateinit var addressAdapter: AddressAdapter
    private var listAddressDetails: MutableList<Addresse>? = mutableListOf()
    private var check = false

    private val args: AddressFragmentArgs by navArgs()

    private fun setupRecyclerView() {
        addressAdapter = AddressAdapter(listAddressDetails,
            // Lambda function for handling item click
            { address ->
                val bundle = Bundle().apply {
                    putParcelable("address", address)
                }
                findNavController().navigate(R.id.addressDetailsFragment, bundle)
            },
            // Lambda function for handling edit click
            { address ->
                val bundle = Bundle().apply {
                    putParcelable("address", address)
                    putInt("check", 1)
                }
//                findNavController().navigate(R.id.editoraddFragment, bundle)
            })

        binding.recycViewAddress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressAdapter
        }

        // Setup ItemTouchHelper for swipe-to-delete functionality
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // dragDirs (not used here)
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // swipeDirs
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val address = listAddressDetails!![position]
                viewModel.deleteAddress(address.id)
                loadAgain()
                addressAdapter.deleteItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycViewAddress)
    }
    override fun setupPreViews() {
        loadAgain()
    }

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.address)
        }
        setupRecyclerView()
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadAgain()
        }
    }
    private fun loadAgain(){
        viewModel.fetchAllAddresses(sharedPreferences.getIdUser())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (check) {
                viewModel.deleteAddressResult.collect { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            result.data?.let { addressResponse ->
                                if (addressResponse.success) {
                                    Toast.makeText(
                                        requireContext(),
                                        addressResponse.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    StyleableToast.makeText(
                                        requireContext(), addressResponse.message, R.style.fail
                                    ).show()
                                }
                            }
                        }

                        Status.ERROR -> {
                            val errorMessage = strNullOrEmpty(result.message)
                            StyleableToast.makeText(
                                requireContext(), errorMessage, R.style.fail
                            ).show()
                        }

                        Status.LOADING -> Unit
                        Status.INIT -> Unit
                    }
                }
            }
            viewModel.allAddressResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        if (result.data?.addresses.isNullOrEmpty()){
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.textNoData.visibility = View.VISIBLE
                            return@collect
                        }
                        result.data?.let { addressResponse ->
                            if (addressResponse.success) {
                                binding.swipeRefreshLayout.isRefreshing = false
                                listAddressDetails?.clear()
                                addressResponse.addresses?.let { listAddressDetails?.addAll(it) }
                                addressAdapter.notifyDataSetChanged()
                            } else {
                                StyleableToast.makeText(
                                    requireContext(), addressResponse.message, R.style.fail
                                ).show()
                            }
                        }
                    }

                    Status.ERROR -> {
                        showProgressbar(false)
                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), errorMessage, R.style.fail
                        ).show()
                    }

                    Status.LOADING -> showProgressbar(true)
                    Status.INIT -> Unit
                }
            }
        }

    }

    override fun setOnClick() {
        binding.btnAddAddress.setOnClickListener {
            // reload address checkout if add address and from address checkout
            if (args.args) {
                val result = Bundle().apply {
                    putBoolean(ResultKey.RELOAD_ADDRESS_CHECKOUT_RESULT_KEY, true)
                }
                setFragmentResult(RequestKey.RELOAD_ADDRESS_CHECKOUT_REQUEST_KEY, result)
            }

            val bundle = Bundle().apply {
                putInt("check", 0)
            }
            findNavController().navigate(R.id.editoraddFragment, bundle)
        }

    }
}