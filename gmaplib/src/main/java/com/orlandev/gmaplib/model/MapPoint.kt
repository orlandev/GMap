package com.orlandev.gmaplib.model

data class MapPoint(
    val latitude: Double,
    val longitude: Double,
) {
    override fun toString(): String {
        return "Lat: $latitude --- LON: $longitude"
    }
}

