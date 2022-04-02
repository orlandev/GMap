package com.orlandev.gmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.orlandev.gmap.ui.theme.GMapTheme
import com.orlandev.gmaplib.MapScreen
import com.orlandev.gmaplib.SimpleMap
import com.orlandev.gmaplib.TAG
import com.orlandev.gmaplib.model.GroupBy
import com.orlandev.gmaplib.model.MapFilter
import com.orlandev.gmaplib.model.MapPlaceInfo
import com.orlandev.gmaplib.model.MapPoint
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //TestMapScreen()
                     TestSimpleMap()
                }
            }
        }
    }
}

@Composable
fun TestSimpleMap() {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                val loaded = remember {
                    mutableStateOf(false)
                }
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    SimpleMap(
                        modifier = Modifier.fillMaxSize(),
                        staticMapPoint = MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.009806, -75.828503),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = MapFilter("3,2,6,8", "Six")),
                        ), zoomStart = 19f, onMapLoaded = {
                            loaded.value = true

                        }, onMarketSelected = {
                            Log.d(TAG, "MARKER CLICKED")
                        })
                    if (!loaded.value) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun TestMapScreen() {
    val listOfFilters = listOf(
        MapFilter("0", "First"),
        MapFilter("1", "Second"),
        MapFilter("2", "Four"),
        MapFilter("3", "Six"),
        MapFilter("4", "Seven"),
        MapFilter("5", "Home"),
        MapFilter("6", "First"),
        MapFilter("7", "Other"),
        MapFilter("8", "Ocho"),
    )
    val listOfMapPoints = listOf<MapPlaceInfo>(
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.009806, -75.828503),
            title = "Lorem Ipsum",
            groupBy = GroupBy(filter = MapFilter("3,2,6,8", "Six")),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.013677, -75.812710),
            title = "Real Home",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.009161, -75.839146),
            title = "Pretty Space",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.043998, -75.829533),
            title = "Other place",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.027870, -75.852879),
            title = "New Space",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.012064, -75.866097),
            title = "Restaurant",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),

        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.013677, -75.839833),
            title = "Lorem",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),
        MapPlaceInfo(
            uuid = UUID.randomUUID().toString(),
            location = MapPoint(20.019322, -75.817688),
            title = "Ipsum",
            groupBy = GroupBy(filter = listOfFilters[Random().nextInt(listOfFilters.size)]),
        ),

        )

    val currentPlaceInfo = rememberSaveable {
        mutableStateOf<MapPlaceInfo?>(null)
    }
    MapScreen(
        fabBackgroundColor = Color.Red,
        listOfMapPoints = listOfMapPoints,
        listOfFilters = listOfFilters,
        onMapPlaceInfoSelected = {
            currentPlaceInfo.value = it
        },
        searchBarPlaceholder = { Text(text = "Search here") }
    ) {
        BoxWithConstraints {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                currentPlaceInfo.value?.let { mapPlaceInfo ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(text = mapPlaceInfo.title)
                                Text(text = mapPlaceInfo.location.toString())
                            }
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(500) {
                        Text(text = "LIST COLUMN ITEM")
                    }
                }
            }
        }
    }
}
