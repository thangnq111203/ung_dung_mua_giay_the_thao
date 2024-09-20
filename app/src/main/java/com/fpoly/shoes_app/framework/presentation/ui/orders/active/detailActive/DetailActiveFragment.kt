package com.fpoly.shoes_app.framework.presentation.ui.orders.active.detailActive

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentDetailActiveBinding
import com.fpoly.shoes_app.databinding.LayoutBottomCancleBinding
import com.fpoly.shoes_app.databinding.LayoutDialogRateBinding
import com.fpoly.shoes_app.framework.adapter.order.detail.DetailActiveAdapter
import com.fpoly.shoes_app.framework.adapter.order.detail.DetailHistoryAdapter
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRate
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.formatToVND
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActiveFragment: BaseFragment<FragmentDetailActiveBinding, DetailActiveViewModel>(
    FragmentDetailActiveBinding::inflate,
    DetailActiveViewModel::class.java
) {
    private  var detailHistoryAdapter: DetailHistoryAdapter?=null
    private  var detailActiveAdapter: DetailActiveAdapter?=null
    private  var  historyShoe:HistoryShoe ?=null
    override fun setupPreViews() {

    }

    override fun setupViews() {
         historyShoe = arguments?.getParcelable<HistoryShoe>("history")
        detailHistoryAdapter = DetailHistoryAdapter(
            orderDetailShoes = historyShoe!!.orderDetails
        )
        detailActiveAdapter = DetailActiveAdapter(
            orderActiveDetailShoes = historyShoe!!.orderStatusDetails
        )
        binding.apply {
            nameUser.text = historyShoe!!.orderDetails?.get(0)?.name ?: "Oder"
            numberPhoneUser.text = historyShoe!!.phoneNumber
            nameAddress.text = historyShoe!!.addressOrder
            headerLayout.tvTitle.text = getString(R.string.detailHistory)
            recycProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailHistoryAdapter
            }
            recycProcedure.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailActiveAdapter
            }
            val allViews = listOf(boxComplete, view1, transitionComplete, view2, ShiperComplete, view3, Success)
            val numberOfViewsToTint = when (historyShoe!!.orderStatusDetails?.size) {

                1,2 -> 1
                3 -> 3
                4 -> 5
                else -> allViews.size
            }
            if (historyShoe!!.orderStatusDetails?.size==5||historyShoe!!.orderStatusDetails?.size==1){
                comfirmTake.visibility = View.VISIBLE
                if (historyShoe!!.orderStatusDetails?.size==1){
                    comfirmTake.text = getString(R.string.cancel)
                }
                }
            val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.black)
            allViews.take(numberOfViewsToTint).forEach { view ->
                view.backgroundTintList = colorStateList
            }

        }


    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.confirmTakeResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val signUpResponse = result.data
                        if (signUpResponse?.order?.status ==0) {
                            findNavController().popBackStack()
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("errorMessage",errorMessage)
                        showProgressbar(false)
                    }

                    Status.LOADING -> {
                        showProgressbar(true)
                    }

                    Status.INIT -> {
                    }
                }
            }
        }
 viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cancleResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val signUpResponse = result.data
                        if (signUpResponse?.order?.status ==6) {
                            findNavController().popBackStack()
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("errorMessage",errorMessage)
                        showProgressbar(false)
                    }

                    Status.LOADING -> {
                        showProgressbar(true)
                    }

                    Status.INIT -> {
                    }
                }
            }
        }

    }
    @SuppressLint("SetTextI18n")
    private fun showBottomSheetDialog(id: String) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding: LayoutBottomCancleBinding = LayoutBottomCancleBinding.inflate(layoutInflater)

        dialogBinding.bottomSheetCancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        dialogBinding.bottomSheetOkButton.setOnClickListener {

            viewModel.cancleDetailVM(id, dialogBinding.becauseCancel.text.toString())
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }
    override fun setOnClick() {
        binding.apply {
            headerLayout.imgBack.setOnClickListener {
                navController?.popBackStack()
            }
            comfirmTake.setOnClickListener {
                if (historyShoe!!.orderStatusDetails?.size==1){
                    historyShoe!!._id?.let { it1 -> showBottomSheetDialog(it1) }
                }

                    historyShoe?._id?.let { it1 -> viewModel.confirmTakeDetailVM(it1) }
            }
        }


    }

}