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
                    val description =
                        "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen."
                    val listOfMapPoints = listOf<MapPlaceInfo>(
                        MapPlaceInfo(
                            location = MapPoint(20.009806, -75.828503),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.812710),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "First"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.009161, -75.839146),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.043998, -75.829533),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Second"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.027870, -75.852879),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.012064, -75.866097),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.013677, -75.839833),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Third"),
                        ),
                        MapPlaceInfo(
                            location = MapPoint(20.019322, -75.817688),
                            title = "Lorem Ipsum",
                            groupBy = GroupBy(filter = "Four"),
                        ),

                        )
                    MapScreen(
                        listOfMapPoints = listOfMapPoints,
                        listOfFilters = listOf( "First", "Second", "Third", "Four","Six","Seven","Home","Other")
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
