package com.fpoly.shoes_app.framework.presentation.ui.profile.helpCenter

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.databinding.FragmentHelpCenterBinding
import com.fpoly.shoes_app.framework.presentation.common.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.schedule

@AndroidEntryPoint
class HelpCenterFragment : BaseFragment<FragmentHelpCenterBinding, HelpCenterViewModel>(
    FragmentHelpCenterBinding::inflate, HelpCenterViewModel::class.java
), OnMapReadyCallback {
    private lateinit var phone: String
    private lateinit var mMap: GoogleMap
    private var latLng: LatLng? = null
    private lateinit var address: String

    override fun setupPreViews() {
        binding.apply {
            phone = getString(R.string.phoneHelp1)
            phoneHelp.text = phone
            store1Help.text = getString(R.string.store1Detail)
            store2Help.text = getString(R.string.store2Detail)
        }
        address = getString(R.string.store1Detail)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Timer().schedule(100) {
            activity?.runOnUiThread {
                searchLocationByName(address)
            }
        }
    }

    override fun setupViews() {
        binding.run {
            headerLayout.tvTitle.text = getString(R.string.helpCenter)
        }
    }

    override fun bindViewModel() {
    }

    override fun setOnClick() {
        binding.apply {
            phoneHelp.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:$phone")
                startActivity(dialIntent)
            }
            store1Help.setOnClickListener {
                searchLocationByName(getString(R.string.store1Detail))
            }
            store2Help.setOnClickListener {
                searchLocationByName(getString(R.string.store2Detail))
            }
            headerLayout.imgBack.setOnClickListener {
                navController?.popBackStack()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
    }
}