package com.fpoly.shoes_app.framework.presentation.ui.setUpAccount

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentSetUpAccountBinding
import com.fpoly.shoes_app.framework.data.othetasks.AddImage
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Imagesss
import com.fpoly.shoes_app.utility.Status
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class SetUpAccountFragment : BaseFragment<FragmentSetUpAccountBinding, SetUpAccountViewModel>(
    FragmentSetUpAccountBinding::inflate, SetUpAccountViewModel::class.java
) {
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private val gender = arrayOf("Nữ", "Nam")
    private var uriPath: Uri? = null
    private var id: String = ""
    private var email: String = ""

    private fun showDatePickerDialog(dateEditText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, yearBirth, monthBirth, dayOfMonth ->
                val selectedDate = "$yearBirth-${monthBirth + 1}-$dayOfMonth"
                dateEditText.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    override fun setupPreViews() {
        captureImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.imageUri
                handleResult(uriPath)
            }
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriPath = AddImage.handleImageSelectionResult(result.data)
                handleResult(uriPath)
            }
        }
    }

    private fun handleResult(uri: Uri?) {
        uri?.let {
            AddImage.Glides(it, requireContext(), binding.imgAvatar)
        }
    }

    override fun setupViews() {
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
        id = arguments?.getString("id") ?: sharedPreferences.getIdUser()
    email= arguments?.getString("email") ?: sharedPreferences.getUserName()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.mailEditText.setText(email)
        addTextWatchers()
    }

    private fun addTextWatchers() {
        binding.nameEditText.addTextChangedListener(createTextWatcher())
        binding.phoneEditText.addTextChangedListener(createTextWatcher())
        binding.dateEditText.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            checkIfAnyFieldChanged()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun checkIfAnyFieldChanged() {
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val birth = binding.dateEditText.text.toString().trim()
        val gender = binding.spinner.selectedItem.toString()

        binding.btnNextPager.isEnabled = name.isNotEmpty() || phone.isNotEmpty() || email.isNotEmpty() || birth.isNotEmpty() || gender.isNotEmpty()
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setUpResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSetUpSuccess(result.data)
                    Status.ERROR -> handleError(result.message)
                    Status.LOADING -> showProgressbar(true)
                    Status.INIT -> Unit
                }
            }
        }
    }

    private fun handleSetUpSuccess(data: SetUpAccountResponse?) {
        showProgressbar(false)
        data?.let {
            StyleableToast.makeText(requireContext(), getString(R.string.success), R.style.success).show()
            findNavController().navigate(
                R.id.loginFragmentScreen,
                null,
                NavOptions.Builder().setPopUpTo(findNavController().currentDestination?.id ?: -1, true).build()
            )
        }
    }

    private fun handleError(errorMessage: String?) {
        showProgressbar(false)
        Log.e("SetUpAccountFragment", "Setup error: $errorMessage")
    }

    override fun setOnClick() {
        binding.relative.setOnClickListener {
            AddImage.openImageDialog(null, requireContext(), requireActivity()) { intent ->
                when (intent.action) {
                    MediaStore.ACTION_IMAGE_CAPTURE -> captureImageLauncher.launch(intent)
                    Intent.ACTION_PICK -> pickImageLauncher.launch(intent)
                }
            }
        }

        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText)
        }

        binding.toolbar.setNavigationOnClickListener {
            if (sharedPreferences.getIdUser().isEmpty()) {
                fragmentManager?.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                navController?.navigate(R.id.loginFragmentScreen, null, NavOptions.Builder().setPopUpTo(
                    navController?.currentDestination?.id ?: -1, true).build())
            } else {
                findNavController().popBackStack()
            }
        }

        binding.btnNextPager.setOnClickListener {
            validateAndSubmit()
        }

        setUpEditTextListeners()
    }

    private fun setUpEditTextListeners() {
        binding.mailEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField { CheckValidate.checkEmail(requireContext(), binding.mailEditText, binding.layoutInputMail, binding.layoutInputPhone) }
            } else false
        }

        binding.phoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField { CheckValidate.checkPhone(requireContext(), binding.phoneEditText, binding.layoutInputPhone, binding.btnNextPager) }
            } else false
        }

        binding.nameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField { CheckValidate.checkStr(requireContext(), binding.nameEditText, binding.layoutInputPhone, binding.layoutInputMail) }
            } else false
        }
    }

    private fun validateField(validation: () -> Boolean): Boolean {
        return if (validation()) {
            true
        } else {
            Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun validateAndSubmit() {
        val isPhoneValid = CheckValidate.checkPhone(
            requireContext(),
            binding.phoneEditText,
            binding.layoutInputPhone,
            binding.btnNextPager
        )

        val isEmailValid = CheckValidate.checkEmail(
            requireContext(),
            binding.mailEditText,
            binding.layoutInputMail,
            binding.layoutInputPhone
        )
        if (isPhoneValid && isEmailValid) {
            submitProfileChanges()
        } else {
            Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitProfileChanges() {
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val email = binding.mailEditText.text.toString().trim()
        val birth = binding.dateEditText.text.toString().trim()
        val gender = binding.spinner.selectedItem.toString()

        binding.btnNextPager.isEnabled = false
        viewModel.setUp(id, uriPath?.let { File(Imagesss.getPathFromUri(it, requireContext())) }, phone, name, email, birth, gender)
    }
}
