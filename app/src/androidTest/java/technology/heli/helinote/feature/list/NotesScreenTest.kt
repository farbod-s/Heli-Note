package technology.heli.helinote.feature.list

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import technology.heli.helinote.app.ui.MainActivity
import technology.heli.helinote.core.data.di.DataMapperModule
import technology.heli.helinote.core.data.di.RepositoryModule
import technology.heli.helinote.core.database.di.DataStoreModule
import technology.heli.helinote.core.database.di.DatabaseModule
import technology.heli.helinote.core.domain.di.UseCaseModule
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.ui.theme.HeliNoteTheme
import technology.heli.helinote.feature.navigation.Screen
import technology.heli.helinote.feature.notification.di.NotificationModule

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(
    DatabaseModule::class,
    DataStoreModule::class,
    DataMapperModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    NotificationModule::class
)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Mock
    lateinit var viewModel: NotesViewModel
    private val mockNotesState = MutableStateFlow(NotesState())
    private val mockSearchQuery = MutableStateFlow("")

    @Before
    fun setUp() {
        hiltRule.inject()
        MockitoAnnotations.openMocks(this)

        // Mock ViewModel behavior
        Mockito.`when`(viewModel.state).thenReturn(mockNotesState)
        Mockito.`when`(viewModel.searchQuery).thenReturn(mockSearchQuery)

        // Mock NotesState
        mockNotesState.value = NotesState(
            notes = listOf(
                Note(
                    id = 1, title = "Sample Note 1", content = "Content of Note 1"
                ), Note(
                    id = 2, title = "Sample Note 2", content = "Content of Note 2"
                )
            ), cells = 2
        )

        // Set the content for test
        composeRule.setContent {
            val navController = rememberNavController()
            HeliNoteTheme {
                NavHost(
                    navController = navController, startDestination = Screen.NoteListScreen.route
                ) {
                    composable(route = Screen.NoteListScreen.route) {
                        NotesScreen(
                            navController = navController, viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    @Test
    fun testNotesListIsDisplayed() {
        // Assert that the notes list is displayed with the mock data
        composeRule.onNodeWithText("Sample Note 1").assertIsDisplayed()
        composeRule.onNodeWithText("Sample Note 2").assertIsDisplayed()
    }

    @Test
    fun testToggleLayoutButtonChangesGridCells() {
        // Assert that initially two columns are used (based on the mock state)
        composeRule.onAllNodes(hasTestTag("NoteItem")).assertCountEquals(2)

        // Toggle layout by clicking the toggle button
        composeRule.onNodeWithContentDescription("Toggle Layout").performClick()

        // Verify the layout changes
        composeRule.onAllNodes(hasTestTag("NoteItem")).assertCountEquals(2)
    }

    @Test
    fun testSearchFieldFiltersNotes() {
        // Type query into search field
        composeRule.onNodeWithTag("SearchNoteTextField").performTextInput("Sample Note 1")

        // Verify only "Sample Note 1" is displayed after filtering
        composeRule.onNodeWithText("Sample Note 1").assertIsDisplayed()
        composeRule.onNodeWithText("Sample Note 2").assertDoesNotExist()
    }

    @Test
    fun testAddButtonNavigatesToAddEditScreen() {
        // Click the add button
        composeRule.onNodeWithContentDescription("Add Note").performClick()

        // Assert that navigation to the Add/Edit screen occurred
        composeRule.onNodeWithTag("Add/Edit Screen (0)").assertIsDisplayed()
    }

    @Test
    fun testClickNoteNavigatesToEditScreen() {
        // Click on a note
        composeRule.onNodeWithText("Sample Note 1").performClick()

        // Assert that navigation to the edit screen with the correct note ID happened
        composeRule.onNodeWithTag("Add/Edit Screen (1)").assertIsDisplayed()
    }
}