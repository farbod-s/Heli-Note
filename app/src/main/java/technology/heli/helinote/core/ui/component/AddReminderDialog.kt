package technology.heli.helinote.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import technology.heli.helinote.core.domain.model.RepeatType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onSave: (Long, Long, String) -> Unit,
    repeatTypes: List<String> = RepeatType.entries.map { it.name },
    currentTime: Calendar = Calendar.getInstance()
) {
    var spinnerExpanded by remember { mutableStateOf(false) }
    var selectedRepeatType by remember { mutableStateOf(repeatTypes[0]) }

    var selectedDate by remember { mutableLongStateOf(currentTime.timeInMillis) }
    var selectedTime by remember { mutableLongStateOf(currentTime.timeInMillis) }

    // To handle the DatePicker dialog visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { timestamp ->
                timestamp?.let { selectedDate = it }
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { timestamp ->
                selectedTime = timestamp
                showTimePicker = false
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Reminder") },
        text = {
            Column {
                // Date Picker
                Button(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Text("Select Date: ${formatSelectedDate(selectedDate)}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Time Picker
                Button(
                    onClick = {
                        showTimePicker = true
                    }
                ) {
                    Text("Select Time: ${formatSelectedTime(selectedTime)}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Spinner for Repeat Type
                ExposedDropdownMenuBox(
                    expanded = spinnerExpanded,
                    onExpandedChange = { spinnerExpanded = !spinnerExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedRepeatType,
                        onValueChange = { },
                        label = { Text("Repeat Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = spinnerExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .clickable { spinnerExpanded = true }
                    )
                    ExposedDropdownMenu(
                        expanded = spinnerExpanded,
                        onDismissRequest = { spinnerExpanded = false }
                    ) {
                        repeatTypes.forEach { repeat ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = repeat)
                                },
                                onClick = {
                                    selectedRepeatType = repeat
                                    spinnerExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(selectedDate, selectedTime, selectedRepeatType)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun formatSelectedDate(dateInMillis: Long): String {
    val formatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    return formatter.format(Date(dateInMillis))
}

fun formatSelectedTime(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date(timeInMillis))
}