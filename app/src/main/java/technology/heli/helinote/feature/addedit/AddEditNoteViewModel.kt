package technology.heli.helinote.feature.addedit

import android.Manifest
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import technology.heli.helinote.core.database.store.PreferencesDataStore
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.model.RepeatType
import technology.heli.helinote.core.domain.model.exception.ExactAlarmPermissionRequiredException
import technology.heli.helinote.core.domain.model.exception.InvalidNoteException
import technology.heli.helinote.core.domain.model.exception.PostNotificationPermissionNotGrantedException
import technology.heli.helinote.core.domain.usecase.GetNoteUseCase
import technology.heli.helinote.core.domain.usecase.UpdateNoteUseCase
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val preferences: PreferencesDataStore,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditNoteState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AddEditNoteUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var getNoteJob: Job? = null
    private var saveNoteJob: Job? = null

    private var currentNoteId: Long = 0L
    private val remindersToAdd: MutableList<Reminder> = mutableListOf()
    private val remindersToRemove: MutableList<Reminder> = mutableListOf()

    init {
        savedStateHandle.get<Long>("noteId")?.let { noteId ->
            if (noteId > 0) {
                currentNoteId = noteId
                getNote(noteId)
            }
        }
    }

    fun submitAction(action: AddEditNoteAction) {
        when (action) {
            is AddEditNoteAction.OnSaveClicked -> saveNote()
            is AddEditNoteAction.OnTitleChanged -> updateNoteTitle(action.newValue)
            is AddEditNoteAction.OnContentChanged -> updateNoteContent(action.newValue)
            is AddEditNoteAction.OnReminderAdded -> addReminder(
                date = action.date,
                time = action.time,
                repeatType = action.repeatType
            )

            is AddEditNoteAction.OnReminderRemoved -> removeReminder(action.reminderId)
            is AddEditNoteAction.OnNotificationPermissionRejected -> notificationPermissionRejected()
            is AddEditNoteAction.OnNotificationPermissionAccepted -> notificationPermissionAccepted()
            is AddEditNoteAction.OnContentFocusChanged -> contentFocusChanged(action.focused)
            is AddEditNoteAction.OnTitleFocusChanged -> titleFocusChanged(action.focused)
        }
    }

    private fun getNote(id: Long) {
        getNoteJob?.cancel()
        getNoteJob = viewModelScope.launch {
            getNoteUseCase(id).mapNotNull { it }.collect { note ->
                _state.value = _state.value.copy(
                    title = note.title,
                    content = note.content,
                    isTitleHintVisible = false,
                    isContentHintVisible = false,
                    reminders = note.reminders
                        .plus(remindersToAdd)
                        .minus(remindersToRemove)
                )
            }
        }
    }

    private fun saveNote() {
        saveNoteJob?.cancel()
        saveNoteJob = viewModelScope.launch {
            try {
                updateNoteUseCase(
                    note = Note(
                        id = currentNoteId,
                        title = _state.value.title,
                        content = _state.value.content,
                        reminders = _state.value.reminders
                    ),
                    remindersToAdd = remindersToAdd,
                    remindersToRemove = remindersToRemove
                )
                remindersToAdd.clear()
                remindersToRemove.clear()
                _uiEvent.emit(AddEditNoteUiEvent.NoteSaved)
            } catch (exception: InvalidNoteException) {
                _uiEvent.emit(
                    AddEditNoteUiEvent.Error(
                        message = exception.message ?: "Couldn't save the note."
                    )
                )
            } catch (exception: ExactAlarmPermissionRequiredException) {
                _uiEvent.emit(
                    AddEditNoteUiEvent.Error(
                        message = exception.message ?: "Exact alarm permission required.",
                        action = UiEventAction.OpenExactAlarmSettings
                    )
                )
            } catch (exception: PostNotificationPermissionNotGrantedException) {
                val permissionAskedBefore =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        preferences.isPermissionAskedBefore(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        false
                    }
                val action =
                    if (permissionAskedBefore) {
                        UiEventAction.OpenNotificationSettings
                    } else {
                        UiEventAction.ShowPostNotificationPermission
                    }
                _uiEvent.emit(
                    AddEditNoteUiEvent.Error(
                        message = exception.message ?: "Notification permission is not granted.",
                        action = action
                    )
                )
            }
        }
    }

    private fun updateNoteTitle(newValue: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(title = newValue)
        }
    }

    private fun updateNoteContent(newValue: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(content = newValue)
        }
    }

    private fun addReminder(date: Long, time: Long, repeatType: RepeatType) {
        viewModelScope.launch {
            val dateTimeInMillis = getDateTimeInMillis(date = date, time = time)
            val reminder = Reminder(
                timestamp = dateTimeInMillis,
                repeatType = repeatType,
                noteId = currentNoteId
            )
            remindersToAdd.add(reminder)
            _state.value = _state.value.copy(
                reminders = _state.value.reminders.plus(reminder)
            )
        }
    }

    private fun removeReminder(reminderId: Long) {
        viewModelScope.launch {
            _state.value.reminders.firstOrNull { it.id == reminderId }?.let { reminder ->
                remindersToRemove.add(reminder)
                _state.value = _state.value.copy(
                    reminders = _state.value.reminders.minus(reminder)
                )
            }
        }
    }

    private fun getDateTimeInMillis(date: Long, time: Long): Long {
        // Calendar for date (year, month, day)
        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = date
        }

        // Calendar for time (hour, minute)
        val timeCalendar = Calendar.getInstance().apply {
            timeInMillis = time
        }

        // Merge two calendars
        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        dateCalendar.set(Calendar.SECOND, 0)
        dateCalendar.set(Calendar.MILLISECOND, 0)

        return dateCalendar.timeInMillis
    }

    private fun notificationPermissionRejected() {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                preferences.setPermissionAskedBefore(
                    permission = Manifest.permission.POST_NOTIFICATIONS,
                    value = true
                )
            }
        }
    }

    private fun notificationPermissionAccepted() {
        saveNote()
    }

    private fun titleFocusChanged(focused: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isTitleHintVisible = !focused && _state.value.title.isBlank()
            )
        }
    }

    private fun contentFocusChanged(focused: Boolean) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isContentHintVisible = !focused && _state.value.content.isBlank()
            )
        }
    }
}