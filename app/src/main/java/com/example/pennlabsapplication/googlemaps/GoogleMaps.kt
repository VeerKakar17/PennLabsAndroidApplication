package com.example.pennlabsapplication.googlemaps

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pennlabsapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.example.pennlabsapplication.googlemaps.place.Place
import com.example.pennlabsapplication.googlemaps.place.PlaceRenderer
import com.example.pennlabsapplication.googlemaps.place.PlacesReader
import com.google.maps.android.clustering.ClusterManager


class MapsFragment : Fragment(R.layout.activity_main) {

    private val places: List<Place> by lazy {
        PlacesReader(requireContext()).read()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            val defaultLatLng = LatLng(39.9529, -75.197098)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 14f))

            addClusteredMarkers(googleMap)
        }
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

    companion object {
        val TAG = MapsFragment::class.java.simpleName
    }
}
