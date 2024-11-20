package com.eldi.akubutuhbakso.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    iconContent: @Composable () -> Unit = {},
    onCloseClick: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        modifier = modifier,
    ) {
        IconButton(
            onClick = onCloseClick,
        ) {
            iconContent()
        }
    }
}
