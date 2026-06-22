package com.qamarq.jellymusic.ui.screens

import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.CastMediaControlIntent
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.data.playback.JELLYMUSIC_CAST_RECEIVER_APP_ID
import com.qamarq.jellymusic.ui.motion.ElovaireMotion
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import com.qamarq.jellymusic.ui.theme.RoseAccent

/** Hoists "show the cast picker" so a single sheet instance renders at the app root, where it can
 * actually blur the real screen content behind it (a Dialog renders in its own Window and can't). */
internal class CastPickerController {
    var visible by mutableStateOf(false)
}

internal val LocalCastPickerController = compositionLocalOf<CastPickerController?> { null }

private fun castRouteSelector(): MediaRouteSelector = MediaRouteSelector.Builder()
    .addControlCategory(CastMediaControlIntent.categoryForCast(JELLYMUSIC_CAST_RECEIVER_APP_ID))
    .build()

@Composable
internal fun rememberCastRoutesState(): CastRoutesState {
    val context = LocalContext.current
    var routes by remember { mutableStateOf(emptyList<MediaRouter.RouteInfo>()) }
    var selectedRouteId by remember { mutableStateOf<String?>(null) }
    var selectedConnectionState by remember { mutableStateOf(MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED) }
    var volumeRefreshTick by remember { mutableStateOf(0) }
    DisposableEffect(context) {
        val mediaRouter = MediaRouter.getInstance(context)
        val selector = castRouteSelector()

        fun refresh() {
            routes = mediaRouter.routes.filter { !it.isDefault && it.matchesSelector(selector) }
            val selectedRoute = mediaRouter.selectedRoute.takeIf { !it.isDefault }
            selectedRouteId = selectedRoute?.id
            selectedConnectionState = selectedRoute?.connectionState
                ?: MediaRouter.RouteInfo.CONNECTION_STATE_DISCONNECTED
            volumeRefreshTick++
        }

        val callback = object : MediaRouter.Callback() {
            override fun onRouteAdded(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteRemoved(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteChanged(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
            override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo, reason: Int) = refresh()
            override fun onRouteUnselected(router: MediaRouter, route: MediaRouter.RouteInfo, reason: Int) = refresh()
            override fun onRouteVolumeChanged(router: MediaRouter, route: MediaRouter.RouteInfo) = refresh()
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
        volumeRefreshTick = volumeRefreshTick,
    )
}

internal data class CastRoutesState(
    val routes: List<MediaRouter.RouteInfo>,
    val selectedRouteId: String?,
    val selectedConnectionState: Int,
    val volumeRefreshTick: Int = 0,
) {
    val isCasting: Boolean get() = selectedRouteId != null
    // Treat anything short of fully CONNECTED as still connecting, rather than only the
    // CONNECTING value - some providers briefly report other transitional states (or skip
    // straight from DISCONNECTED), and we don't want a premature "Connected" label.
    val isConnecting: Boolean get() = isCasting && selectedConnectionState != MediaRouter.RouteInfo.CONNECTION_STATE_CONNECTED
    val selectedRoute: MediaRouter.RouteInfo? get() = routes.firstOrNull { it.id == selectedRouteId }
}

private val BLUETOOTH_AUDIO_DEVICE_TYPES = setOf(
    AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
    AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
)

/** The currently connected Bluetooth audio output device's name, if any - mirrors what the
 * system output switcher shows so the cast picker can offer it as a local playback target too. */
@Composable
internal fun rememberConnectedBluetoothAudioDeviceName(): String? {
    val context = LocalContext.current
    var deviceName by remember { mutableStateOf<String?>(null) }
    DisposableEffect(context) {
        val audioManager = context.getSystemService(AudioManager::class.java)

        fun refresh() {
            deviceName = audioManager
                ?.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
                ?.firstOrNull { it.type in BLUETOOTH_AUDIO_DEVICE_TYPES }
                ?.productName
                ?.toString()
        }

        val callback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>) = refresh()
            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>) = refresh()
        }
        audioManager?.registerAudioDeviceCallback(callback, null)
        refresh()
        onDispose {
            audioManager?.unregisterAudioDeviceCallback(callback)
        }
    }
    return deviceName
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
    val bluetoothDeviceName = rememberConnectedBluetoothAudioDeviceName()
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
                .background(Color.Black.copy(alpha = 0.42f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { controller.visible = false },
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            AnimatedVisibility(
                visible = controller.visible,
                enter = fadeIn(animationSpec = ElovaireMotion.contentFadeInSpec()) +
                    slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = ElovaireMotion.offsetSoft(durationMillis = ElovaireMotion.Standard),
                    ),
                exit = fadeOut(animationSpec = tween(durationMillis = 150)) +
                    slideOutVertically(
                        targetOffsetY = { it / 6 },
                        animationSpec = tween(durationMillis = 150),
                    ),
            ) {
                DynamicBackdropSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    overlayAlpha = 0.6f,
                    borderColor = blurSurfaceBorderColor(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(bottom = 12.dp),
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CastPickerIconButton(
                                iconRes = R.drawable.ic_lucide_x,
                                contentDescription = "Close",
                                onClick = { controller.visible = false },
                            )
                            Text(
                                text = "Cast to device",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                            CastPickerIconButton(
                                iconRes = R.drawable.ic_lucide_info,
                                contentDescription = "Help",
                                onClick = {},
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        DividerLine()
                        Spacer(modifier = Modifier.height(16.dp))

                        val connectedRoute = castState.selectedRoute
                        val availableRoutes = castState.routes.filter { it.id != castState.selectedRouteId }
                        val localSourceName = bluetoothDeviceName ?: "This phone"
                        val localSourceIconRes = if (bluetoothDeviceName != null) {
                            R.drawable.ic_lucide_headphones
                        } else {
                            R.drawable.ic_lucide_smartphone
                        }
                        val localSourceSubtitle = if (bluetoothDeviceName != null) "Bluetooth" else "Built-in speaker"

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                        ) {
                            if (connectedRoute != null) {
                                // Read fresh on every recomposition (driven by
                                // castState.volumeRefreshTick via onRouteVolumeChanged) rather than
                                // caching it keyed off the route object - MediaRouter mutates the
                                // same RouteInfo instance in place, so a remember() keyed on it
                                // would never see external volume changes (TV remote, phone, etc.)
                                // as a "changed" key and would go stale.
                                val volumeFraction = if (connectedRoute.volumeMax > 0) {
                                    connectedRoute.volume.toFloat() / connectedRoute.volumeMax.toFloat()
                                } else {
                                    0f
                                }
                                ConnectedCastDeviceCard(
                                    route = connectedRoute,
                                    isConnecting = castState.isConnecting,
                                    volumeFraction = volumeFraction,
                                    onVolumeChange = { fraction ->
                                        val newVolume = (fraction * connectedRoute.volumeMax)
                                            .toInt()
                                            .coerceIn(0, connectedRoute.volumeMax)
                                        connectedRoute.requestSetVolume(newVolume)
                                    },
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            } else {
                                ConnectedLocalSourceCard(
                                    name = localSourceName,
                                    iconRes = localSourceIconRes,
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            if (connectedRoute != null || availableRoutes.isNotEmpty()) {
                                Text(
                                    text = "AVAILABLE DEVICES",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = readableSecondaryTextColor(),
                                    modifier = Modifier.padding(bottom = 4.dp),
                                )
                                if (connectedRoute != null) {
                                    AvailableLocalSourceRow(
                                        name = localSourceName,
                                        subtitle = localSourceSubtitle,
                                        iconRes = localSourceIconRes,
                                        onClick = {
                                            MediaRouter.getInstance(context)
                                                .unselect(MediaRouter.UNSELECT_REASON_STOPPED)
                                        },
                                    )
                                }
                                availableRoutes.forEach { route ->
                                    AvailableCastDeviceRow(
                                        route = route,
                                        onClick = { route.select() },
                                    )
                                }
                            } else {
                                Text(
                                    text = "No devices found on your network. Make sure your phone and your device (TV/speaker) are on the same Wi-Fi network.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = readableSecondaryTextColor(),
                                    textAlign = TextAlign.Start,
                                )
                            }

                            if (castState.isCasting) {
                                Spacer(modifier = Modifier.height(20.dp))
                                StopCastingButton(
                                    onClick = {
                                        MediaRouter.getInstance(context)
                                            .unselect(MediaRouter.UNSELECT_REASON_STOPPED)
                                        controller.visible = false
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CastPickerIconButton(
    iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp),
        )
    }
}

private fun castDeviceIconRes(route: MediaRouter.RouteInfo): Int {
    return when (route.deviceType) {
        MediaRouter.RouteInfo.DEVICE_TYPE_TV -> R.drawable.ic_lucide_tv
        MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER -> R.drawable.ic_lucide_speaker
        MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH_A2DP -> R.drawable.ic_lucide_cast
        else -> R.drawable.ic_lucide_cast
    }
}

private fun castDeviceTypeLabel(route: MediaRouter.RouteInfo): String {
    return when (route.deviceType) {
        MediaRouter.RouteInfo.DEVICE_TYPE_TV -> "TV"
        MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER -> "Wi-Fi speaker"
        MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH_A2DP -> "Bluetooth"
        else -> "Wi-Fi device"
    }
}

@Composable
private fun ConnectedCastDeviceCard(
    route: MediaRouter.RouteInfo,
    isConnecting: Boolean,
    volumeFraction: Float,
    onVolumeChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.card))
            .background(RoseAccent.copy(alpha = 0.16f))
            .padding(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(RoseAccent.copy(alpha = 0.24f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = castDeviceIconRes(route)),
                    contentDescription = null,
                    tint = RoseAccent,
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (isConnecting) "Connecting..." else "Connected",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isConnecting) readableSecondaryTextColor() else RoseAccent,
                )
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(RoseAccent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lucide_check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_volume_x),
                contentDescription = null,
                tint = readableSecondaryTextColor(),
                modifier = Modifier.size(16.dp),
            )
            ThinContinuousSlider(
                value = volumeFraction,
                onValueChange = onVolumeChange,
                valueRange = 0f..1f,
                lineThickness = 4.dp,
                knobSize = 18.dp,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_volume_2),
                contentDescription = null,
                tint = readableSecondaryTextColor(),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun ConnectedLocalSourceCard(
    name: String,
    iconRes: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.card))
            .background(RoseAccent.copy(alpha = 0.16f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(RoseAccent.copy(alpha = 0.24f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = RoseAccent,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Connected",
                style = MaterialTheme.typography.labelLarge,
                color = RoseAccent,
            )
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(RoseAccent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lucide_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@Composable
private fun AvailableLocalSourceRow(
    name: String,
    subtitle: String,
    iconRes: Int,
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
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = readableSecondaryTextColor(),
            )
        }
        Canvas(modifier = Modifier.size(20.dp)) {
            drawCircle(
                color = Color.Gray.copy(alpha = 0.5f),
                radius = size.minDimension / 2f - 1.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx()),
            )
        }
    }
}

@Composable
private fun AvailableCastDeviceRow(
    route: MediaRouter.RouteInfo,
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
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = castDeviceIconRes(route)),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = castDeviceTypeLabel(route),
                style = MaterialTheme.typography.labelLarge,
                color = readableSecondaryTextColor(),
            )
        }
        Canvas(modifier = Modifier.size(20.dp)) {
            drawCircle(
                color = Color.Gray.copy(alpha = 0.5f),
                radius = size.minDimension / 2f - 1.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx()),
            )
        }
    }
}

@Composable
private fun StopCastingButton(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ElovaireRadii.pill))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                shape = RoundedCornerShape(ElovaireRadii.pill),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_lucide_circle_stop),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = "Stop casting",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.error,
        )
    }
}
