package com.example.inventory.ui.author

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.book.BookEditDestination
import com.example.inventory.ui.book.BookEditViewModel
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object AuthorEditDestination : NavigationDestination {
    override val route = "author_edit"
    override val titleRes = R.string.edit_author
    override val icon = Icons.Default.Edit
    const val authorIdArg = "authorId"
    val routeWithArgs = "$route/{$authorIdArg}"
    override val buttonText = R.string.edit_author
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorEditScreen (
    navigateBack: () -> Unit, //Lam moi
    onNavigateUp: () -> Unit, //Tro lai man hinh truoc do
    modifier: Modifier = Modifier,
    viewModel: AuthorEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
){
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    // val authorUiState by viewModel.authorEditUiState.collectAsState()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(AuthorEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { innerPadding ->
        AuthorBody(
            authorUiState = viewModel.authorEditUiState,
            onValuesChange = {newValue -> viewModel.updateUiState(newValue)},
            onSaveClick = {
                coroutineScope.launch {
                        viewModel.updateAuthor()
                        navigateBack()
                }
                          },
            onDeleteClick = {
                coroutineScope.launch {
                    viewModel.deleteAuthor()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding),
            isAdmins = true
        )

    }
}