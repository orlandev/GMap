package com.orlandev.gmaplib.utils

import com.orlandev.gmaplib.model.MapPlaceInfo

sealed class MapCardButtonsEvent() {
    data class OnShareEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
    data class OnSeeMoreEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
    data class OnGoUrlEvent(val mapPlaceInfo: MapPlaceInfo) : MapCardButtonsEvent()
}