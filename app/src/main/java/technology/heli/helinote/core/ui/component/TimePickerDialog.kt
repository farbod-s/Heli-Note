package technology.heli.helinote.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Calendar

@ExperimentalMaterial3Api
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = Calendar.getInstance()
    val state = rememberTimePickerState(
        initialHour = currentTime[Calendar.HOUR_OF_DAY],
        initialMinute = currentTime[Calendar.MINUTE],
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Time")
        },
        text = {
            TimePicker(
                state = state,
                modifier = modifier
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(
                        currentTime.apply {
                            set(Calendar.HOUR_OF_DAY, state.hour)
                            set(Calendar.MINUTE, state.minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }.timeInMillis
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}