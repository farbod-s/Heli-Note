package technology.heli.helinote.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerButton(text: String, onClick: () -> Unit) {
    TextField(
        value = text,
        onValueChange = {},
        enabled = false,
        readOnly = true,
        singleLine = true,
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
        },
        colors = TextFieldDefaults.colors(
            disabledContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            disabledIndicatorColor = Color.LightGray,
            disabledTrailingIconColor = Color.DarkGray,
            focusedTrailingIconColor = Color.DarkGray,
            unfocusedTrailingIconColor = Color.DarkGray,
            disabledTextColor = Color.Black,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
}