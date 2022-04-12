package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.utils.Constants
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class RepresentativeFragment : Fragment() {

    companion object {
        const val LOG_TAG: String = "RepresentativeFragment"
    }

    // Check the Android Version.
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    // Declare Binding and ViewModel
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var application: Application
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var viewModelFactory: RepresentativeViewModelFactory

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        application = requireNotNull(this.activity).application

        //TODO: Establish bindings

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search

        return binding.root
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initViewModelAndBinding() {
        viewModelFactory = RepresentativeViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RepresentativeViewModel::class.java)
        binding.lifecycleOwner = this
    }

    private fun initAdapter() {

    }

    private fun initClickListener() {

    }


//------------------------------------- Location Permission Checking -------------------------------


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        // Handle location permission result to get location on permission granted
        Log.d(LOG_TAG, "onRequestPermissionResult: run.")
        if (
            grantResults.isEmpty() ||
            grantResults[Constants.LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == Constants.REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE && grantResults[Constants.BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED))
        {
            // Permission denied.
            Snackbar.make(this.requireView(), R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings) {
                    // Displays App settings screen.
                    // The BuildConfig.APPLICATION_ID has been replaced by BuildConfig.LIBRARY_PACKAGE_NAME
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.LIBRARY_PACKAGE_NAME, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {

        Log.d(LOG_TAG, "checkLocationPermissions: run.")
        return if (isPermissionGranted()) {
            Log.d(LOG_TAG, "The Permission Granted.")
            getLocation()
            true
        } else {
            Log.d(LOG_TAG, "The Permission Deny.")
            requestForegroundAndBackgroundLocationPermissions()
            false
        }

    }

    private fun isPermissionGranted() : Boolean {

        Log.d(LOG_TAG, "isPermissionGranted: run.")
        // Check if permission is already granted and return (true = granted, false = denied/other)
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION))
        // background permission according to the Android Version.
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved

    }

    private fun requestForegroundAndBackgroundLocationPermissions() {

        Log.d(LOG_TAG, "requestForegroundAndBackgroundLocationPermissions: run.")
        if (isPermissionGranted())
            return
        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val resultCode = when {
            runningQOrLater -> {
                // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                Constants.REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> Constants.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        requestPermissions(
            permissionsArray,
            resultCode
        )

    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}