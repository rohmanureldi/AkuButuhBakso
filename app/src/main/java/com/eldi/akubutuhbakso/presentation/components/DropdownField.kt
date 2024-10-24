package com.eldi.akubutuhbakso.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.ui.theme.Paddings
import com.eldi.akubutuhbakso.ui.theme.textBlack
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DropdownField(
    label: String,
    options: ImmutableList<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Paddings.small),
            color = textBlack,
            text = label,
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { /* Read only */ },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Filled.KeyboardArrowUp
                        } else
                            Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            stringResource(R.string.label_collapse_dropdown)
                        } else {
                            stringResource(R.string.label_expand_dropdown)
                        },
                    )
                },
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Red,
                    unfocusedIndicatorColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Surface(
                modifier = Modifier
                    .height(OutlinedTextFieldDefaults.MinHeight)
                    .fillMaxWidth(),
                onClick = { expanded = true },
                color = Color.Transparent,
            ) { /* Do Nothing */ }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.medium),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
