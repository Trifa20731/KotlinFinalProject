package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.utils.Constants
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class RepresentativeFragment : Fragment(), LocationListener {

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

    // ArrayAdapter to set the spinner.
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var representativeListAdapter: RepresentativeListAdapter

    // Location Manager.
    private lateinit var locationManager: LocationManager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        application = requireNotNull(this.activity).application

        // Init view model and data binding.
        initViewModelAndBinding()
        // Init click listener.
        initClickListener()
        // Init adapter.
        initAdapter()
        // Init observer function.
        initObserver()
        return binding.root
    }


//------------------------------------- Initialization ---------------------------------------------


    private fun initViewModelAndBinding() {

        viewModelFactory = RepresentativeViewModelFactory(application, ElectionDatabase.getInstance(application))
        viewModel = ViewModelProvider(this, viewModelFactory).get(RepresentativeViewModel::class.java)
        binding.lifecycleOwner = this

    }

    private fun initClickListener() {

        binding.findMyRepresentativeBtn.setOnClickListener { findMyRepresentative() }
        binding.useMyLocationBtn.setOnClickListener { checkLocationPermissions() }

    }

    private fun initAdapter() {

        arrayAdapter = ArrayAdapter.createFromResource(application, R.array.states, android.R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.stateSpinner.adapter = arrayAdapter

        representativeListAdapter = RepresentativeListAdapter()
        binding.representativeListRv.adapter = representativeListAdapter

    }

    private fun initObserver() {

        viewModel.representativeResponse.observe( viewLifecycleOwner, Observer { viewModel.updateRepresentativeList(it) })
        viewModel.representativeShowingList.observe( viewLifecycleOwner, Observer { representativeListAdapter.submitList(it) } )

    }


//------------------------------------- Find Representative Functions ------------------------------


    /** The function retrieve the representative list after entering the data in the EditText. */
    private fun findMyRepresentative() {

        val addressLineOne: String = binding.addressLineOneEt.text.toString()
        val addressLineTwo: String = binding.addressLineTwoEt.text.toString()
        val city: String = binding.cityEt.text.toString()
        val zip: String = binding.cityEt.text.toString()
        val state: String = binding.stateSpinner.selectedItem.toString()
        val address: Address = Address(addressLineOne, addressLineTwo, city, state, zip)
        viewModel.retrieveRepresentativeListByAddress(address)

    }

    private fun autoFillEditText(address: Address) {

        binding.addressLineOneEt.setText(address.line1)
        address.line2?.let { binding.addressLineTwoEt.setText(address.line2) }
        binding.cityEt.setText(address.city)
        binding.zipEt.setText(address.zip)
        binding.stateSpinner.setNewValue(address.state)

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
            Snackbar.make(activity!!.findViewById(R.id.content), R.string.permission_denied_explanation, Snackbar.LENGTH_INDEFINITE)
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
            checkDeviceLocationSettingsAndGoToMyLocation()
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

    private fun checkDeviceLocationSettingsAndGoToMyLocation(resolved: Boolean = true) {
        Log.d(LOG_TAG, "checkDeviceLocationSettingsAndGoToMyLocation, run.")
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }

        // Add the geofencing tasks.
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())

        // Failure Listener
        locationSettingsResponseTask.addOnFailureListener { exception ->
            Log.d(LOG_TAG, "Failure in location setting response task.")
            if (exception is ResolvableApiException && resolved){
                try {
                    this.startIntentSenderForResult(
                        exception.resolution.intentSender,
                        Constants.REQUEST_TURN_DEVICE_LOCATION_ON,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(LOG_TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                Snackbar.make(
                    binding.representativeMotionLayout,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndGoToMyLocation()
                }.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener { response ->
            Log.d(LOG_TAG, "Success in location setting response task.")
            if ( response.isSuccessful ) {
                locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val currentLocation: Location? = getLastUpdateLocation(locationManager)
                currentLocation?.let {
                    Log.d(LOG_TAG, "Get current location successfully.")
                    geoCodeLocation(it)
                }?:let {
                    Log.d(LOG_TAG, "Fail to get the current location.")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_TURN_DEVICE_LOCATION_ON) {
            checkDeviceLocationSettingsAndGoToMyLocation(false)
        }
    }

    private fun getLastUpdateLocation (locationManager: LocationManager): Location? {
        return if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            null
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this)
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }

    private fun geoCodeLocation(location: Location): Address {

        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()

    }


//------------------------------------- Location Listener Override Functions -----------------------


    override fun onLocationChanged(location: Location) {
        Log.d(LOG_TAG, "onLocationChanged: Run.")
        val address = geoCodeLocation(location)
        autoFillEditText(address)
        viewModel.retrieveRepresentativeListByAddress(address)
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(LOG_TAG, "onProviderEnable: run.")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(LOG_TAG, "onProviderDisabled: run.")
        checkDeviceLocationSettingsAndGoToMyLocation()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(LOG_TAG, "onStatusChanged: run.")
    }

    private fun hideKeyboard() {

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)

    }

}