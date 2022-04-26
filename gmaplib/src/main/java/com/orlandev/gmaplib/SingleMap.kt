package com.orlandev.gmaplib

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.orlandev.gmaplib.extensions.toLatLon
import com.orlandev.gmaplib.model.MapPlaceInfo

//Experimental
@Composable
fun SimpleMap(
    modifier: Modifier = Modifier,
    staticMapPoint: MapPlaceInfo,
    zoomStart: Float,
    jsonStyle: String,// in JSON format
    onMapLoaded: () -> Unit,
    onMarketSelected: (Boolean) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(staticMapPoint.location.toLatLon(), zoomStart)
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                mapStyleOptions = if (jsonStyle.isNotEmpty()) MapStyleOptions(jsonStyle) else null
            )
        )
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMapLoaded = onMapLoaded,
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(
                CameraPosition.fromLatLngZoom(
                    staticMapPoint.location.toLatLon(),
                    zoomStart
                )
            )
        },
        onPOIClick = {
            Log.d(TAG, "POI clicked: ${it.name}")
        }
    ) {

        val markerClick: (Marker) -> Boolean = {
            onMarketSelected(true)
            false
        }

        MarkerInfoWindowContent(
            state = MarkerState(position = staticMapPoint.location.toLatLon()),
            visible = true,
            title = staticMapPoint.title,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
            onClick = markerClick,
        ) { marker ->
            Text(marker.title ?: "Title")
        }
    }

}
