package com.orlandev.gmaplib.model

import android.graphics.Bitmap

data class GroupBy(
    val filter: String,
    val icon: Bitmap? = null,
)

data class MapPlaceInfo(
    val uuid: String,
    val location: MapPoint,
    val title: String = "",
    val icon: Bitmap? = null,
    val groupBy: GroupBy? = null,
)