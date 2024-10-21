package com.amartyasingh.instagramstoryplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current


    //TODO: Sample list of story URLs (replace with actual URLs)
    val stories = listOf(
        "https://example.com/story1.mp4",
        "https://example.com/story2.mp4",
        "https://example.com/story3.mp4"
    )

    Box(Modifier.fillMaxSize()){
        Button(
            onClick = {
                val intent = Intent(context, StoryPlayerActivity::class.java)
                intent.putStringArrayListExtra("stories", ArrayList(stories))
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp).align(Alignment.Center)
        ) {
            Text(text = "Play Stories")
        }
    }
}