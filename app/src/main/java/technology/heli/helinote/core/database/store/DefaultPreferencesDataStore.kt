package technology.heli.helinote.core.database.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class DefaultPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataStore {

    companion object {
        const val KEY_PERMISSION_PREFIX = "permissions_"
        val KEY_GRID_CELLS = intPreferencesKey("notes_staggered_grid_cells")
    }

    override suspend fun isPermissionAskedBefore(permission: String): Boolean =
        dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey("${KEY_PERMISSION_PREFIX}_$permission")]
            }.firstOrNull() ?: false

    override suspend fun setPermissionAskedBefore(permission: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("${KEY_PERMISSION_PREFIX}_$permission")] = value
        }
    }

    override fun getNotesStaggeredGridCells(): Flow<Int> =
        dataStore.data
            .mapNotNull { preferences ->
                preferences[KEY_GRID_CELLS]
            }

    override suspend fun setNotesStaggeredGridCells(cells: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_GRID_CELLS] = cells
        }
    }
}