package com.example.inventory.ui.author

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.book.BookInputForm
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object AuthorEntryDestination : NavigationDestination {
    override val route = "author entry"
    override val titleRes = R.string.add_new_author
    override val icon = Icons.Default.Edit
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAuthorScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthorEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = stringResource(AuthorEntryDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
            }
        },
    ) { innerPadding ->

        AuthorBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            authorUiState = viewModel.authorUiState ,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveAuthor()
                    navigateBack()
                }
            },
            onValuesChange = {newAuthorDetails ->
                viewModel.updateUiState(newAuthorDetails)
            }
        )
    }
}


//Noi dung hien thi
@Composable
fun AuthorBody(
    modifier: Modifier,
    authorUiState: AuthorUiState,
    onSaveClick: () -> Unit,
    onValuesChange: (AuthorDetails) -> Unit,
){

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        AuthorInputForm(
            authorDetails = authorUiState.authorDetails,
            onValuesChange = onValuesChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier,
            shape = MaterialTheme.shapes.small,
            enabled = authorUiState.isEntryValid,
        ) {
            Text(stringResource(R.string.save_action))
        }

    }

}


//Form nhap lieu
@Composable
fun AuthorInputForm (
    modifier: Modifier = Modifier,
    authorDetails: AuthorDetails,
    onValuesChange: (AuthorDetails) -> Unit,
){
    OutlinedTextField(
        value = authorDetails.name,
        onValueChange = {
            onValuesChange(authorDetails.copy(name = it))
        },
        label = { Text(stringResource(R.string.author_name_rq)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
    )
}