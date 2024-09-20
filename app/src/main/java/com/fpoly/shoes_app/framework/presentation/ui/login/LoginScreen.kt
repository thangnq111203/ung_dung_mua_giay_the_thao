package com.fpoly.shoes_app.framework.presentation.ui.login

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentLoginScreenBinding
import com.fpoly.shoes_app.framework.domain.model.login.LoginResponse
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.Status
import com.fpoly.shoes_app.utility.service.ServiceUtil.playNotificationSound
import com.fpoly.shoes_app.utility.toMD5
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginScreen : BaseFragment<FragmentLoginScreenBinding, LoginViewModel>(
    FragmentLoginScreenBinding::inflate, LoginViewModel::class.java
) {
    private var check = false
    private var username: String = ""
    private var password: String = ""
    private var tokenFCM: String = ""
    private val navOptions =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left).setPopExitAnim(R.anim.slide_out_right).build()

    private val navOptions1 =
        NavOptions.Builder().setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
            .setPopEnterAnim(R.anim.slide_in_right).setPopExitAnim(R.anim.slide_out_left).build()

    override fun setupPreViews() {

    }

    private fun setupListeners() {
        with(sharedPreferences) {
            val userName = getUserName()
            val passWord = getPassWord()
             tokenFCM = getToken()
            if (userName.isNotEmpty() && passWord.isNotEmpty()) {
                check = true
                viewModel.signIn(userName, passWord,tokenFCM)
            }
            val userNameWait = getUserNameWait()
            if (userNameWait.isNotEmpty()) {
                binding.userNameEditTextLogin.setText(userNameWait)
            }
        }
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSuccess(result.data)
                    Status.ERROR -> handleError(result.message)
                    Status.LOADING -> handleLoading()
                    Status.INIT -> binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun handleSuccess(loginResponse: LoginResponse?) {
        enableInputs()
        binding.progressBar.visibility = View.GONE
        if (loginResponse?.success == true) {
            playNotificationSound(requireContext(), "Shoe_Fbee", "Login Success")
            Log.e("token", SharedPreferencesManager.getToken())
            loginResponse.user?.let { sharePre(it.id.toString()) }
            navigateToHome()
            clearInputs()
            StyleableToast.makeText(
                requireContext(), getString(R.string.success), R.style.success
            ).show()
        } else {
            showErrorMessage(loginResponse?.message)
        }
    }

    private suspend fun handleError(errorMessage: String?) {
        binding.progressBar.visibility = View.GONE
        showErrorMessage(errorMessage)
        enableInputs()
        Log.e("LoginFragment", "Login error: $errorMessage")
    }

    private fun handleLoading() {
        disableInputs()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun sharePre(id: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            sharedPreferences.setIdUser(id)
            if (check) {
                sharedPreferences.setPassWord(username, password.toMD5())
            } else {
                sharedPreferences.setPassWord(username, null)
            }
        }
    }

    private fun enableInputs() {
        binding.apply {
            layoutInputUserNameLogin.isEnabled = true
            layoutInputPasswordLogin.isEnabled = true
            switchLogin.isEnabled = true
            textForGot.isEnabled = true
            textSignUp.isEnabled = true
            btnLogin.isEnabled = true
        }
    }

    private fun disableInputs() {
        binding.apply {
            userNameEditTextLogin.isEnabled = false
            switchLogin.isEnabled = false
            textForGot.isEnabled = false
            textSignUp.isEnabled = false
            btnLogin.isEnabled = false
            passwordEditTextLogin.isEnabled = false
        }
    }

    private fun navigateToHome() {
        val navController = findNavController()
        val navOptions = NavOptions.Builder().setPopUpTo(
            navController.currentDestination?.id ?: -1, true
        ).build()
        navController.navigate(R.id.homeFragment, null, navOptions)
    }

    private fun clearInputs() {
        binding.userNameEditTextLogin.text?.clear()
        binding.passwordEditTextLogin.text?.clear()
    }

    private suspend fun showErrorMessage(errorMessage: String?) {
        StyleableToast.makeText(
            requireContext(), getString(R.string.fail_password), R.style.fail
        ).show()
        binding.apply {
            layoutInputUserNameLogin.error = errorMessage
            layoutInputPasswordLogin.error = errorMessage
        }
        delay(2000)
        binding.apply {
            layoutInputUserNameLogin.error = null
            layoutInputPasswordLogin.error = null
        }
    }

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
        setupListeners()
    }

    override fun setOnClick() {
        binding.apply {
            textSignUp.setOnClickListener {
                findNavController().navigate(R.id.signUpFragment, null, navOptions)
            }
            btnLogin.setOnClickListener {
                username = userNameEditTextLogin.text?.trim().toString()
                password = passwordEditTextLogin.text?.trim().toString()
                if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) viewModel.signIn(
                    username,
                    password.toMD5(),
                    tokenFCM
                ) else Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT)
                    .show()
            }
            textForGot.setOnClickListener {
                findNavController().navigate(R.id.forGotFragment, null, navOptions1)
            }
            switchLogin.setOnCheckedChangeListener { _, isChecked ->
                check = isChecked
                Log.e("check", isChecked.toString())
            }
        }
    }
}
