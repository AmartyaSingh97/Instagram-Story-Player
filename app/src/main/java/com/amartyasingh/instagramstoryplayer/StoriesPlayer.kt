package com.amartyasingh.instagramstoryplayer

import androidx.compose.runtime.mutableStateListOf
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.amartyasingh.instagramstoryplayer.model.Stories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StoriesPlayer(
    private val player: ExoPlayer,
    private var autoPlay: Boolean = true
) {
    private val _progress = mutableStateListOf<Float>() // Progress for each story
    private val internalStories = mutableListOf<Stories>()
    var storiesIndex = 0
    private var isProgressUpdating = false

    fun setData(stories: List<String>) {
        internalStories.clear()
        _progress.clear()
        stories.forEach {
            internalStories.add(Stories(it))
            _progress.add(0f) // Initialize progress for each story
        }
        showStory(storiesIndex)
    }

    private fun showStory(index: Int) {
        storiesIndex = index
        player.setMediaItem(MediaItem.fromUri(internalStories[index].url))
        player.prepare()
        player.playWhenReady = autoPlay
        resetProgress() // Reset progress when a new story starts
        startUpdatingProgress() // Start progress updates
    }

    private fun resetProgress() {
        _progress[storiesIndex] = 0f
    }

    private fun startUpdatingProgress() {
        isProgressUpdating = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isProgressUpdating && player.isPlaying) {
                val currentProgress = player.currentPosition.toFloat() / player.duration
                if (storiesIndex < _progress.size) {
                    _progress[storiesIndex] = currentProgress
                }
                delay(100) // Update every 100 milliseconds
            }
        }
    }

    private fun stopUpdatingProgress() {
        isProgressUpdating = false
    }

    fun getProgress(): List<Float> = _progress

    fun nextStory(): Boolean {
        return if (storiesIndex < internalStories.size - 1) {
            stopUpdatingProgress()
            showStory(storiesIndex + 1)
            true
        } else {
            false // No more stories
        }
    }

    fun previousStory() {
        if (storiesIndex > 0) {
            stopUpdatingProgress()
            showStory(storiesIndex - 1)
        }
    }
}

