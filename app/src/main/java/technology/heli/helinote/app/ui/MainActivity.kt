package technology.heli.helinote.app.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import technology.heli.helinote.core.ui.theme.HeliNoteTheme
import technology.heli.helinote.feature.addedit.AddEditNoteScreen
import technology.heli.helinote.feature.list.NotesScreen
import technology.heli.helinote.feature.navigation.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HeliNoteTheme {
                AppNavHost(
                    onNavigateToExactAlarmSettings = { navigateToExactAlarmSettings() },
                    onNavigateToNotificationSettings = { navigateToNotificationSettings() }
                )
            }
        }
    }

    private fun navigateToExactAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent().apply { action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM }
                .also { startActivity(it) }
        }
    }

    private fun navigateToNotificationSettings() {
        Intent().apply {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }

                else -> {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", packageName)
                    putExtra("app_uid", applicationInfo.uid)
                }
            }
        }.also { startActivity(it) }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    onNavigateToExactAlarmSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.NoteListScreen.route) {
        composable(route = Screen.NoteListScreen.route) {
            NotesScreen(navController = navController)
        }
        composable(
            route = Screen.NoteAddEditScreen.route,
            arguments = listOf(
                navArgument(name = "noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            AddEditNoteScreen(
                navController = navController,
                noteId = noteId,
                onNavigateToExactAlarmSettings = { onNavigateToExactAlarmSettings() },
                onNavigateToNotificationSettings = { onNavigateToNotificationSettings() }
            )
        }
    }
}