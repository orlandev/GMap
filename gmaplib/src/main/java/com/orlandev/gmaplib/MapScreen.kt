package com.orlandev.gmaplib

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.orlandev.gmaplib.composables.PlaceCardInfo
import com.orlandev.gmaplib.extensions.toLatLon
import com.orlandev.gmaplib.model.MapPlaceInfo
import com.orlandev.gmaplib.utils.MapCardButtonsEvent


const val TAG = "MapScreen"

@Composable
fun MapScreen(
    listOfMapPoints: List<MapPlaceInfo>,
    onMapCardButtonsEvent: (MapCardButtonsEvent) -> Unit
) {

    var isMapLoaded by remember { mutableStateOf(false) }

    val currentPlaceInfo = remember {
        mutableStateOf<MapPlaceInfo?>(null)
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMapView(
            modifier = Modifier.matchParentSize(),
            onMapLoaded = {
                isMapLoaded = true
            },
            mapPointsInfo = listOfMapPoints,
            onMarketSelected = {
                currentPlaceInfo.value = it
            }
        )
        if (!isMapLoaded) {
            AnimatedVisibility(
                modifier = Modifier
                    .matchParentSize(),
                visible = !isMapLoaded,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .wrapContentSize()
                )
            }
        }

        AnimatedVisibility(
            enter = slideInVertically(
                initialOffsetY = { 100 }
            ) + fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .padding(bottom = 57.dp)
                .align(Alignment.BottomCenter),
            visible = currentPlaceInfo.value != null
        ) {
            PlaceCardInfo(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                onMapCardButtonsEvent = onMapCardButtonsEvent,
                mapPlaceInfo = currentPlaceInfo.value
            )
        }
    }
}

@Composable
private fun GoogleMapView(
    modifier: Modifier,
    mapPointsInfo: List<MapPlaceInfo>,
    onMarketSelected: (MapPlaceInfo) -> Unit,
    onMapLoaded: () -> Unit
) {

    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPointsInfo[0].location.toLatLon(), 11f)
    }

    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
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
                    mapPointsInfo[0].location.toLatLon(),
                    11f
                )
            )
        },
        onPOIClick = {
            Log.d(TAG, "POI clicked: ${it.name}")
        }
    ) {
        // Drawing on the map is accomplished with a child-based API
        val markerClick: (Marker) -> Boolean = { currentMarkerSelected ->
            Log.d(TAG, "${currentMarkerSelected.title} was clicked")
            onMarketSelected(mapPointsInfo.first {
                it.location.toLatLon() == currentMarkerSelected.position
            })
            false
        }
        mapPointsInfo.forEach {
            MarkerInfoWindowContent(
                position = it.location.toLatLon(),
                title = it.title,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = markerClick,
            ) { marker ->
                Text(marker.title ?: "Title")
            }
        }
    }
}

