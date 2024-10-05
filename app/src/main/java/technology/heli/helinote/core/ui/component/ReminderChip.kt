package technology.heli.helinote.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import technology.heli.helinote.core.domain.model.Reminder

@Composable
fun ReminderChip(reminder: Reminder, onRemoveReminder: ((Long) -> Unit)? = null) {
    AssistChip(
        onClick = {},
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Reminder Icon",
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = reminder.formatAsReadableDateTime(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        },
        trailingIcon = {
            onRemoveReminder?.let {
                IconButton(onClick = { it.invoke(reminder.id) }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove Reminder")
                }
            }
        },
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .heightIn(min = 48.dp, max = 56.dp)
            .wrapContentSize()
    )
}