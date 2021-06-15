package com.example.composetest

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetest.ui.theme.ComposeTestTheme
import com.example.composetest.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // extension function
            ComposeTestTheme { // this function is from Theme.kt
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    DefaultPreview()
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
        // fillMaxSize will make the screen as big as the parent's size.
        // In this case, the parent's size is the size of screen.
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally, // horizontal alignment
            //verticalArrangement = Arrangement.Bottom, // vertical alignment
        ) {
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    //.height(50.dp)
                    .weight(3f), // take place by 3/4
                color = MaterialTheme.colors.secondary
            ) {}
            Surface(
                modifier = Modifier
                    .width(200.dp)
                    //.height(50.dp)
                    .weight(1f),// take place by 1/4
                color = MaterialTheme.colors.primary
            ) {}
        }
    }
}