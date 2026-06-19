package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainApplicationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AcademicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val academicViewModel: AcademicViewModel = viewModel()
            val themePref by academicViewModel.themePreference.collectAsState()
            val isDark = when (themePref) {
                1 -> false
                2 -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            MyApplicationTheme(darkTheme = isDark) {
                MainApplicationScreen(viewModel = academicViewModel)
            }
        }
    }
}
