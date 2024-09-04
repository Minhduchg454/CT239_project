package com.example.inventory.ui.book

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.author.AuthorEntryDestination
import com.example.inventory.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object BookEditDestination : NavigationDestination {
    override val route = "book_edit"
    override val titleRes = R.string.book_edit_title
    override val icon = Icons.Default.Edit
    const val bookIdArg = "bookId"
    val routeWithArgs = "$route/{$bookIdArg}"
    override val buttonText = R.string.book_edit_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {

    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(BookEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookEditUiState,
            onBookValueChange = {newValue -> viewModel.updateUiState(newValue)}, //viewModel::updateUiState, // {newValue -> viewModel.updateUiState(newValue)}
            onSaveClick = { coroutineScope.launch {
                viewModel.updateBook()
                navigateBack()
            } },
            modifier = Modifier
                /*
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState()),

                 */
            ,navigateToAddAuthor = {
                navController.navigate(AuthorEntryDestination.route) // Điều hướng đến màn hình thêm tác giả
            },
            contentPadding = innerPadding
        )

    }

}