package com.fpoly.shoes_app.framework.presentation.ui.profile.editProfile

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentEditProfileBinding
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate
import com.fpoly.shoes_app.framework.domain.model.profile.ProfileResponse
import com.fpoly.shoes_app.framework.domain.model.setUp.SetUpAccountResponse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.framework.presentation.ui.setUpAccount.SetUpAccountViewModel
import com.fpoly.shoes_app.utility.Status
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, SetUpAccountViewModel>(
    FragmentEditProfileBinding::inflate, SetUpAccountViewModel::class.java
) {
    private val gender = arrayOf("Nữ", "Nam")
    private var id = ""
    private var genderSelection = 1
    private var originalFullName: String? = null
    private var originalPhoneNumber: String? = null
    private var originalGmail: String? = null
    private var originalBirthDay: String? = null
    private var originalGender: Int = 0

    private fun showDatePickerDialog(dateEditText: EditText, layoutData: TextInputLayout) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, yearBirth, monthBirth, dayOfMonth ->
                val selectedDate = "$yearBirth-${monthBirth + 1}-$dayOfMonth"
                dateEditText.setText(selectedDate)
                layoutData.requestFocus()
            }, year, month, day)

        datePickerDialog.show()
    }

    override fun setupPreViews() {
        // Any additional setup before views are initialized can go here
    }

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.edit_info)
        }
        id = sharedPreferences.getIdUser()
        viewModel.profilefind(id)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gender)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        addTextWatchers()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.findProfileResult.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> handleSuccess(result.data)
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
            StyleableToast.makeText(
                requireContext(),
                getString(R.string.success),
                R.style.success
            ).show()

            findNavController().navigate(
                R.id.profileFragment,
                null,
                NavOptions.Builder().setPopUpTo(
                    findNavController().currentDestination?.id ?: -1, true
                ).build()
            )
        }
    }

    private fun handleSuccess(profileResponse: ProfileResponse?) {
        showProgressbar(false)
        profileResponse?.user?.let { user ->
            originalFullName = user.fullName
            originalPhoneNumber = user.phoneNumber
            originalGmail = user.gmail
            originalBirthDay = user.birthday
            originalGender = if (user.grender == "Female") 0 else 1

            binding.nameEditText.setText(originalFullName ?: getString(R.string.name))
            binding.phoneEditText.setText(originalPhoneNumber ?: getString(R.string.phone_suggest))
            binding.dateEditText.setText(originalBirthDay ?: getString(R.string.birthDay))
            binding.mailEditText.setText(originalGmail ?: getString(R.string.email))
            binding.spinner.setSelection(originalGender)
        }
    }

    private fun handleError(errorMessage: String?) {
        showProgressbar(false)
        Log.e("EditProfileFragment", "Profile error: $errorMessage")
    }

    private fun addTextWatchers() {
        binding.nameEditText.addTextChangedListener(createTextWatcher())
        binding.phoneEditText.addTextChangedListener(createTextWatcher())
        binding.mailEditText.addTextChangedListener(createTextWatcher())
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
        val currentFullName = binding.nameEditText.text.toString().trim()
        val currentPhoneNumber = binding.phoneEditText.text.toString().trim()
        val currentGmail = binding.mailEditText.text.toString().trim()
        val currentBirthDay = binding.dateEditText.text.toString().trim()
        val currentGender = binding.spinner.selectedItemPosition

        binding.btnNextPager.isEnabled = (currentFullName != originalFullName ||
                currentPhoneNumber != originalPhoneNumber ||
                currentGmail != originalGmail ||
                currentBirthDay != originalBirthDay ||
                currentGender != originalGender)
    }

    override fun setOnClick() {
        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                genderSelection = position
                checkIfAnyFieldChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText, binding.layoutInputMail)
        }

        binding.btnNextPager.setOnClickListener {
            validateAndSubmit()
        }
        setUpEditTextListeners()
    }

    private fun setUpEditTextListeners() {
        binding.mailEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField {
                    CheckValidate.checkEmail(
                        requireContext(),
                        binding.mailEditText,
                        binding.layoutInputMail,
                        binding.layoutInputPhone
                    )
                }
            } else false
        }

        binding.phoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField {
                    CheckValidate.checkPhone(
                        requireContext(),
                        binding.phoneEditText,
                        binding.layoutInputPhone,
                        binding.btnNextPager
                    )
                }
            } else false
        }

        binding.nameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateField {
                    CheckValidate.checkStr(
                        requireContext(),
                        binding.nameEditText,
                        binding.layoutInputPhone,
                        binding.layoutInputMail
                    )
                }
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
        val isPhoneValid = validateField {
            CheckValidate.checkPhone(
                requireContext(),
                binding.phoneEditText,
                binding.layoutInputPhone, binding.btnNextPager
            )
        }

        val isEmailValid = validateField {
            CheckValidate.checkEmail(
                requireContext(),
                binding.mailEditText,
                binding.layoutInputMail,
                binding.layoutInputPhone
            )
        }

        val isNameValid = validateField {
            CheckValidate.checkStr(
                requireContext(),
                binding.nameEditText,
                binding.layoutInputPhone,
                binding.layoutInputMail
            )
        }

        if (isPhoneValid && isEmailValid && isNameValid) {
            submitProfileChanges()
        } else {
            // Additional logging for debugging
            Log.d(
                "EditProfileFragment",
                "Validation failed. Phone: $isPhoneValid ${binding.phoneEditText.text}, Email: $isEmailValid, Name: $isNameValid"
            )
            Toast.makeText(requireContext(), R.string.inputFullInfo, Toast.LENGTH_SHORT).show()
        }
    }


    private fun submitProfileChanges() {
        val fullName = binding.nameEditText.text.toString().trim()
        val phoneNumber = binding.phoneEditText.text.toString().trim()
        val gmail = binding.mailEditText.text.toString().trim()
        val birthDay = binding.dateEditText.text.toString().trim()
        val gender = genderSelection.toString()

        binding.btnNextPager.isEnabled = false
        viewModel.setUp(id, null, phoneNumber, fullName, gmail, birthDay, gender)
    }
}

