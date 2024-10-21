package com.amartyasingh.instagramstoryplayer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amartyasingh.instagramstoryplayer.StoriesPlayer

@Composable
fun StoryProgressBar(stories: List<String>, storiesPlayer: StoriesPlayer) {
    val progressList = storiesPlayer.getProgress()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        stories.forEachIndexed { index, _ ->
            LinearProgressIndicator(
                progress = {
                    if (index < progressList.size) progressList[index] else 0f // Ensure index is valid
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                color = if (index == storiesPlayer.storiesIndex) Color.White else Color.Gray, // Active story bar is white
                trackColor = Color.Gray, // Inactive story bars are gray
            )
        }
    }
}


