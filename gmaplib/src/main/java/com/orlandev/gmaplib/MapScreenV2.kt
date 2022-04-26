package com.orlandev.gmaplib

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

//THIS IS ONLY FOR INTERNAL  TESTING

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreenV2() {


    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = 10f, minZoomPreference = 5f)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false)
        )
    }
    GoogleMap(properties = mapProperties, uiSettings = mapUiSettings)
    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    LaunchedEffect(backdropState) {
        backdropState.reveal()
    }

    val offset by backdropState.offset
    val halfHeightDp = LocalConfiguration.current.screenHeightDp / 2
    val halfHeightPx = with(LocalDensity.current) {
        halfHeightDp.dp.toPx()
    }

    val scope = rememberCoroutineScope()

    BackdropScaffold(
        appBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Backdrop Component",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    backdropState.conceal()
                                }
                            },
                        textAlign = TextAlign.Center
                    )
                },
                backgroundColor = Color.Transparent
            )
        },
        scaffoldState = backdropState,
        frontLayerScrimColor = Color.Unspecified,
        peekHeight = 0.dp,
        backLayerBackgroundColor = Color.White,
        headerHeight = halfHeightDp.dp,
        backLayerContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .alpha(offset / halfHeightPx)
            ) {
                val newYork = LatLng(40.73, -73.9712) //lat long
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(newYork, 12f)
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(state = MarkerState(LatLng(40.73, -73.9912)))
                }
            }
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val verticalListAlpha = ((halfHeightPx - offset) / halfHeightPx).coerceIn(0f..1f)
                val horizontalAlpha = (offset / halfHeightPx).coerceIn(0f..1f)

                TimeTabs()
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.alpha(verticalListAlpha)
                    ) {
                        itemsIndexed(List(30) {
                            "Movie $it"
                        }) { index, item ->
                            Column {
                                Card(
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .size(width = 360.dp, height = 200.dp)
                                        .padding(8.dp)
                                        .clickable { }
                                ) {
                                    Image(
                                        painter = painterResource(id = getImageResource(index)),
                                        contentDescription = "List Image",
                                        modifier = Modifier.fillMaxSize(),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Movie $index",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.subtitle2
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 16.dp))
                        }
                    }

                    LazyRow(
                        modifier = Modifier.alpha(horizontalAlpha)
                    ) {
                        itemsIndexed(List(30) {
                            "Movie $it"
                        }) { index, item ->
                            Column {
                                Card(
                                    elevation = 4.dp,
                                    modifier = Modifier
                                        .size(width = 280.dp, height = 200.dp)
                                        .padding(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = getImageResource(index)),
                                        contentDescription = "List Image",
                                        modifier = Modifier.fillMaxSize(),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(start = 12.dp),
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Text(
                                    text = "Released on 03/04/2022.",
                                    modifier = Modifier.padding(start = 12.dp, top = 8.dp),
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun getImageResource(index: Int) = when (index % 3) {
    0 -> R.drawable.movie1
    1 -> R.drawable.movie2
    2 -> R.drawable.movie3
    else -> R.drawable.movie1
}

@Composable
fun TimeTabs() {
    var tabIndex by remember { mutableStateOf(0) }
    TabRow(
        selectedTabIndex = tabIndex,
        backgroundColor = Color.Transparent
    ) {
        listOf("Morning", "Afternoon", "Night").forEachIndexed { index, text ->
            Tab(
                selected = tabIndex == index,
                onClick = {
                    tabIndex = index
                },
                text = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.button
                    )
                },
                modifier = Modifier.background(Color.White)
            )
        }
    }
}