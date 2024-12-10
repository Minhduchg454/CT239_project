package com.example.inventory.ui.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.CardColorsCustom

object SettingsDestination : NavigationDestination {
    override val route = "Settings"
    override val titleRes = R.string.Settings
    override val icon = Icons.Outlined.Settings
    override val buttonText = R.string.Settings
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onUpdateDeleteBookClick : () -> Unit,
    onAddBookClick : () -> Unit,
    onUpdateDeleteAuthorClick: () -> Unit,
    onAddAuthorClick: () -> Unit,
    viewModel: infoScreenViewModel = viewModel()
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
                InventoryTopAppBar(
                    title = stringResource(SettingsDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
        },
    ) { innerPadding ->
        SettingsBody(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
            onUpdateDeleteBookClick = onUpdateDeleteBookClick,
            onAddBookClick = onAddBookClick,
            onUpdateDeleteAuthorClick = onUpdateDeleteAuthorClick,
            onAddAuthorClick = onAddAuthorClick,
            isAuthenticated = isAuthenticated,
            enterPassword = { viewModel.authenticate(it) }
        )
    }
}

@Composable
fun SettingsBody (
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onUpdateDeleteBookClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onUpdateDeleteAuthorClick: () -> Unit,
    onAddAuthorClick: () -> Unit,
    isAuthenticated: Boolean,
    enterPassword : (String) -> Unit,
){
    LazyColumn (
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium)
            )
    ){
        items(1) { // Sử dụng `items`  lặp lại một số lượng card nhất định
            InfoAppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    )
            )
        }

        item {
            PasswordProtectedAdminArea( // Gọi hàm bảo vệ bằng mật khẩu ở đây
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    ),
                onUpdateDeleteBookClick = onUpdateDeleteBookClick,
                onAddBookClick = onAddBookClick,
                onUpdateDeleteAuthorClick = onUpdateDeleteAuthorClick,
                onAddAuthorClick = onAddAuthorClick,
                isAuthenticated = isAuthenticated,
                enterPassword = enterPassword,
            )
        }
    }
}


@Composable
fun PasswordProtectedAdminArea(
    modifier: Modifier = Modifier,
    onUpdateDeleteBookClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onUpdateDeleteAuthorClick: () -> Unit,
    onAddAuthorClick: () -> Unit,
    isAuthenticated: Boolean,
    enterPassword: (String) -> Unit
) {
    var showPasswordDialog by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    if (showPasswordDialog) {
        PasswordDialog(
            onDismiss = {
                showPasswordDialog = false
                isError = false
                        },
            isError = isError,
            onPasswordSubmit = { password ->
                enterPassword(password)
                if (isAuthenticated) {
                    isError = false
                } else {
                    isError = true
                }
            }

        )
    }

    if (isAuthenticated) {
        showPasswordDialog = false
        isError = false
        AdminArea(
            modifier = modifier,
            onUpdateDeleteBookClick = onUpdateDeleteBookClick,
            onAddBookClick = onAddBookClick,
            onUpdateDeleteAuthorClick = onUpdateDeleteAuthorClick,
            onAddAuthorClick = onAddAuthorClick
        )
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showPasswordDialog = true }) {
                Text(stringResource(R.string.access_admin))
            }
        }
    }
}

@Composable
fun PasswordDialog(
    onDismiss: () -> Unit,
    onPasswordSubmit: (String) -> Unit,
    isError: Boolean
) {
    var password by remember { mutableStateOf("") }
    var label = if (isError) R.string.wrong_password else R.string.enter_password
    val isPasswordVisible = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = { onPasswordSubmit(password) }) {
                Text(stringResource(R.string.submit_button))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        },
        text = {
            Column {
                Text(
                    text = stringResource(label),
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (isPasswordVisible.value) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }

                        IconButton(onClick = {
                            isPasswordVisible.value = !isPasswordVisible.value
                        }) {
                            Icon(imageVector = image, contentDescription = if (isPasswordVisible.value) {
                                stringResource( R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            })
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, // Bàn phím số
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onPasswordSubmit(password) // Gọi hàm khi nhấn "Done"
                        }
                    )
                )
            }
        }
    )
}


@Composable
fun InfoAppCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
        colors =  CardColorsCustom(),
        shape = RoundedCornerShape(16.dp), // Hinh dang
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(id = R.string.App_infomations),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentWidth(), //tu dong xuong dong,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )
            Text(
                text = stringResource(id = R.string.Course_Name),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.Mentor),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            

            Text(
                text = stringResource(id = R.string.Developer),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.StudentId),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.App_version),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider()
            Text(
                text = stringResource(id = R.string.Data_source),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AdminArea(
    modifier: Modifier = Modifier,
    onUpdateDeleteBookClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onUpdateDeleteAuthorClick: () -> Unit,
    onAddAuthorClick: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
        shape = RoundedCornerShape(16.dp), // Hinh dang
        colors = CardColorsCustom()
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(id = R.string.Admin_areas),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentWidth(), // Tự động xuống dòng
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            AdminActionItem(
                textResId = R.string.Add_new_Book,
                onClick = onAddBookClick
            )

            AdminActionItem(
                textResId = R.string.Update_Delete_Book,
                onClick = onUpdateDeleteBookClick
            )

            AdminActionItem(
                textResId = R.string.Add_new_Author,
                onClick = onAddAuthorClick
            )

            AdminActionItem(
                textResId = R.string.Update_Delete_Author,
                onClick = onUpdateDeleteAuthorClick
            )

        }
    }

}


@Composable
fun AdminActionItem(
    textResId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            text = stringResource(id = textResId),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
    HorizontalDivider()
}

