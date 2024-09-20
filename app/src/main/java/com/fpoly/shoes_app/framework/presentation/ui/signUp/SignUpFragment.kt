package com.fpoly.shoes_app.framework.presentation.ui.signUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSignUpBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.service.ServiceUtil.playNotificationSound
import com.fpoly.shoes_app.utility.toMD5
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>(
    FragmentSignUpBinding::inflate, SignUpViewModel::class.java
) {
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val signUpResponse = result.data
                        if (signUpResponse?.success == true) {
                            val bundle = Bundle().apply{
                                putString("id", signUpResponse.user?.id)
                                putString("email", signUpResponse.user?.nameAccount)
                            }
                            val navController = findNavController()
                            binding.userNameEditText.text?.clear()
                            binding.passwordEditText.text?.clear()
                            binding.rePasswordEditText.text?.clear()
                            playNotificationSound(requireContext(),"Đăng ký tài khoản thành công","")
                            navController.navigate(
                                R.id.setUpAccountFragment, bundle, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                        signUpResponse?.message?.let { errorMessage ->
                            StyleableToast.makeText(
                                requireContext(),
                                getString(R.string.accountAlreadyExists),
                                R.style.fail
                            ).show()
                            binding.userNameEditText.error = errorMessage

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

    override fun setOnClick() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            val password = binding.passwordEditText.text?.toString()?.trim()
            val rePassword = binding.rePasswordEditText.text?.toString()?.trim()

            if (!password.isNullOrEmpty() && !rePassword.isNullOrEmpty()) {
                if (password == rePassword) {
                    viewModel.signUp(binding.userNameEditText.text.toString().trim(), password.toMD5())
                } else {
                    StyleableToast.makeText(
                        requireContext(), getString(R.string.passwordIncorrect), R.style.fail
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT).show()
            }

        }
    }
}