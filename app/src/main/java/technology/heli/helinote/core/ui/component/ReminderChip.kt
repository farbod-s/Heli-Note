package technology.heli.helinote.core.ui.component

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import technology.heli.helinote.core.domain.model.Reminder

@Composable
fun ReminderChip(reminder: Reminder, onRemoveReminder: ((Long) -> Unit)? = null) {
    AssistChip(
        onClick = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Reminder Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(14.dp)
            )
        },
        label = {
            Text(
                text = reminder.formatAsReadableDateTime(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingIcon = {
            onRemoveReminder?.let {
                IconButton(onClick = { it.invoke(reminder.id) }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove Reminder",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .heightIn(min = 32.dp, max = 32.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}