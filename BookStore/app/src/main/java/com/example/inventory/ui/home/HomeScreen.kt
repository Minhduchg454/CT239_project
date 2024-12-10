package com.example.inventory.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults.overscrollEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.AUTHOR
import com.example.inventory.data.BOOK
import com.example.inventory.data.BOOK_TYPE
import com.example.inventory.data.SUBJECT
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.book.stringSubjectToResourceId
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
    override val icon = Icons.Outlined.Home
    override val buttonText = R.string.home_button
}

/**
 * Entry route for Home screen
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemUpdate: (Int) -> Unit,
    navigateToListSubject: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel:  HomeViewModel = viewModel (factory = AppViewModelProvider.Factory),
) {
    //Nguon du lieu cap nhat lien tuc
    val bookUiState by viewModel.BooksUiState.collectAsState()
    val authorUiState by viewModel.AuthorsUiState.collectAsState()
    val subjectUiState by viewModel.SubjectsUiState.collectAsState()

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
            }
        },
    ) { innerPadding ->
        HomeBookBodyLazyrow(
            bookList = bookUiState.bookList,
            authorList = authorUiState.authorList,
            subjectList = subjectUiState.subjectList ,
            onItemClick = navigateToItemUpdate,
            modifier = modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
            navigateToListSubject = navigateToListSubject
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeBookBodyLazyrow(
    bookList: List<BOOK>,
    authorList: List<AUTHOR>,
    subjectList: List<SUBJECT>,
    onItemClick: (Int) -> Unit,
    navigateToListSubject: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val booksBySubject = groupBooksBySubject(bookList)
    if (bookList.isEmpty()){
        // Hiển thị thông báo nếu không có dữ liệu
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.no_book),
                contentDescription = "",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
            )
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }else{
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)) //Khoang cach tung item
        ) {
            booksBySubject.forEach { (subjectId, books) ->
                item {
                    val subject =subjectList.find { subjectId == it.SUBJECT_Id } ?: SUBJECT(SUBJECT_Name = "Unknown")
                    Row (
                        modifier =
                        Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_large)) // Padding đồng nhất với LazyRow
                            .padding(top = dimensionResource(id = R.dimen.padding_small))
                            .fillMaxWidth()
                            .clickable { navigateToListSubject(subject.SUBJECT_Id.toString()) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.Subject)
                                    + ": "
                                    + stringResource(stringSubjectToResourceId(subject.SUBJECT_Name)
                            ),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(id = R.string.More),
                            tint = MaterialTheme.colorScheme.primary
                        )

                    }

                }
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.padding_small))
                    ) {
                        items(items = books, key = { it.BOOK_Id }) { book ->
                            val author = authorList.find { it.AUTHOR_Id == book.AUTHOR_Id } ?: AUTHOR(AUTHOR_Name = "Unknown")
                            BookCard(
                                book = book,
                                author = author,
                                modifier = Modifier
                                    .width(200.dp) // Thiết lập chiều rộng cụ thể
                                    .height(150.dp) // Thiết lập chiều cao cụ thể
                                    .padding(dimensionResource(id = R.dimen.padding_small)) //Moi card cach nhau
                                    .clickable { onItemClick(book.BOOK_Id) }
                            )
                        }
                    }
                }
            }
        }
    }

}




@Composable
fun HomeBookBodyLazyColumn(
    bookList: List<BOOK>,
    authorList: List<AUTHOR>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
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
                    painter = painterResource(R.drawable.no_book),
                    contentDescription = "",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                )
                Text(
                    text = stringResource(R.string.no_item_description),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
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

/*
    Nhóm sách theo chủ đề
 */
fun groupBooksBySubject(books: List<BOOK>): Map<Int?, List<BOOK>> {
    return books.groupBy { it.SUBJECT_Id }
}


@Composable
fun BookCard(
    book: BOOK,
    author: AUTHOR,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {


            Text(
                text = book.BOOK_Name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentWidth(), //tu dong xuong dong,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )

            // Thêm khoảng trống giữa tên sách và thông tin bên dưới
            Spacer(modifier = Modifier.weight(1f))

            // Phần tác giả và số kệ ở dưới cùng
            Column {
                Text(
                    text = author.AUTHOR_Name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.BOOK_ShelfNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}
