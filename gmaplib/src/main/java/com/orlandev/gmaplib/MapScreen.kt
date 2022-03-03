package com.orlandev.gmaplib

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Dp
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val TAG = "MapScreen"

@OptIn(
    ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
fun MapScreen(
    sheetPeekHeight: Dp = 200.dp,
    listOfMapPoints: List<MapPlaceInfo>,
    listOfFilters: List<String>,
    onMapCardButtonsEvent: (MapCardButtonsEvent) -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    var isMapLoaded by remember { mutableStateOf(false) }

    val currentPlaceInfo = remember {
        mutableStateOf<MapPlaceInfo?>(null)
    }

    val (currentFilter, setCurrentFilter) = rememberSaveable {
        mutableStateOf("")
    }

    val (showCardView, setShowCardView) = rememberSaveable {
        mutableStateOf(false)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (currentPlaceInfo.value != null) sheetPeekHeight else 0.dp,
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentPlaceInfo.value != null && scaffoldState.bottomSheetState.isCollapsed,
                exit = scaleOut() + fadeOut(),
                enter = scaleIn() + fadeIn()
            ) {
                FloatingActionButton(
                    onClick = { currentPlaceInfo.value = null })
                {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Close"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetContent = {
            LazyColumn(modifier = Modifier) {
                item {
                    Button(onClick = {
                        scope.launch {
                            if (scaffoldState.bottomSheetState.isExpanded)
                                scaffoldState.bottomSheetState.collapse()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)

                    }
                }
                items(500) {
                    Text(text = "Sheet Content")
                }
            }
        },
    ) {

        Box(Modifier.fillMaxSize()) {
            GoogleMapView(
                modifier = Modifier.matchParentSize(),
                onMapLoaded = {
                    isMapLoaded = true
                },
                currentFilter = currentFilter,
                mapPointsInfo = listOfMapPoints,
                onMarketSelected = {
                    currentPlaceInfo.value = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(state = ScrollState(0))
                    .align(Alignment.TopCenter)
            ) {
                listOfFilters.forEach { listOfFilterItem ->
                    Chip(modifier = Modifier.padding(8.dp), onClick = {
                        setCurrentFilter(listOfFilterItem)
                    }) {
                        Text(text = listOfFilterItem)
                    }
                }
            }


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

    }


}

@Composable
private fun GoogleMapView(
    modifier: Modifier,
    mapPointsInfo: List<MapPlaceInfo>,
    onMarketSelected: (MapPlaceInfo) -> Unit,
    onMapLoaded: () -> Unit,
    currentFilter: String
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

    val markerList = rememberSaveable {
        mutableStateOf(mapPointsInfo)
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

        val markerClick: (Marker) -> Boolean = { currentMarkerSelected ->
            onMarketSelected(mapPointsInfo.first {
                it.location.toLatLon() == currentMarkerSelected.position
            })
            false
        }

        markerList.value.forEach {
            MarkerInfoWindowContent(
                position = it.location.toLatLon(),
                visible = it.groupBy == currentFilter || currentFilter == "",
                title = it.title,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = markerClick,
            ) { marker ->
                Text(marker.title ?: "Title")
            }
        }

    }
}



