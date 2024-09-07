package com.example.inventory.ui.admins

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.data.Book
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.BookCard
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.home.groupBooksBySubject
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchBarHome
import com.google.accompanist.systemuicontroller.rememberSystemUiController


object AdminsScreenDestination : NavigationDestination {
    override val route = "Edit_Books"
    override val titleRes = R.string.book_edit_title
    override val icon = Icons.Default.Edit
    override val buttonText = R.string.book_edit_title
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminsScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, id cua item
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel (factory = AppViewModelProvider.Factory)
) {

    val searchUiState by viewModel.adminsSearchUiState.collectAsState() //Du lieu hien thi B2
    val searchQuery by viewModel.adminsSearchQuery.collectAsState() //Du lieu nhap vao tim kiem B1
    val authorUiState by viewModel.AuthorsUiState.collectAsState()

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = stringResource(AdminsScreenDestination.titleRes),
                    canNavigateBack = true,
                    scrollBehavior = scrollBehavior,
                    navigateUp = navigateBack
                )
                SearchBarHome(
                    searchQuery = searchQuery,
                    viewModel = viewModel,
                    )
            }
        },
    ) { innerPadding ->
            HomeBookBodyLazyColumn(
                bookList = searchUiState.bookList,
                authorList = authorUiState.authorList,
                onItemClick = navigateToItemUpdate,
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = innerPadding,
            )
    }
}


