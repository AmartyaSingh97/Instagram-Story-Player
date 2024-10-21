package com.amartyasingh.instagramstoryplayer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.amartyasingh.instagramstoryplayer.screens.StoryPlayerScreen

class StoryPlayerActivity: ComponentActivity() {
    private lateinit var player: ExoPlayer
    private var isMuted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stories = intent.getStringArrayListExtra("stories") ?: emptyList<String>()

        player = ExoPlayer.Builder(this).build()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        Log.d("StoryPlayer", "Player is ready to play")
                    }
                    Player.STATE_BUFFERING -> {
                        Log.d("StoryPlayer", "Player is buffering")
                    }
                    Player.STATE_ENDED -> {
                        Log.d("StoryPlayer", "Playback ended")
                    }
                    Player.STATE_IDLE -> {
                        Log.d("StoryPlayer", "Player is idle")
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("StoryPlayer", "Error occurred: ${error.message}")
                Toast.makeText(this@StoryPlayerActivity, "Error loading video", Toast.LENGTH_LONG).show()
            }
        })


        setContent {
            StoryPlayerScreen(stories, player, isMuted, onMuteToggle = {
                isMuted = !isMuted
                player.volume = if (isMuted) 0f else 1f
            }) {
                finish()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}