package com.eldi.akubutuhbakso.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Paddings {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp

    fun topHorizontal(top: Dp, horizontal: Dp): PaddingValues {
        return PaddingValues(top = top, start = horizontal, end = horizontal)
    }

    fun topHorizontal(size: Dp): PaddingValues {
        return PaddingValues(top = size, start = size, end = size)
    }

    fun bottomHorizontal(bottom: Dp, horizontal: Dp): PaddingValues {
        return PaddingValues(bottom = bottom, start = horizontal, end = horizontal)
    }

    fun bottomHorizontal(size: Dp): PaddingValues {
        return PaddingValues(bottom = size, start = size, end = size)
    }

    fun startVertical(start: Dp, vertical: Dp): PaddingValues {
        return PaddingValues(bottom = vertical, start = start, top = vertical)
    }

    fun startVertical(size: Dp): PaddingValues {
        return PaddingValues(bottom = size, start = size, top = size)
    }

    fun endVertical(size: Dp): PaddingValues {
        return PaddingValues(bottom = size, end = size, top = size)
    }
}
