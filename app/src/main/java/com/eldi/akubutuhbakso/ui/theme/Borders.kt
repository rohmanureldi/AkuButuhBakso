package com.eldi.akubutuhbakso.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Borders {
    val default = BorderStroke(width = 1.dp, color = Color(0x80FFFFFF))
    fun custom(
        width: Dp = 1.dp,
        color: Color = Color(0x80FFFFFF),
    ): BorderStroke {
        return BorderStroke(width, color)
    }
}
