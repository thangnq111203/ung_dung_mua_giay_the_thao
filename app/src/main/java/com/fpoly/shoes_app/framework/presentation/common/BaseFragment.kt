package com.fpoly.shoes_app.framework.presentation.common

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.fpoly.shoes_app.databinding.AlertDialogViewBinding
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.ViewModelActivity
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.dialog.ProgressbarDialogFragment
import com.fpoly.shoes_app.utility.service.ServiceUtil
import javax.inject.Inject


abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val viewModelClass: Class<VM>
) : Fragment() {

    private lateinit var _binding: VB
    protected val binding: VB get() = _binding

    protected val viewModelActivity: ViewModelActivity by activityViewModels()

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[viewModelClass]
    }

    private var _navController: NavController? = null

    protected val navController: NavController? get() = _navController

    @Inject
    internal lateinit var service: ServiceUtil

    @Inject
    internal lateinit var sharedPreferences: SharedPreferencesManager

    @Inject
    internal lateinit var progressDialog: ProgressbarDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v(TAG, "onAttach: $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate: $this")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView: $this")
        _navController = findNavController()
        _binding = bindingInflater.invoke(layoutInflater, container, false)
        setupPreViews()
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated: $this")
        (requireActivity() as? MainActivity)?.showBottomNavigation(true)
        setupViews()
        setOnClick()
        bindViewModel()
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart: $this")
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume: $this")
    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause: $this")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop: $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG, "onDestroyView: $this")
        _navController = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v(TAG, "onDetach: $this")
    }

    open fun setupPreViews() {}
    abstract fun setupViews()

    abstract fun bindViewModel()

    abstract fun setOnClick()

    protected fun showProgressbar(isShowProgressbar: Boolean) {
        val fragmentManager = childFragmentManager
        val existingFragment = fragmentManager.findFragmentByTag(TAG_PROGRESSBAR_DIALOG_FRAGMENT)

        if (isShowProgressbar) {
            if (existingFragment == null) {
                fragmentManager.beginTransaction()
                    .add(progressDialog, TAG_PROGRESSBAR_DIALOG_FRAGMENT)
                    .commitNowAllowingStateLoss()
            }
        } else {
            if (existingFragment != null) {
                progressDialog.dismiss()
                fragmentManager.beginTransaction()
                    .remove(existingFragment)
                    .commitNowAllowingStateLoss()
            }
        }
    }

    protected fun showAlertDialog(
        imgSuccess: Boolean = false,
        title: String = "",
        description: String = "",
        button: String = "",
        buttonCancel: String = "",
        onClick: () -> Unit = {},
        onClickCancel: () -> Unit = {},
    ) {
        val builder = AlertDialog.Builder(requireActivity())
        val binding = AlertDialogViewBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        binding.run {
            tvTitle.text = title
            tvDescription.text = description
            btnButton.tvButton.text = button
            tvCancel.text = buttonCancel

            tvTitle.isVisible = title.isNotBlank()
            tvDescription.isVisible = description.isNotBlank()
            btnButton.root.isVisible = button.isNotBlank()
            tvCancel.isVisible = buttonCancel.isNotBlank()
            img.isVisible = imgSuccess

            btnButton.root.setOnClickListener {
                onClick()
                dialog.dismiss()
            }
            tvCancel.setOnClickListener {
                onClickCancel()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private companion object {
        private const val TAG_PROGRESSBAR_DIALOG_FRAGMENT = "ProgressbarDialogFragment"
    }
}