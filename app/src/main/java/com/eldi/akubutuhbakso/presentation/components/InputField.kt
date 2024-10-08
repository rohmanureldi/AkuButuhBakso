package com.eldi.akubutuhbakso.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    title: String = "Title placeholder",
    text: String = "",
    placeholder: String = "Placeholder",
    onTextChange: (String) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            color = Color(0xFF001A41),
            text = title,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(placeholder, color = Color.LightGray)
            },
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.LightGray,
            ),
        )
    }
}
