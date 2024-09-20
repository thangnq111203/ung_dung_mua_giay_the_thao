package com.fpoly.shoes_app.framework.presentation.ui.profile.addressDetail

import android.util.Log
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentAddressDetailsBinding
import com.fpoly.shoes_app.framework.domain.model.profile.address.Addresse
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressDetailsFragment : BaseFragment<FragmentAddressDetailsBinding, AddressDetailsViewModel>(
    FragmentAddressDetailsBinding::inflate, AddressDetailsViewModel::class.java
), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var bundle: Addresse? = null
    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.address_detail)
        }
        bundle = arguments?.getParcelable("address")
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setupPreViews() {
    }

    override fun bindViewModel() {
    }

    override fun setOnClick() {

        binding.headerLayout.imgBack.setOnClickListener {
            navController?.popBackStack()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        bundle?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            updateMapAndAddress(latLng, it.detailAddress)
        }
    }

    private fun updateMapAndAddress(latLng: LatLng, address: String) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng).title(address))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))
        Log.d("AddressDetailsFragment", "Updated map with address: $address")
        binding.nameAddressEditText.hint = bundle?.nameAddress ?: ""
        binding.nameUserEditText.hint = bundle?.fullName ?: ""
        binding.layoutInputPhone.text = bundle?.phoneNumber.toString()
    }
}
