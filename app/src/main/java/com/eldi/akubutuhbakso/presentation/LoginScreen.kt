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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eldi.akubutuhbakso.R
import com.eldi.akubutuhbakso.presentation.components.DropdownField
import com.eldi.akubutuhbakso.presentation.components.InputField
import com.eldi.akubutuhbakso.ui.theme.AkuButuhBaksoTheme
import com.eldi.akubutuhbakso.ui.theme.Borders
import com.eldi.akubutuhbakso.ui.theme.Paddings
import com.eldi.akubutuhbakso.ui.theme.textBlack
import com.eldi.akubutuhbakso.ui.theme.tselDarkBlueContainerLight
import kotlinx.collections.immutable.toPersistentList

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.drawable.ic_login),
            contentDescription = "LoginMascot",
        )

        RegistrationIntro(
            modifier = Modifier.fillMaxWidth(),
        )

        LoginForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.topHorizontal(Paddings.medium)),
        )
    }
}

@Composable
private fun RegistrationIntro(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
}

@Composable
private fun LoginForm(modifier: Modifier = Modifier) {
    val availableRoles = stringArrayResource(R.array.user_roles).toPersistentList()
    var selectedRole: String by remember {
        mutableStateOf(availableRoles[0])
    }
    var userName by remember {
        mutableStateOf("")
    }
    val onUsernameChange: (String) -> Unit = remember {
        {
            userName = it
        }
    }

    var isTncChecked by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Paddings.medium),
        border = Borders.default,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Paddings.medium,
                    vertical = Paddings.medium,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.label_name),
                placeholder = stringResource(R.string.label_input_name),
                onTextChange = onUsernameChange,
                text = userName,
            )

            Spacer(modifier = Modifier.height(Paddings.medium))

            DropdownField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.label_role),
                options = availableRoles,
                selectedOption = selectedRole,
                onOptionSelected = {
                    selectedRole = it
                },
            )

            Button(
                onClick = {
                    // TODO: To map activity
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Paddings.medium),
                shape = CircleShape,
                enabled = isTncChecked && userName.isNotBlank(),
            ) {
                Text(stringResource(R.string.label_join))
            }

            Spacer(modifier = Modifier.height(Paddings.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Checkbox(
                    modifier = Modifier,
                    checked = isTncChecked,
                    onCheckedChange = {
                        isTncChecked = it
                    },
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
