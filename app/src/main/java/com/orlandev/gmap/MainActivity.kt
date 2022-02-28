package com.orlandev.gmap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.orlandev.gmap.ui.theme.GMapTheme
import com.orlandev.gmaplib.MapCardButtonsEvent
import com.orlandev.gmaplib.MapPlaceInfo
import com.orlandev.gmaplib.MapPoint
import com.orlandev.gmaplib.MapScreen

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
                            title = "Primer Lugar",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.812710),
                            title = "Segund Lugar",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.009161, -75.839146),
                            title = "Title",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.043998, -75.829533),
                            title = "Title",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.027870, -75.852879),
                            title = "Title",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.012064, -75.866097),
                            title = "Title",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.839833),
                            title = "Title",
                            description = "Description"
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.019322, -75.817688),
                            title = "Title",
                            description = "Description"
                        ),

                        )
                    MapScreen(listOfMapPoints = listOfMapPoints) {
                        when (it) {
                            is MapCardButtonsEvent.OnGoUrlEvent -> {

                                Log.d("event", "open Url")
                            }
                            is MapCardButtonsEvent.OnSeeMoreEvent -> {

                                Log.d("event", "open place description")
                            }
                            is MapCardButtonsEvent.OnShareEvent -> {
                                Log.d("event", "Share event")
                            }
                        }
                    }
                }
            }
        }
    }
}
