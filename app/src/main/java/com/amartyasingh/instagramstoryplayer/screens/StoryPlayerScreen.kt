package com.amartyasingh.instagramstoryplayer.screens

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.amartyasingh.instagramstoryplayer.StoriesPlayer

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StoryPlayerScreen(
    stories: List<String>,
    player: ExoPlayer,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    LocalLifecycleOwner.current
    val playerView = remember { PlayerView(context) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val segmentedVideoPlayer = remember { StoriesPlayer(player) }

    DisposableEffect(Unit) {
        playerView.player = player
        playerView.useController = false
        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        player.prepare()
        onDispose {
            player.release()
        }
    }

    LaunchedEffect(stories) {
        segmentedVideoPlayer.setData(stories)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    val screenWidth = size.width
                    if (offset.x < screenWidth / 2) {
                        // Rewind to previous story
                        if (currentIndex > 0) {
                            currentIndex -= 1
                            segmentedVideoPlayer.previousStory()
                        }
                    } else {
                        // Forward to the next story
                        if (segmentedVideoPlayer.nextStory()) {
                            currentIndex += 1
                        } else {
                            onClose() // Close the activity when the last story is done
                        }
                    }
                }
            )
        }
    ) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.fillMaxSize()
        )

        StoryProgressBar(stories, segmentedVideoPlayer)

        IconButton(
            onClick = onMuteToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Mute",
                tint = Color.White
            )
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp, 56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}


