package com.reza3320.oldschool

import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import io.supabase.SupabaseClient





class MainActivity : ComponentActivity() {

    // Declare SupabaseClient as a late-initialized variable
    private lateinit var supabaseClient: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Supabase client
        supabaseClient = SupabaseClient.create(
            url = "https://optcuklsrbgivtnqqnfv.supabase.co",  // Replace with your Supabase project URL
            apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9wdGN1a2xzcmJnaXZ0bnFxbmZ2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg3ODcyODAsImV4cCI6MjA0NDM2MzI4MH0.Z-yO_qyyT-WmcqvPXG_byiXj_60QFVE7N_Ockghefi4"  // Replace with your public API key
        )

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