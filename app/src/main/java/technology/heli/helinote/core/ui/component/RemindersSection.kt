package technology.heli.helinote.core.ui.component

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import technology.heli.helinote.core.domain.model.Reminder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RemindersSection(
    reminders: List<Reminder>,
    onRemoveReminder: ((Long) -> Unit)? = null
) {
    if (reminders.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            reminders.forEach { reminder ->
                ReminderChip(reminder = reminder, onRemoveReminder = onRemoveReminder)
            }
        }
    }
}