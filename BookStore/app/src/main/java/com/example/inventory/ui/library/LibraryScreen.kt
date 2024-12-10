package com.example.inventory.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.AUTHOR
import com.example.inventory.data.BOOK
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.BookCard
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.navigation.NavigationDestination

object LibraryDestination : NavigationDestination {
    override val route = "Library"
    override val titleRes = R.string.Library
    override val icon = Icons.Outlined.LibraryAdd
    override val buttonText = R.string.library_button
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, id cua item
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel (factory = AppViewModelProvider.Factory)
) {

    val bookSaveUiState by viewModel.BooksSaveUiState.collectAsState()
    val authorUiState by viewModel.AuthorsUiState.collectAsState()


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
        LibraryBodyLazyColumn(
            bookList = bookSaveUiState.bookList,
            authorList = authorUiState.authorList,
            onItemClick = navigateToItemUpdate,
            navigateToHomeScreen = navigateToHomeScreen,
            modifier = modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun LibraryBodyLazyColumn(
    bookList: List<BOOK>,
    authorList: List<AUTHOR>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navigateToHomeScreen: () -> Unit,
    contentPadding: PaddingValues,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
            ),
    ) {
        if (bookList.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.library_add),
                    contentDescription = "",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.save_book_description),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { navigateToHomeScreen() }) {
                    Text(stringResource(R.string.explore_button))
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = contentPadding,
            ) {
                items(items =  bookList, key = { it.BOOK_Id }) { book ->

                    val author = authorList.find { it.AUTHOR_Id == book.AUTHOR_Id } ?: AUTHOR(AUTHOR_Name = "Unknown")

                    //Chi thuc hien neu author khong phai la null, neu la null thi bo qua chay den bien khac
                    BookCard(
                        book = book,
                        author = author,
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(200.dp) // Thiết lập chiều rộng cụ thể
                            .height(150.dp) // Thiết lập chiều cao cụ thể
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onItemClick(book.BOOK_Id) }
                    )

                }
            }
        }
    }
}