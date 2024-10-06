package technology.heli.helinote.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import technology.heli.helinote.core.database.store.PreferencesDataStore
import technology.heli.helinote.core.domain.usecase.GetNotesUseCase
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val preferences: PreferencesDataStore,
) : ViewModel() {

    private val _state = MutableStateFlow(NotesState())
    val state = _state.asStateFlow()

    private var getNotesJob: Job? = null
    private var getConfigsJob: Job? = null

    private var isGridLayout = false

    init {
        getNotes()
        getConfigs()
    }

    fun submitAction(action: NotesAction) {
        when (action) {
            is NotesAction.OnLayoutToggled -> changeLayout()
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = viewModelScope.launch {
            getNotesUseCase().collect { notes ->
                _state.value = _state.value.copy(notes = notes)
            }
        }
    }

    private fun getConfigs() {
        getConfigsJob?.cancel()
        getConfigsJob = viewModelScope.launch {
            preferences.getNotesStaggeredGridCells().collect { cells ->
                _state.value = _state.value.copy(cells = cells)
                isGridLayout = cells > 1
            }
        }
    }

    private fun changeLayout() {
        viewModelScope.launch {
            isGridLayout = !isGridLayout
            val cells = if (isGridLayout) 2 else 1
            preferences.setNotesStaggeredGridCells(cells)
        }
    }
}