package com.fpoly.shoes_app.framework.presentation.ui.forgot.otpConfirm

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentOtpBinding
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.forgot.forGotEmail.ForGotEmailViewModel
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OPTConfirmFragment : BaseFragment<FragmentOtpBinding, OTPConfirmViewModel>(
    FragmentOtpBinding::inflate, OTPConfirmViewModel::class.java
) {
    private val forGotEmailViewModel: ForGotEmailViewModel by activityViewModels()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var email: String

    private fun startCountdownTimer() {
        binding.countdownTimerTextView.isEnabled = false
        binding.btnSelect.isEnabled = true
        binding.let { safeBinding ->
            val countdownDuration = 120000
            countDownTimer = object : CountDownTimer(countdownDuration.toLong(), 1000) {
                @SuppressLint("DefaultLocale")
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = String.format(
                        "%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60
                    )
                    safeBinding.countdownTimerTextView.text = timeLeft
                }

                @SuppressLint("SetTextI18n")
                override fun onFinish() {
                    safeBinding.countdownTimerTextView.text = "00:00"
                    safeBinding.countdownTimerTextView.text = getString(R.string.resend_code_otp)
                    safeBinding.btnSelect.isEnabled = false
                    safeBinding.countdownTimerTextView.isEnabled = true
                    countDownTimer?.cancel()
                }
            }
            countDownTimer?.start()
        }
    }


    override fun setupPreViews() {
        email = arguments?.getString("email").toString()

    }
    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
        startCountdownTimer()
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.otpConfirmResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val otpConfirmResponse = result.data
                        if (otpConfirmResponse?.success == true) {
                            val navController = findNavController()
                            fragmentManager?.popBackStackImmediate(R.id.loginFragmentScreen, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            navController.navigate(
                                R.id.createNewPassFragment, null, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }
                        StyleableToast.makeText(
                            requireContext(), strNullOrEmpty(otpConfirmResponse?.message), R.style.fail
                        ).show()
                        binding.edtOPT.error = strNullOrEmpty(otpConfirmResponse?.message)
                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("LoginFragment", "Login error: $errorMessage")
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
            forGotEmailViewModel.forgotMailResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        startCountdownTimer()
                    }

                    Status.ERROR -> {
                        val errorMessage = result.message ?: "Unknown error"
                        Log.e("OPTConfirmFragment", "Forgot Mail error: $errorMessage")
                    }

                    Status.LOADING -> {
                        // Show loading if needed
                    }

                    Status.INIT -> {
                        // Handle initial state if needed
                    }
                }
            }
        }
    }

    override fun setOnClick() {
        binding.apply {
            btnSelect.setOnClickListener {
                if (binding.edtOPT.text.toString().trim().isNotEmpty())
                    viewModel.otpConfirm(email, binding.edtOPT.text.toString().trim())
                else
                    Toast.makeText(requireContext(),getString(R.string.pleaseOTP), Toast.LENGTH_SHORT).show()
            }
            countdownTimerTextView.setOnClickListener {
                forGotEmailViewModel.forgotMail(ForgotMail( email))
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

    }
}
