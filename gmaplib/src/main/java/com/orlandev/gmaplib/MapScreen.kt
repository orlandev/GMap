package com.orlandev.gmaplib

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*


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

data class MapPoint(
    val latitude: Double,
    val longitude: Double,
)

fun MapPoint.toLatLon(): LatLng {
    return LatLng(
        latitude,
        longitude
    )
}

data class MapPlaceInfo(
    val location: MapPoint,
    val title: String = "",
    val description: String = "",
    val imageHeader: String = "",
)

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

sealed class MapCardButtonsEvent() {
    data class OnShareEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
    data class OnSeeMoreEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
    data class OnGoUrlEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
}


@Composable
fun PlaceCardInfo(
    modifier: Modifier = Modifier,
    mapPlaceInfo: MapPlaceInfo? = null,
    onMapCardButtonsEvent: (MapCardButtonsEvent) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            mapPlaceInfo?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    painter = if (it.imageHeader.isNotEmpty())
                        rememberImagePainter(data = it.imageHeader)
                    else
                        painterResource(id = R.drawable.image_placeholder),
                    contentDescription = mapPlaceInfo.title
                )
            }
            Text(
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                text = mapPlaceInfo?.title ?: "Title",
                style = MaterialTheme.typography.body1
            )
            Text(
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                text = mapPlaceInfo?.description ?: "Description",
                style = MaterialTheme.typography.caption
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(ScrollState(0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(shape = RoundedCornerShape(50),
                    onClick = {
                        onMapCardButtonsEvent(MapCardButtonsEvent.OnSeeMoreEvent(mapPlaceInfo = mapPlaceInfo!!))
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_more_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.see_more).uppercase())
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(shape = RoundedCornerShape(50),
                    onClick = {
                        onMapCardButtonsEvent(MapCardButtonsEvent.OnShareEvent(mapPlaceInfo = mapPlaceInfo!!))
                    }) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.share_text).uppercase())
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(shape = RoundedCornerShape(50), onClick = {
                    onMapCardButtonsEvent(MapCardButtonsEvent.OnGoUrlEvent(mapPlaceInfo = mapPlaceInfo!!))
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_link_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.url_text).uppercase())
                }
            }
        }
    }
}
