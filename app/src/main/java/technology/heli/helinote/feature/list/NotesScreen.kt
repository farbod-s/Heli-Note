package technology.heli.helinote.feature.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import technology.heli.helinote.core.ui.component.CircularFloatingActionButton
import technology.heli.helinote.core.ui.component.NoteItem
import technology.heli.helinote.core.ui.component.OutlinedIconButton
import technology.heli.helinote.core.ui.component.SearchTextField
import technology.heli.helinote.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Recent Notes") },
                actions = {
                    OutlinedIconButton(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "Toggle Layout",
                        onClick = { viewModel.submitAction(NotesAction.OnLayoutToggled) }
                    )
                }
            )
        },
        floatingActionButton = {
            CircularFloatingActionButton(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note",
                onClick = {
                    navController.navigate(Screen.NoteAddEditScreen.createRoute())
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchTextField(
                query = searchQuery,
                onQueryChanged = { viewModel.submitAction(NotesAction.OnSearch(it)) }
            )
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(8.dp),
                columns = StaggeredGridCells.Fixed(state.cells),
                content = {
                    items(count = state.notes.count()) { index ->
                        state.notes[index].let { note ->
                            NoteItem(
                                note = note,
                                onClick = {
                                    navController.navigate(Screen.NoteAddEditScreen.createRoute(note.id))
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}