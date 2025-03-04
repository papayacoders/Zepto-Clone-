package com.example.zepto

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.zepto.ui.screens.ZeptoCloneApp
import com.example.zepto.ui.theme.ZeptoTheme
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.ui.input.nestedscroll.nestedScroll

import androidx.compose.ui.unit.IntOffset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set specific hardware acceleration settings
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

        // For 120Hz displays, optimize refresh rate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.preferredDisplayModeId =
                window.decorView.display?.supportedModes
                    ?.filter { it.refreshRate >= 120f }
                    ?.maxByOrNull { it.refreshRate }?.modeId ?: 0
        }

        // Enable edge-to-edge
        enableEdgeToEdge()

        setContent {
            ZeptoTheme {
                ZeptoCloneApp()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZeptoTheme {
ZeptoCloneApp()
    }
}