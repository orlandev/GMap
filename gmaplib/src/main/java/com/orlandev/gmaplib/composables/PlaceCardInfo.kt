package com.orlandev.gmaplib.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.orlandev.gmaplib.utils.MapCardButtonsEvent
import com.orlandev.gmaplib.R
import com.orlandev.gmaplib.model.MapPlaceInfo

@Composable
fun PlaceCardInfo(
    modifier: Modifier = Modifier,
    mapPlaceInfo: MapPlaceInfo? = null,
    onMapCardButtonsEvent: (MapCardButtonsEvent) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            mapPlaceInfo?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    painter = if (it.headerImage.isNotEmpty())
                        rememberImagePainter(data = it.headerImage)
                    else
                        painterResource(id = R.drawable.image_placeholder),
                    contentDescription = mapPlaceInfo.title
                )
            }
            Text(
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                text = mapPlaceInfo?.title ?: "Title",
                style = MaterialTheme.typography.body1
            )
            Text(
                maxLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                text = mapPlaceInfo?.description ?: "Description",
                style = MaterialTheme.typography.caption
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(ScrollState(0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(shape = RoundedCornerShape(50),
                    onClick = {
                        onMapCardButtonsEvent(MapCardButtonsEvent.OnSeeMoreEvent(mapPlaceInfo = mapPlaceInfo!!))
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_more_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.see_more).uppercase())
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(shape = RoundedCornerShape(50),
                    onClick = {
                        onMapCardButtonsEvent(MapCardButtonsEvent.OnShareEvent(mapPlaceInfo = mapPlaceInfo!!))
                    }) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.share_text).uppercase())
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(shape = RoundedCornerShape(50), onClick = {
                    onMapCardButtonsEvent(MapCardButtonsEvent.OnGoUrlEvent(mapPlaceInfo = mapPlaceInfo!!))
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_link_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.url_text).uppercase())
                }
            }
        }
    }
}