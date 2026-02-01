package com.example.pennlabsapplication.googlemaps.place

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pennlabsapplication.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import androidx.core.graphics.scale

class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager) {


    private val truckIcon: BitmapDescriptor by lazy {
        val height = 100
        val width = 100
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.foodtruck_icon)
        val scaledBitmap = bitmap.scale(width, height, false)
        BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    override fun onBeforeClusterItemRendered(item: Place, markerOptions: MarkerOptions) {
        markerOptions.title(item.name)
            .position(item.latLng)
            .icon(truckIcon)
    }

    override fun onClusterItemRendered(clusterItem: Place, marker: Marker) {
        marker.tag = clusterItem
    }
}
