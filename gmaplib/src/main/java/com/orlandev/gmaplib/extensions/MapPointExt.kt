package com.orlandev.gmaplib.extensions

import com.google.android.gms.maps.model.LatLng
import com.orlandev.gmaplib.model.MapPoint

fun MapPoint.toLatLon(): LatLng {
    return LatLng(
        latitude,
        longitude
    )
}
