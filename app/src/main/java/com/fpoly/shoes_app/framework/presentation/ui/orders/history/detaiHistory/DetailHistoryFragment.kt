package com.fpoly.shoes_app.framework.presentation.ui.orders.history.detaiHistory

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentDetailHistoryBinding
import com.fpoly.shoes_app.databinding.LayoutDialogRateBinding
import com.fpoly.shoes_app.framework.adapter.order.detail.DetailHistoryAdapter
import com.fpoly.shoes_app.framework.domain.model.history.HistoryShoe
import com.fpoly.shoes_app.framework.domain.model.updateRate.UpdateRate
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.formatToVND
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailHistoryFragment : BaseFragment<FragmentDetailHistoryBinding, DetailHistoryViewModel>(
    FragmentDetailHistoryBinding::inflate,
    DetailHistoryViewModel::class.java
) {
    private lateinit var detailHistoryAdapter: DetailHistoryAdapter
    private lateinit var idUser: String
    override fun setupPreViews() {

    }

    @SuppressLint("SetTextI18n")
    override fun setupViews() {
        idUser = sharedPreferences.getIdUser()
        val historyShoe = arguments?.getParcelable<HistoryShoe>("history")

        val barcodeData = historyShoe?._id // Dữ liệu bạn muốn mã hóa
            val width = 1000
            val height = 300

            try {
                val bitmap = generateBarcode(barcodeData, width, height)
                binding.barcodeImageView.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        detailHistoryAdapter = DetailHistoryAdapter(
            orderDetailShoes = historyShoe!!.orderDetails
        )

        binding.apply {
            if (historyShoe.status == "completed") {
                rate.setOnClickListener {
                    showBottomSheetDialog(historyShoe)
                }
            } else {
                rate.text = getString(R.string.orderAgain)
                rate.setOnClickListener {

                }
            }
            headerLayout.tvTitle.text = getString(R.string.detailHistory)
            nameUser.text = historyShoe.orderDetails?.get(0)?.name ?: "Oder"
            numberPhoneUser.text = historyShoe.phoneNumber
            nameAddress.text = historyShoe.addressOrder
            priceDemoOriginal.text = historyShoe.totalPre.toString().formatToVND()
            priceDemoSell.text = historyShoe.promo.toString().formatToVND()
            totalDemo.text = historyShoe.total.toString().formatToVND()
            methodDemo.text = historyShoe.pay
            dateDemo.text = historyShoe.dateOrder
            statusDemo.text = historyShoe.status
            recycComplete.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = detailHistoryAdapter
            }
        }
        }
        @Throws(WriterException::class)
        private fun generateBarcode(text: String?, width: Int, height: Int): Bitmap {
            val writer = Code128Writer()
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.CODE_128, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            return bitmap
        }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rateResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val signUpResponse = result.data
                        if (signUpResponse?.message?.isNotEmpty() == true && signUpResponse.updatedShoes?.isNotEmpty() == true) {
                            findNavController().popBackStack()
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }
                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
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
    private fun showBottomSheetDialog(historyShoe: HistoryShoe) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding: LayoutDialogRateBinding = LayoutDialogRateBinding.inflate(layoutInflater)
        val firstShoe = historyShoe.orderDetails?.get(0)
        Glide.with(requireContext()).load(historyShoe.thumbnail)
            .placeholder(R.drawable.download) // Placeholder image
            .error(R.drawable.download) // Error image
            .into(dialogBinding.imageShoeItem)
        dialogBinding.nameShoeItem.text = historyShoe.nameOrder
        dialogBinding.contentShoeItem.text = getString(R.string.size) + firstShoe?.size.toString()
        dialogBinding.colorShoeItem.text = firstShoe?.textColor
        dialogBinding.colorShoeItemView.backgroundTintList = ColorStateList.valueOf(
            Color.parseColor(
                firstShoe?.codeColor
            )
        )
        dialogBinding.priceShoeItem.text = historyShoe.total.toString().formatToVND()
        dialogBinding.bottomSheetCancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        dialogBinding.bottomSheetOkButton.setOnClickListener {
            Toast.makeText(
                requireContext(), dialogBinding.ratingBar.rating.toString(), Toast.LENGTH_SHORT
            ).show()
            viewModel.rate(
                UpdateRate(
                    commentText = dialogBinding.commentRate.text.toString(),
                    rateNumber = dialogBinding.ratingBar.rating.toInt(),
//                    shoeId = "66b8dccae5663d624f47f105" ,
                    shoeId = historyShoe.orderDetails?.map { it.shoeId.toString() } ?: emptyList() ,
                    oderId = historyShoe._id,
                    userId = idUser

                )
            )
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }
    }

}