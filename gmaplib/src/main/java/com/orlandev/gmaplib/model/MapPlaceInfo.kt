package com.orlandev.gmaplib.model

data class MapPlaceInfo(
    val location: MapPoint,
    val title: String = "",
    val description: String = "",
    val headerImage: String = "",
    val groupBy: String = "",
)