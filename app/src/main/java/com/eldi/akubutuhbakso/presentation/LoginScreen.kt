package com.eldi.akubutuhbakso.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.presentation.components.InputField
import com.eldi.akubutuhbakso.ui.theme.AkuButuhBaksoTheme
import com.eldi.akubutuhbakso.ui.theme.Borders
import com.eldi.akubutuhbakso.ui.theme.Paddings
import com.eldi.akubutuhbakso.ui.theme.textBlack
import com.eldi.akubutuhbakso.ui.theme.tselDarkBlueContainerLight

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.ic_login),
                contentDescription = "LoginMascot",
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.label_registration),
                    color = textBlack,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(Paddings.small))
                Text(stringResource(R.string.label_input_name_and_role), color = textBlack)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.topHorizontal(Paddings.medium)),
                shape = RoundedCornerShape(Paddings.medium),
                border = Borders.default,
            ) {
                LoginForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = Paddings.medium,
                            vertical = Paddings.medium,
                        ),
                )
            }
        }
    }
}

@Composable
private fun LoginForm(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.label_name),
            placeholder = stringResource(R.string.label_input_name),
        )
        Spacer(modifier = Modifier.height(Paddings.medium))
        InputField(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.label_role),
            placeholder = stringResource(R.string.label_input_role),
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Paddings.medium),
            shape = CircleShape,
        ) {
            Text(stringResource(R.string.label_join))
        }

        Spacer(modifier = Modifier.height(Paddings.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                modifier = Modifier,
                checked = true,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = tselDarkBlueContainerLight,
                ),
            )

            Text(
                text = stringResource(R.string.label_tnc_desc),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LoginPreview() {
    AkuButuhBaksoTheme {
        LoginScreen()
    }
}
