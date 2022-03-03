package com.orlandev.gmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orlandev.gmap.ui.theme.GMapTheme
import com.orlandev.gmaplib.MapScreen
import com.orlandev.gmaplib.model.GroupBy
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
                    val listOfMapPoints = listOf<MapPlaceInfo>(
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.009806, -75.828503),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.013677, -75.812710),
                            title = "Real Home",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.009161, -75.839146),
                            title = "Pretty Space",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.043998, -75.829533),
                            title = "Other place",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.027870, -75.852879),
                            title = "New Space",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.012064, -75.866097),
                            title = "Restaurant",
                            groupBy = GroupBy(filter = "Third"),
                        ),

                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.013677, -75.839833),
                            title = "Lorem",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            uuid = UUID.randomUUID().toString(),
                            location = MapPoint(20.019322, -75.817688),
                            title = "Ipsum",
                            groupBy = GroupBy(filter = "Four"),
                        ),

                        )

                    val currentPlaceInfo = rememberSaveable {
                        mutableStateOf<MapPlaceInfo?>(null)
                    }
                    MapScreen(
                        listOfMapPoints = listOfMapPoints,
                        listOfFilters = listOf(
                            "First",
                            "Second",
                            "Third",
                            "Four",
                            "Six",
                            "Seven",
                            "Home",
                            "Other"
                        ),
                        onMapPlaceInfoSelected = {
                            currentPlaceInfo.value = it
                        },
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
            }
        }
    }
}
