package com.fpoly.shoes_app.framework.presentation.ui.profile.editOrAdd

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentEditoraddBinding
import com.fpoly.shoes_app.framework.data.othetasks.CheckValidate.strNullOrEmpty
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.domain.model.profile.address.addAddress.AddAddress
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.fpoly.shoes_app.utility.Resource
import com.fpoly.shoes_app.utility.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class EditOrAddFragment : BaseFragment<FragmentEditoraddBinding, EditOrAddViewModel>(
    FragmentEditoraddBinding::inflate, EditOrAddViewModel::class.java
), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var bundle: Addresse? = null
    private var check: Int? = null
    private var checkBox: String? = null
    private var addressDetail: String = ""
    private var latLng: LatLng? = null
    private var initialNameAddress: String? = null
    private var initialNameUser: String? = null
    private var initialPhone: String? = null
    private var initialDetailAddress: String? = null
    private var initialCheckAddress: String? = null

    override fun setupPreViews() {
        // Add any additional setup code here if needed
    }

    @SuppressLint("SuspiciousIndentation")
    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.add_address)
        }
        bundle = arguments?.getParcelable("address")
        check = arguments?.getInt("check")
        initialNameAddress = bundle?.nameAddress ?: ""
        initialNameUser = bundle?.fullName ?: ""
        initialPhone = bundle?.phoneNumber ?: ""
        initialDetailAddress = bundle?.detailAddress ?: ""
        initialCheckAddress = bundle?.permission ?: "1"
        latLng = bundle?.latitude?.let { bundle?.longitude?.let { it1 -> LatLng(it, it1) } }
        binding.btnAdd.text =
            if (check == 0) getString(R.string.add) else getString(R.string.update)
        binding.addressDetail.visibility = if (check == 0) View.GONE else View.VISIBLE

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (check == 1) {
            binding.nameAddressEditText.setText(bundle?.nameAddress ?: "")
            binding.nameUserEditText.setText(bundle?.fullName ?: "")
            binding.phoneUserEditText.setText(bundle?.phoneNumber.toString())
        }
        binding.checkbox.isChecked = bundle?.permission != "1"
    }

    override fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            var viewModelCheck: StateFlow<Resource<Any>>
            if (check == 0) viewModelCheck = viewModel.addAddressResult
            else viewModelCheck = viewModel.updateAddressResult
            viewModelCheck.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        showProgressbar(false)
                        result.data?.let { addressResponse ->
                            if (addressResponse.success) {
                                Toast.makeText(
                                    requireContext(), addressResponse.message, Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()

                            } else {
                                StyleableToast.makeText(
                                    requireContext(), addressResponse.message, R.style.fail
                                ).show()
                            }
                        }
                    }

                    Status.ERROR -> {
                        showProgressbar(false)
                        val errorMessage = strNullOrEmpty(result.message)
                        StyleableToast.makeText(
                            requireContext(), errorMessage, R.style.fail
                        ).show()
                    }

                    Status.LOADING -> showProgressbar(true)
                    Status.INIT -> Unit
                }
            }
        }
    }

    override fun setOnClick() {
        binding.searchMap.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchLocationByName(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        binding.nameAddressEditText.addTextChangedListener {
            binding.btnAdd.isEnabled = hasAddressChanged()
        }
        binding.nameUserEditText.addTextChangedListener {
            binding.btnAdd.isEnabled = hasAddressChanged()
        }
        binding.phoneUserEditText.addTextChangedListener {
            binding.btnAdd.isEnabled = hasAddressChanged()
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            checkBox = if (!isChecked) "1" else "0"
            binding.btnAdd.isEnabled = hasAddressChanged()
        }

        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }

        binding.btnAdd.setOnClickListener {
            Log.e("checkValidate", checkAddressChanged().toString())
            Log.e("checkValidate", latLng.toString())
            if (checkAddressChanged()) {

                val nameAddress = binding.nameAddressEditText.text.toString()
                val nameUser = binding.nameUserEditText.text.toString()
                val phoneUser = binding.phoneUserEditText.text.toString()

                if (latLng != null) {
                    val latiAddress = latLng?.latitude ?: bundle?.latitude
                    val longAddress = latLng?.longitude ?: bundle?.longitude
                    val id = sharedPreferences.getIdUser()

                    if (latiAddress != null && longAddress != null) {
                        val address = AddAddress(
                            detailAddress = addressDetail,
                            nameAddress = nameAddress,
                            phoneNumber = phoneUser,
                            latitude = latiAddress,
                            longitude = longAddress,
                            userId = id,
                            fullName = nameUser,
                            permission = checkBox
                        )

                        if (check == 0) {
                            viewModel.addAddress(address)
                        } else {
                            bundle?.id?.let { id ->
                                viewModel.updateAddress(id, address)
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Unable to add or update the address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please fill in the address details.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        bundle?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            updateMapAndAddress(latLng, it.detailAddress)
        }
    }

    private fun searchLocationByName(locationName: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> =
                geocoder.getFromLocationName(locationName, 1) ?: emptyList()
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                latLng = LatLng(address.latitude, address.longitude)
                updateMapAndAddress(latLng!!, address.getAddressLine(0))
            } else {
                Toast.makeText(context, getString(R.string.no_find_address), Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapAndAddress(latLng: LatLng, address: String) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
        Log.d("AddressDetailsFragment", "Updated map with address: $address")
        addressDetail = address
    }

    private fun hasAddressChanged(): Boolean {
        val currentNameAddress = binding.nameAddressEditText.text.toString()
        val currentDetailAddress = latLng?.toString() ?: ""
        val currentCheckBoxState = if (!binding.checkbox.isChecked) "1" else "0"

        return currentNameAddress != initialNameAddress ||
                currentDetailAddress != initialDetailAddress ||
                currentCheckBoxState != initialCheckAddress
    }

    private fun checkAddressChanged(): Boolean {
        val currentNameAddress = binding.nameAddressEditText.text.toString()
        val currentNameUser = binding.nameUserEditText.text.toString()
        val currentPhone = binding.phoneUserEditText.text.toString()
        val currentCheckBoxState = if (!binding.checkbox.isChecked) "1" else "0"
        return (currentNameUser != initialNameUser || currentNameAddress != initialNameAddress || currentCheckBoxState != initialCheckAddress || currentPhone != initialPhone)
    }
}
