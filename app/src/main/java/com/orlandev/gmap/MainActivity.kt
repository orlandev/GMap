package com.orlandev.gmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.orlandev.gmap.ui.theme.GMapTheme
import com.orlandev.gmaplib.MapScreen
import com.orlandev.gmaplib.model.GroupBy
import com.orlandev.gmaplib.model.MapPlaceInfo
import com.orlandev.gmaplib.model.MapPoint

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
                            location = MapPoint(20.009806, -75.828503),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.812710),
                            title = "Real Home",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.009161, -75.839146),
                            title = "Pretty Space",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.043998, -75.829533),
                            title = "Other place",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.027870, -75.852879),
                            title = "New Space",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.012064, -75.866097),
                            title = "Restaurant",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.839833),
                            title = "Lorem",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.019322, -75.817688),
                            title = "Ipsum",
                            groupBy = GroupBy(filter = "Four"),
                        ),

                        )
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
                        )
                    ) {
                        LazyColumn() {
                            items(1000) {
                                Text(text = "Content -> ")
                            }
                        }
                    }
                }
            }
        }
    }
}
