package com.example.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetest.ui.theme.ComposeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // extension function
            ComposeTestTheme { // this function is from Theme.kt
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Row(
                        modifier = Modifier
                            .height(500.dp)
                            .width(500.dp)
                            .background(Color.LightGray),
                        horizontalArrangement = Arrangement.Start,
                        //horizontalAlignment = Alignment.CenterHorizontally, // horizontal alignment
                        //verticalArrangement = Arrangement.Bottom, // vertical alignment
                    ) {
                        CustomItem(3f, color=MaterialTheme.colors.secondary)
                        CustomItem(1f)
                    }
                }
            }
        }
    }
}

// default color as primary color
@Composable
fun RowScope.CustomItem(weight: Float, color: Color = MaterialTheme.colors.primary) {
    Surface(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .weight(weight), // To add weight, ColumnScope에서 가져와서 CustomItem을 써야함. 내장함수로
        color = color
    ) {}
}

// When there is "Preview" annotation, it allow us to see composable inside the right split screen
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        // fillMaxSize will make the screen as big as the parent's size.
        // In this case, the parent's size is the size of screen.
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start, // horizontal alignment
            //verticalArrangement = Arrangement.Bottom, // vertical alignment
        ) {
            CustomItem(3f, color=MaterialTheme.colors.secondary)
            CustomItem(1f)
        }
    }
}