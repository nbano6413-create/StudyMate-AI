package com.mo.sh.studyassistant.presentation

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mo.sh.studyassistant.data.repository.DataStoreRepository
import com.mo.sh.studyassistant.presentation.main.MainScreen
import com.mo.sh.studyassistant.presentation.questions.QuestionsScreen
import com.mo.sh.studyassistant.presentation.settings.SettingsScreen
import com.mo.sh.studyassistant.presentation.summarize.SummarizeScreen
import com.mo.sh.studyassistant.presentation.tutor.TutorScreen
import com.mo.sh.studyassistant.presentation.writer.WriterScreen
import com.mo.sh.studyassistant.ui.theme.StudyAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val theme by viewModel.get(
                intPreferencesKey(DataStoreRepository.THEME),
                DataStoreRepository.THEME_SYSTEM
            ).collectAsState(DataStoreRepository.THEME_SYSTEM)

            StudyAssistantTheme(
                darkTheme = when (theme) {
                    DataStoreRepository.THEME_LIGHT -> false
                    DataStoreRepository.THEME_DARK -> true
                    else -> androidx.compose.foundation.isSystemInDarkTheme()
                }
            ) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Main.route
                ) {
                    composable(Screen.Main.route) {
                        MainScreen(navController = navController)
                    }
                    composable(
                        route = Screen.Tutor.route,
                        arguments = listOf(
                            navArgument("imageUri") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            },
                            navArgument("text") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val imageUriString = backStackEntry.arguments?.getString("imageUri")
                        val text = backStackEntry.arguments?.getString("text")
                        TutorScreen(
                            sharedImageUri = imageUriString?.toUri(),
                            sharedText = text,
                            viewModel = viewModel
                        )
                    }
                    composable(
                        route = Screen.Summarize.route,
                        arguments = listOf(
                            navArgument("pdfUri") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val pdfUriString = backStackEntry.arguments?.getString("pdfUri")
                        SummarizeScreen(
                            sharedPdfUri = pdfUriString?.toUri(),
                            viewModel = viewModel
                        )
                    }
                    composable(Screen.Writer.route) {
                        WriterScreen(viewModel = viewModel)
                    }
                    composable(Screen.Questions.route) {
                        QuestionsScreen(viewModel = viewModel)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
