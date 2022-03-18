package com.orlandev.gmaplib


import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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

@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun MapScreen(
    sheetPeekHeight: Dp = 200.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    listOfMapPoints: List<MapPlaceInfo>,
    listOfFilters: List<String>,
    fabBackgroundColor: Color,
    fabContentColor: Color = Color.Black,
    onMapPlaceInfoSelected: (MapPlaceInfo) -> Unit,
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

    val focusManager = LocalFocusManager.current

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
                    backgroundColor = fabBackgroundColor,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    onClick = { currentPlaceInfo.value = null })
                {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetElevation = 8.dp,
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
                    focusManager.clearFocus()
                    onMapPlaceInfoSelected(it)
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (leadingIcon != null) {
                                leadingIcon()
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search, contentDescription = null
                                )
                            }
                        }
                        OutlinedTextField(
                            modifier = Modifier.weight(8f),
                            singleLine = true,
                            maxLines = 1,
                            placeholder = { Text(text = "Search here") },
                            value = searchFilter,
                            onValueChange = {
                                setCurrentFilter(it)
                                setSearchFilter(it)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                            )
                        )
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (trailingIcon != null) {
                                trailingIcon()
                            } else {
                                if (searchFilter.isNotEmpty())
                                    Icon(
                                        modifier = Modifier.clickable {
                                            setSearchFilter("")
                                            setCurrentFilter("")
                                        },
                                        imageVector = Icons.Default.Close, contentDescription = null
                                    )
                            }
                        }

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

                    MapChip(
                        modifier = Modifier.padding(8.dp),
                        border = chipBorderStroke,
                        onClick = {
                            setSearchFilter("")
                            setCurrentFilter("")
                        })
                    {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                    listOfFilters.forEach { listOfFilterItem ->
                        MapChip(
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
internal fun MapChip(
    modifier: Modifier,
    border: BorderStroke,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentSize(align = Alignment.Center)
            .clickable { onClick() }, border = border, shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            content()
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
                visible = it.groupBy?.filter == currentFilter || it.title.contains(currentFilter) || currentFilter == "",
                title = it.title,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = markerClick,
            ) { marker ->
                Text(marker.title ?: "Title")
            }
        }

    }
}



