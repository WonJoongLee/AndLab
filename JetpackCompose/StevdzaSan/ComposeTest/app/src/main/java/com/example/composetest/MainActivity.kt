package com.example.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.composetest.ui.theme.ComposeTestTheme
import com.example.composetest.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // extension function
            ComposeTestTheme { // this function is from Theme.kt
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                }
            }
        }
    }
}

// When there is "Preview" annotation, it allow us to see composable inside the right split screen
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
    }
}