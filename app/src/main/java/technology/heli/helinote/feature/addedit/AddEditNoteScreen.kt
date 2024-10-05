package technology.heli.helinote.feature.addedit

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import technology.heli.helinote.core.ui.component.AddReminderDialog
import technology.heli.helinote.core.ui.component.RemindersSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteId: Long,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    onNavigateToExactAlarmSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showReminderDialog by remember { mutableStateOf(false) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.submitAction(AddEditNoteAction.OnSaveClicked)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AddEditNoteUiEvent.NoteSaved -> {
                    navController.popBackStack()
                }

                is AddEditNoteUiEvent.Error -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action?.label
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        when (event.action) {
                            is UiEventAction.OpenExactAlarmPermission -> {
                                onNavigateToExactAlarmSettings()
                            }

                            is UiEventAction.ShowPostNotificationPermission -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }

                            else -> Unit
                        }
                    }
                }

                else -> Unit
            }
        }
    }

    if (showReminderDialog) {
        AddReminderDialog(
            onDismiss = { showReminderDialog = false },
            onSave = { date, time, repeatType ->
                viewModel.submitAction(
                    AddEditNoteAction.OnReminderAdded(
                        date = date,
                        time = time,
                        repeatType = repeatType
                    )
                )
                showReminderDialog = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = if (noteId == 0L) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showReminderDialog = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Add Reminder")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.submitAction(AddEditNoteAction.OnSaveClicked)
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save Note")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            RemindersSection(
                reminders = state.reminders.toList(),
                onRemoveReminder = { reminderId ->
                    viewModel.submitAction(
                        AddEditNoteAction.OnReminderRemoved(
                            reminderId
                        )
                    )
                }
            )
            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    viewModel.submitAction(AddEditNoteAction.OnTitleChanged(it))
                },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = state.content,
                onValueChange = {
                    viewModel.submitAction(AddEditNoteAction.OnContentChanged(it))
                },
                label = { Text(text = "Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(1f)
            )
        }
    }
}