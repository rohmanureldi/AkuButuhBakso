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
import com.eldi.akubutuhbakso.ui.theme.Paddings
import com.eldi.akubutuhbakso.ui.theme.textBlack

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    title: String = "Title placeholder",
    text: String = "",
    placeholder: String = "Placeholder",
    onTextChange: (String) -> Unit = {},
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(bottom = Paddings.small),
            color = textBlack,
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
                focusedIndicatorColor = Color.DarkGray,
                unfocusedIndicatorColor = Color.LightGray,
            ),
            isError = isError,
            supportingText = supportingText,
        )
    }
}
