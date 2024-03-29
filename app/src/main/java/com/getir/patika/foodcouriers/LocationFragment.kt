package com.getir.patika.foodcouriers

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class LocationFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private lateinit var addressTextView: TextView

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        googleMap.setOnMapClickListener {clickedLatLng->
            manageGoogleMap(
                map = googleMap,
                latLng = clickedLatLng,
            )
            getAddressFromLatLng(clickedLatLng){
                addressTextView.text = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_location, container, false)
        addressTextView = view.findViewById<TextView>(R.id.address_textview)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        registerLauncher{
            //permission granted
            getUserLocation(){ myLocLatLng->
                manageGoogleMap(
                    map = map,
                    latLng = myLocLatLng,
                    shouldAnimated = true
                )
                getAddressFromLatLng(myLocLatLng){
                    addressTextView.text = it
                }
            }
        }

        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        initAutocompleteFragment { selectedPlace->
            val selectedLatLng = selectedPlace.latLng

            manageGoogleMap(
                map = map,
                latLng = selectedLatLng!!,
                shouldAnimated = true
            )

            getAddressFromLatLng(selectedLatLng){
                addressTextView.text = it
            }
        }
    }

    // Managing with map when user click, selected place or first start of app
    private fun manageGoogleMap(
        map:GoogleMap,
        latLng: LatLng,
        markerTitle: String = "My Location",
        markerIcon: Int = R.drawable.ic_marker,
        shouldAnimated: Boolean = false
    ){
        map.clear()
        if (shouldAnimated)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_VALUE))

        map.addMarker(MarkerOptions().position(latLng).title(markerTitle).icon(
            generateBitmapDescriptorFromRes(requireActivity().baseContext!!, markerIcon)
        ))
    }

    // Initialize and can be configurable with onPlaceSelected lambda function
    private fun initAutocompleteFragment(
        onPlaceSelected: (Place) -> Unit
    ){
        Places.initialize(requireActivity().applicationContext, getString(R.string.MAPS_API_KEY))
        autocompleteFragment = childFragmentManager.findFragmentById(R.id.autoComplete_fragment_my) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status) {
                // Handle error
            }

            override fun onPlaceSelected(place: Place) {
                onPlaceSelected(place)
            }

        })
    }

    // Registering location permission launcher
    private fun registerLauncher(
        onPermissionDenied: () -> Unit = {},
        onPermissionGranted: () -> Unit = {}
    ){
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission Granted
                onPermissionGranted()
            }
            else {
                // Permission Denied
                onPermissionDenied()
            }
        }
    }


    // Getting user location with using FusedLocationClient. When it found a location,
    // it can be used with onSuccess lambda function.
    @SuppressLint("MissingPermission")
    private fun getUserLocation(
        onSuccess: (LatLng) -> Unit = {}
    ){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false })
            .addOnSuccessListener { location: Location? ->
                if (location != null){
                    val myLocationLatLng = LatLng(location.latitude, location.longitude)
                    onSuccess(myLocationLatLng)
                }
            }
    }

    // Getting address from latitude and longitude. Also accept a callback when address found
    // so Textview can be changed via that callback.
    private fun getAddressFromLatLng(latLng: LatLng, onAddressFind: (String)-> Unit = {}){
        val geocoder = Geocoder(requireActivity().baseContext)
        val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)  // Get the first address line
                // Use the address string for further processing
                println("Address $address")
                onAddressFind(address)
            } else {
                // Handle the case where address cannot be retrieved
            }
        }
    }

}