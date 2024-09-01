package com.example.inventory.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.info.SettingsDestination
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchScreenDestination

object LibraryDestination : NavigationDestination {
    override val route = "Library"
    override val titleRes = R.string.Library
    override val icon = Icons.Filled.Menu
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, id cua item
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel (factory = AppViewModelProvider.Factory)
) {

    val bookSaveUiState by viewModel.BooksSaveUiState.collectAsState()
    val authorUiState by viewModel.AuthorsUiState.collectAsState()
    /*
    LaunchedEffect (viewModel.BooksSaveUiState.collectAsState()){
        bookSaveUiState = viewModel.BooksSaveUiState.collectAsState()
    }

     */

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            InventoryTopAppBar(
                title = stringResource(LibraryDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        HomeBookBodyLazyColumn(
            bookList = bookSaveUiState.bookList,
            authorList = authorUiState.authorList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}