package com.qamarq.jellymusic.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.CastMediaControlIntent
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import com.qamarq.jellymusic.ui.theme.RoseAccent
import com.qamarq.jellymusic.ui.theme.ToggleEnabledGreen

/** Hoists "show the cast picker" so a single sheet instance renders at the app root, where it can
 * actually blur the real screen content behind it (a Dialog renders in its own Window and can't). */
internal class CastPickerController {
    var visible by mutableStateOf(false)
}

internal val LocalCastPickerController = compositionLocalOf<CastPickerController?> { null }

private fun castRouteSelector(): MediaRouteSelector = MediaRouteSelector.Builder()
    .addControlCategory(CastMediaControlIntent.categoryForCast(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID))
    .build()

@Composable
internal fun rememberCastRoutesState(): CastRoutesState {
    val context = LocalContext.current
    var routes by remember { mutableStateOf(emptyList<MediaRouter.RouteInfo>()) }
    var selectedRouteId by remember { mutableStateOf<String?>(null) }
    var selectedConnectionState by remember { mutableStateOf(MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED) }
    DisposableEffect(context) {
        val mediaRouter = MediaRouter.getInstance(context)
        val selector = castRouteSelector()

        fun refresh() {
            routes = mediaRouter.routes.filter { !it.isDefault && it.matchesSelector(selector) }
            val selectedRoute = mediaRouter.selectedRoute.takeIf { !it.isDefault }
            selectedRouteId = selectedRoute?.id
            selectedConnectionState = selectedRoute?.connectionState
                ?: MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED
        }

        val callback = object : MediaRouter.Callback() {
            override fun onRouteAdded(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteRemoved(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteChanged(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo, reason: Int) = refresh()
            override fun onRouteUnselected(router: MediaRouter, route: MediaRouter.RouteInfo, reason: Int) = refresh()
        }
        mediaRouter.addCallback(selector, callback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY)
        refresh()
        onDispose {
            mediaRouter.removeCallback(callback)
        }
    }
    return CastRoutesState(
        routes = routes,
        selectedRouteId = selectedRouteId,
        selectedConnectionState = selectedConnectionState,
    )
}

internal data class CastRoutesState(
    val routes: List<MediaRouter.RouteInfo>,
    val selectedRouteId: String?,
    val selectedConnectionState: Int,
) {
    val isCasting: Boolean get() = selectedRouteId != null
    val isConnecting: Boolean get() = isCasting && selectedConnectionState == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING
}

@Composable
internal fun CastButton(
    modifier: Modifier = Modifier,
) {
    val controller = LocalCastPickerController.current
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { controller?.visible = true },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_lucide_cast),
            contentDescription = "Cast",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
internal fun CastDevicePickerOverlay(
    controller: CastPickerController,
) {
    val castState = rememberCastRoutesState()
    val context = LocalContext.current
    AnimatedVisibility(
        visible = controller.visible,
        modifier = Modifier.zIndex(30f),
        enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()),
        exit = fadeOut(animationSpec = ElovaireMotion.contentFadeOutSpec()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { controller.visible = false },
                ),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedVisibility(
                visible = controller.visible,
                enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                    ),
                exit = fadeOut(animationSpec = tween(durationMillis = 150)) +
                    scaleOut(
                        targetScale = 0.94f,
                        animationSpec = tween(durationMillis = 150),
                    ),
            ) {
                DynamicBackdropSurface(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .padding(vertical = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                    shape = RoundedCornerShape(ElovaireRadii.card),
                    overlayAlpha = 0.42f,
                    borderColor = blurSurfaceBorderColor(),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lucide_cast),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                text = "Cast",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (castState.routes.isEmpty()) {
                            Text(
                                text = "No devices found on your network. Make sure your phone and your device (TV/speaker) are on the same Wi-Fi network.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = readableSecondaryTextColor(),
                                textAlign = TextAlign.Start,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        } else {
                            castState.routes.forEach { route ->
                                CastDeviceRow(
                                    route = route,
                                    selected = route.id == castState.selectedRouteId,
                                    onClick = {
                                        route.select()
                                        controller.visible = false
                                    },
                                )
                            }
                        }
                        if (castState.isCasting) {
                            Spacer(modifier = Modifier.height(8.dp))
                            DividerLine()
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            MediaRouter.getInstance(context)
                                                .unselect(MediaRouter.UNSELECT_REASON_STOPPED)
                                            controller.visible = false
                                        },
                                    )
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lucide_x),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp),
                                )
                                Text(
                                    text = "Stop casting",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CastDeviceRow(
    route: MediaRouter.RouteInfo,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (selected) RoseAccent.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    id = if (selected && route.connectionState != MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING) {
                        R.drawable.ic_lucide_cast_filled
                    } else {
                        R.drawable.ic_lucide_cast
                    },
                ),
                contentDescription = null,
                tint = if (selected) RoseAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (selected) {
                Text(
                    text = if (route.connectionState == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING) "Connecting..." else "Connected",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (route.connectionState == MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTING) {
                        readableSecondaryTextColor()
                    } else {
                        ToggleEnabledGreen
                    },
                )
            }
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(ToggleEnabledGreen),
            )
        }
    }
}
