package com.all_here.appmakkie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.all_here.appmakkie.ui.theme.AppMakkieTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMakkieTheme {
               flashApp()

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppMakkieTheme {

        flashApp()

    }
}