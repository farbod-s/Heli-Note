package technology.heli.helinote.feature.addedit

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import technology.heli.helinote.core.ui.component.CircularFloatingActionButton
import technology.heli.helinote.core.ui.component.OutlinedIconButton
import technology.heli.helinote.core.ui.component.RemindersSection
import technology.heli.helinote.core.ui.component.TransparentHintTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteId: Long,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    onNavigateToExactAlarmSettings: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showReminderDialog by remember { mutableStateOf(false) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.submitAction(AddEditNoteAction.OnNotificationPermissionAccepted)
        } else {
            viewModel.submitAction(AddEditNoteAction.OnNotificationPermissionRejected)
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
                            is UiEventAction.OpenExactAlarmSettings -> {
                                onNavigateToExactAlarmSettings()
                            }

                            is UiEventAction.ShowPostNotificationPermission -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }

                            is UiEventAction.OpenNotificationSettings -> {
                                onNavigateToNotificationSettings()
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
                title = {},
                navigationIcon = {
                    OutlinedIconButton(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back",
                        onClick = { navController.popBackStack() }
                    )
                },
                actions = {
                    OutlinedIconButton(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Add Reminder",
                        onClick = { showReminderDialog = true }
                    )
                }
            )
        },
        floatingActionButton = {
            CircularFloatingActionButton(
                imageVector = Icons.Default.Check,
                contentDescription = "Save Note",
                onClick = {
                    viewModel.submitAction(AddEditNoteAction.OnSaveClicked)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = state.title,
                hint = "Title",
                onValueChange = {
                    viewModel.submitAction(AddEditNoteAction.OnTitleChanged(it))
                },
                onFocusChange = {
                    viewModel.submitAction(AddEditNoteAction.OnTitleFocusChanged(it.isFocused))
                },
                isHintVisible = state.isTitleHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = state.content,
                hint = "Note",
                onValueChange = {
                    viewModel.submitAction(AddEditNoteAction.OnContentChanged(it))
                },
                onFocusChange = {
                    viewModel.submitAction(AddEditNoteAction.OnContentFocusChanged(it.isFocused))
                },
                isHintVisible = state.isContentHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}