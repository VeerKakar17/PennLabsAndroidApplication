package com.example.pennlabsapplication.googlemaps

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pennlabsapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.example.pennlabsapplication.googlemaps.place.Place
import com.example.pennlabsapplication.googlemaps.place.PlaceRenderer
import com.example.pennlabsapplication.googlemaps.place.PlacesReader
import com.google.maps.android.clustering.ClusterManager

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private val places: List<Place> by lazy {
        PlacesReader(requireContext()).read()
    }

    private var userLocation: Location? = null
    private var userMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userLocation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_LOCATION, Location::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_LOCATION)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment_container) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if (userLocation == null) {
            val defaultLatLng = LatLng(39.9529, -75.197098)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 14f))
        }

        addClusteredMarkers(googleMap)
        addUserMarker(googleMap)
    }

    private fun addClusteredMarkers(googleMap: GoogleMap) {
        val clusterManager = ClusterManager<Place>(requireContext(), googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                requireContext(),
                googleMap,
                clusterManager
            )

        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireActivity()))

        clusterManager.addItems(places)
        clusterManager.cluster()

        clusterManager.setOnClusterItemClickListener { item ->
            addCircle(googleMap, item)
            return@setOnClusterItemClickListener false
        }

        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            clusterManager.onCameraIdle()
        }
    }

    private var circle: Circle? = null

    private fun addCircle(googleMap: GoogleMap, item: Place) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(item.latLng)
                .radius(1000.0)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryTranslucent))
                .strokeColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        )
    }

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_directions_bike_black_24dp, color)
    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
                    .icon(bicycleIcon)
            )
            marker?.tag = place
        }
    }

    private fun addUserMarker(googleMap: GoogleMap) {
        userMarker?.remove()
        userLocation?.let {
            val userLatLng = LatLng(it.latitude, it.longitude)
            userMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(userLatLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
        }
    }

    companion object {
        val TAG = MapsFragment::class.java.simpleName
        private const val ARG_LOCATION = "arg_location"

        fun newInstance(location: Location?): MapsFragment {
            val args = Bundle()
            args.putParcelable(ARG_LOCATION, location)
            val fragment = MapsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
