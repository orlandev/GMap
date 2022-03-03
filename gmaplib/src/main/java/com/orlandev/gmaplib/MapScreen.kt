package com.orlandev.gmaplib

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.*
import com.orlandev.gmaplib.extensions.toLatLon
import com.orlandev.gmaplib.model.MapPlaceInfo

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
    sheetContent: @Composable () -> Unit
) {

    val scaffoldState = rememberBottomSheetScaffoldState()

    var isMapLoaded by remember { mutableStateOf(false) }

    val currentPlaceInfo = remember {
        mutableStateOf<MapPlaceInfo?>(null)
    }

    val (currentFilter, setCurrentFilter) = rememberSaveable {
        mutableStateOf("")
    }

    val (searchFilter, setSearchFilter) = rememberSaveable {
        mutableStateOf("")
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
            sheetContent()
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        OutlinedTextField(
                            singleLine = true,
                            maxLines = 1,
                            placeholder = { Text(text = "Search here") },
                            value = searchFilter,
                            onValueChange = setSearchFilter,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                            )
                        )
                    }
                }

                val scrollFilterState = rememberScrollState(0)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(state = scrollFilterState)
                ) {

                    val chipBorderStroke =
                        BorderStroke(
                            0.7.dp,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                        )


                    Chip(
                        modifier = Modifier.padding(8.dp),
                        border = chipBorderStroke,
                        onClick = {
                            setCurrentFilter("")
                        }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                    listOfFilters.forEach { listOfFilterItem ->

                        Chip(
                            modifier = Modifier.padding(8.dp),
                            border = chipBorderStroke,
                            onClick = {
                                setCurrentFilter(listOfFilterItem)
                            }) {
                            Text(text = listOfFilterItem)
                        }
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
                visible = it.groupBy?.filter == currentFilter || currentFilter == "",
                title = it.title,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = markerClick,
            ) { marker ->
                Text(marker.title ?: "Title")
            }
        }

    }
}



