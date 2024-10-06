package technology.heli.helinote.core.database.store

import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    suspend fun isPermissionAskedBefore(permission: String): Boolean

    suspend fun setPermissionAskedBefore(permission: String, value: Boolean)

    fun getNotesStaggeredGridCells(): Flow<Int>

    suspend fun setNotesStaggeredGridCells(cells: Int)
}