package com.eldi.akubutuhbakso.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.ui.theme.Paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseMapSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onCloseOk: () -> Unit,
    modifier: Modifier = Modifier,
    onCloseCancel: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.LightGray,
            )
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Paddings.medium),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_warning_face),
                contentDescription = null,
            )

            Text(
                text = stringResource(R.string.label_warning_map_close),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = onCloseOk,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Paddings.medium),
                shape = CircleShape,
            ) {
                Text(stringResource(R.string.button_ok))
            }

            OutlinedButton(
                onClick = onCloseCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Paddings.extraSmall),
                shape = CircleShape,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text("Batal")
            }
        }
    }
}
