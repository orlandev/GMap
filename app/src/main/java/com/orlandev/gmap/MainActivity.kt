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
                    TestMapScreen()
                    /// TestSimpleMap()
                    //  MapScreenV2()
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
                        }, jsonStyle = """[
  {
    "featureType": "all",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "featureType": "all",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "lightness": -80
      }
    ]
  },
  {
    "featureType": "administrative",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "administrative.locality",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#263c3f"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#6b9a76"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#2b3544"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#9ca5b3"
      }
    ]
  },
  {
    "featureType": "road.arterial",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road.arterial",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1f2835"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#f3d19c"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#2f3948"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#515c6d"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "lightness": -20
      }
    ]
  }
]"""
                    )
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

    val currentPlaceInfo = remember{
        mutableStateOf<MapPlaceInfo?>(null)
    }
    MapScreen(
        fabBackgroundColor = Color.Red,
        listOfMapPoints = listOfMapPoints,
        listOfFilters = listOfFilters,
        onMapPlaceInfoSelected = {
            currentPlaceInfo.value = it
        },
        searchBarPlaceholder = { Text(text = "Search here") },
        jsonStyle =
        """[
  {
    "featureType": "all",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "featureType": "all",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "lightness": -80
      }
    ]
  },
  {
    "featureType": "administrative",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "administrative.locality",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#263c3f"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#6b9a76"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#2b3544"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#9ca5b3"
      }
    ]
  },
  {
    "featureType": "road.arterial",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road.arterial",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1f2835"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#f3d19c"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#2f3948"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#515c6d"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "lightness": -20
      }
    ]
  }
]"""
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
