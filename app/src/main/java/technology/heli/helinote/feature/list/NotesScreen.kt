package technology.heli.helinote.feature.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import technology.heli.helinote.core.ui.component.CircularFloatingActionButton
import technology.heli.helinote.core.ui.component.NoteItem
import technology.heli.helinote.core.ui.component.SearchTextField
import technology.heli.helinote.feature.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Recent All Note") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.submitAction(NotesAction.OnLayoutToggled)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.List,
                            contentDescription = "Toggle Layout"
                        )
                    }
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
                onQueryChanged = { newQuery ->
                    searchQuery = newQuery
                    viewModel.submitAction(NotesAction.OnSearch(searchQuery))
                }
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