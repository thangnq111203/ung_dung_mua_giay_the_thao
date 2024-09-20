package com.fpoly.shoes_app.framework.presentation.ui.forgot.forGotEmail

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentForGotBinding
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.forgotMail.ForgotMail
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForGotEmailFragment : BaseFragment<FragmentForGotBinding, ForGotEmailViewModel>(
    FragmentForGotBinding::inflate, ForGotEmailViewModel::class.java
) {
    override fun setupPreViews() {

    }
    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.forgotMailResult.collect {

                result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        val forgotMailResponse = result.data
                        val bundle = Bundle().apply {
                            putString("email",binding.emailEditText.text.toString())
                        }
                        if (forgotMailResponse?.success == true) {
                            val navController = findNavController()
                            navController.navigate(
                                R.id.OPTFragment,bundle, NavOptions.Builder().setPopUpTo(
                                    navController.currentDestination?.id ?: -1, true
                                ).build()
                            )
                            sharedPreferences.setIdUser(forgotMailResponse.userId)
                            StyleableToast.makeText(
                                requireContext(), getString(R.string.success), R.style.success
                            ).show()
                            return@collect
                        }

                    }

                    Status.ERROR -> {

                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), strNullOrEmpty(errorMessage), R.style.fail
                        ).show()
                        binding.emailEditText.error = strNullOrEmpty(errorMessage)
                    }

                    Status.LOADING -> {
                        showProgressbar(true)
                    }

                    Status.INIT -> {

                    }
                }
                binding.btnNextPager.isEnabled= true
            }
        }

    }

    override fun setOnClick() {
        binding.apply {
            btnNextPager.setOnClickListener {
                if (!binding.emailEditText.text.toString().trim().isNullOrEmpty()){
                    binding.btnNextPager.isEnabled= false
                    viewModel.forgotMail(ForgotMail( binding.emailEditText.text.toString().trim()))}else
                    Toast.makeText(requireContext(),getString(R.string.inputFullInfo),Toast.LENGTH_SHORT).show()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

    }
}