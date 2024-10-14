package com.reza3320.oldschool

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.reza3320.oldschool.ui.theme.OldSchoolTheme
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import io.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    // Declare SupabaseClient and Agora engine as late-initialized variables
    private lateinit var supabaseClient: SupabaseClient
    private lateinit var agoraEngine: RtcEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Supabase client
        supabaseClient = SupabaseClient.create(
            url = "https://optcuklsrbgivtnqqnfv.supabase.co",  // Replace with your Supabase project URL
            apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9wdGN1a2xzcmJnaXZ0bnFxbmZ2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg3ODcyODAsImV4cCI6MjA0NDM2MzI4MH0.Z-yO_qyyT-WmcqvPXG_byiXj_60QFVE7N_Ockghefi4"  // Replace with your public API key
        )

        // Initialize Agora engine
        initializeAgora()

        // Your existing Jetpack Compose UI setup
        enableEdgeToEdge()
        setContent {
            OldSchoolTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Example: Query data from Supabase after setting up the UI
        querySupabaseData() // Call this function after your UI is rendered

        // Join a video chat channel after matching (for demonstration purposes)
        startVideoChat("test-channel") // Replace with your actual channel
    }

    // Function to query data from Supabase
    private fun querySupabaseData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = supabaseClient
                .from("users") // Replace with your actual table name
                .select("*")
                .execute()

            withContext(Dispatchers.Main) {
                if (response.error != null) {
                    // Handle error (e.g., log it)
                    Log.e("SupabaseError", response.error.message)
                } else {
                    // Handle the retrieved data (e.g., log it or update the UI)
                    val data = response.data
                    Log.d("SupabaseData", data.toString())
                }
            }
        }
    }

    // Initialize Agora engine
    private fun initializeAgora() {
        try {
            agoraEngine = RtcEngine.create(baseContext, "f72f12adc94e40f0ab3fb254e289965d", null)  // Replace with your Agora App ID
            agoraEngine.setVideoEncoderConfiguration(
                VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VD_640x360,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Join a video chat channel
    private fun startVideoChat(channelName: String) {
        agoraEngine.joinChannel(null, channelName, "Optional Info", 0)
    }

    // Setup the local video feed
    private fun setupLocalVideo() {
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        agoraEngine.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
    }

    // Leave the channel (when ending the call)
    private fun leaveChannel() {
        agoraEngine.leaveChannel()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OldSchoolTheme {
        Greeting("Android")
    }
}
