package com.fpoly.shoes_app.framework.presentation.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentProfileBinding
import com.fpoly.shoes_app.databinding.LayoutDialogBinding
import com.fpoly.shoes_app.framework.data.othetasks.AddImage
import com.fpoly.shoes_app.framework.data.othetasks.AddImage.Glides
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.setUpAccount.SetUpAccountViewModel
import com.fpoly.shoes_app.utility.Imagesss
import com.fpoly.shoes_app.utility.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Suppress("UNREACHABLE_CODE")
@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, SetUpAccountViewModel>(
    FragmentProfileBinding::inflate, SetUpAccountViewModel::class.java
) {
    private var uriPath: Uri? = null
    private var imageShow: String? = null
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var idUser: String
    private var bmp: Bitmap? = null

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding: LayoutDialogBinding = LayoutDialogBinding.inflate(layoutInflater)
        dialogBinding.bottomSheetCancelButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        dialogBinding.bottomSheetOkButton.setOnClickListener {
            sharedPreferences.setUserWait()
            sharedPreferences.removeUser()
            sharedPreferences.removeIdUser()
            childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            navController?.navigate(
                ProfileFragmentDirections.actionProfileFragmentToLoginFragmentScreen()
            )
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    override fun setupPreViews() {
        captureImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.imageUri
                handleResult(uriPath)
            }
        }

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.handleImageSelectionResult(result.data)
                handleResult(uriPath)
            }
        }
    }

    override fun setupViews() {
        (requireActivity() as MainActivity).showBottomNavigation(true)
        idUser = sharedPreferences.getIdUser()
        viewModel.profilefind(idUser)
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.profile)
            headerLayout.imgBack.isVisible = false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val signUpResponse = result.data
                        if (signUpResponse?.success == true) {
                            imageShow = signUpResponse.user?.imageAccount?.`$binary`?.base64
                        }

                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("error", errorMessage)

                    }

                    Status.LOADING -> {}

                    Status.INIT -> {}
                }
            }
            return@launch
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.findProfileResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSuccess(result.data)
                    Status.ERROR -> handleError(result.message)
                    Status.LOADING -> handleLoading()
                    Status.INIT -> {}
                }
            }

        }
    }

    private fun handleResult(uri: Uri?) {
        val context = requireContext()
        val bitmap = uri?.let { AddImage.rotateImageIfRequired(context, it) }
        bitmap?.let {
            Glides(it, context, binding.idAvatar)
        }
        val imagePath = uri?.let { File(Imagesss.getPathFromUri(it, context)) }
        viewModel.setUp(idUser, imagePath, null, null, null, null, null)
    }

    private fun handleSuccess(profileResponse: ProfileResponse?) {
        Log.e("profileResponse", profileResponse.toString())
        profileResponse?.user?.let { user ->
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.hideShimmer()
            val dataImage = user.imageAccount?.`$binary`?.base64.toString()
            imageShow = dataImage
            val decodeDataImg = Base64.decode(dataImage, Base64.DEFAULT)
            bmp = BitmapFactory.decodeByteArray(decodeDataImg, 0, decodeDataImg.size)
            bmp?.let { Glides(it, requireContext(), binding.idAvatar) }
            binding.nameProfile.text = user.fullName ?: getString(R.string.name)
            binding.phoneProfile.text = user.phoneNumber ?: getString(R.string.phone_suggest)
        }
    }

    private fun handleError(errorMessage: String?) {
        Log.e("ProfileFragment", "Profile error: $errorMessage")
    }

    private fun handleLoading() {
        binding.let {
            CoroutineScope(Dispatchers.Main).launch {
                it.shimmerLayout.startShimmer()
            }
        }
    }

    override fun setOnClick() {
        binding.apply {
            constraintLogout.setOnClickListener {
                showBottomSheetDialog()
            }
            constraintEdt.setOnClickListener {
                navController?.navigate(
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                )
            }
            constraintAddess.setOnClickListener {
                navController?.navigate(
                    ProfileFragmentDirections.actionProfileFragmentToAddressFragment(false)
                )
            }
            constraintNotification.setOnClickListener {
                navController?.navigate(
                    ProfileFragmentDirections.actionProfileFragmentToGeneralSettingFragment()
                )
            }
            constraintHelpCenter.setOnClickListener {
                navController?.navigate(
                    ProfileFragmentDirections.actionProfileFragmentToHelpCenterFragment()
                )
            }
            relative.setOnClickListener {
                AddImage.openImageDialog(imageShow, requireContext(), requireActivity()) { intent ->
                    when (intent.action) {
                        MediaStore.ACTION_IMAGE_CAPTURE -> captureImageLauncher.launch(intent)
                        Intent.ACTION_PICK -> pickImageLauncher.launch(intent)
                    }
                }
            }
        }
    }
}
