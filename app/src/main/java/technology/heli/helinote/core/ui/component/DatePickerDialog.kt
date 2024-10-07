package technology.heli.helinote.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = Calendar.getInstance()
    val state = rememberDatePickerState(
        initialSelectedDateMillis = currentTime.timeInMillis,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Select Date")
        },
        text = {
            DatePicker(
                state = state,
                modifier = modifier
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(state.selectedDateMillis)
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