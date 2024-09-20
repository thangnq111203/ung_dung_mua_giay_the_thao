package com.fpoly.shoes_app.framework.presentation.ui.forgot.createNewPass

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentCreateNewPassBinding
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.newPass.NewPass
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.forgot.CustomDialogFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.toMD5
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CreateNewPassFragment : BaseFragment<FragmentCreateNewPassBinding, CreateNewPassViewModel>(
    FragmentCreateNewPassBinding::inflate, CreateNewPassViewModel::class.java
) {
//    private val navOptions =
//        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
//            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()
    private var userId = ""
    private val dialog = CustomDialogFragment()
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
        userId = sharedPreferences.getIdUser()
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.createPassResult.collect {

                    result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        dialog.dismiss()
                        val forgotMailResponse = result.data
                        if (forgotMailResponse?.success == true) {
                            val navController = findNavController()
                            fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            sharedPreferences.removeIdUser()
                            navController.navigate(
                                R.id.loginFragmentScreen,null, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                        StyleableToast.makeText(
                            requireContext(), forgotMailResponse?.message, R.style.fail
                        ).show()
                        binding.layoutInputpassWord.error = forgotMailResponse?.message
                        binding.layoutInputrePassWord.error = forgotMailResponse?.message

                    }

                    Status.ERROR -> {
                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), strNullOrEmpty(errorMessage), R.style.fail
                        ).show()
                        binding.layoutInputpassWord.error = strNullOrEmpty(errorMessage)
                        binding.layoutInputrePassWord.error = strNullOrEmpty(errorMessage)
                    }

                    Status.LOADING -> {
                        dialog.isCancelable = false
                        dialog.show(childFragmentManager, "CustomDialogFragment")
                    }

                    Status.INIT -> {
                    }
                }
                binding.btnNextPager.isEnabled= true
            }
        }
    }

    override fun setOnClick() {
        binding.btnNextPager.setOnClickListener {

            val newPass = binding.passWordEditText.text.toString().trim()
            val reNewPass = binding.rePassWordEditText.text.toString().trim()
            if (newPass == reNewPass && newPass.isNotEmpty()) {
                viewModel.newPass(NewPass(userId, newPass.toMD5()))
            }
        }
    }
}
