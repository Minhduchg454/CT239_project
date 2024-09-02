package com.example.inventory.ui.search

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.data.Book
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.book.customTextFieldColors
import com.example.inventory.ui.home.BookCard
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.home.groupBooksBySubject
import com.example.inventory.ui.navigation.NavigationDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.reflect.full.memberProperties


object SearchScreenDestination : NavigationDestination {
    override val route = "Search"
    override val titleRes = R.string.search
    override val icon = Icons.Default.Search
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, id cua item
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel (factory = AppViewModelProvider.Factory)
) {

    val searchUiState by viewModel.searchUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val authorUiState by viewModel.AuthorsUiState.collectAsState()

    val typeSearchQuery by viewModel.typeSearchQuery.collectAsState()


    //Hien thi kieu chon
    val isTypeSearchVision = searchQuery.isEmpty() //khi nao by khi nao =

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = stringResource(SearchScreenDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
                SearchBarHome(
                    searchQuery = searchQuery,
                    viewModel = viewModel,
                    typeSearch = typeSearchQuery
                )
            }
        },
    ) { innerPadding ->
            if (isTypeSearchVision){
                TypeSearch(
                    onTypeClick = {
                        viewModel.updateTypeSearchQuery(it)
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }else{
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
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchBarHome(
    searchQuery: String,
    typeSearch: String,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
){


    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            viewModel.updateSearchQuery(it)
            viewModel.updateAdminSearchQuery(it)
                        }, // Truyền chuỗi tìm kiếm vào ViewModel
        label = {
            Text(text = stringResource(R.string.search) + " by " + typeSearch) },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 0.dp,
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium)
            )
            .height(60.dp),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { // Thêm biểu tượng tìm kiếm bên trái
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done // Thiết lập hành động Done cho bàn phím
        ),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp),
        colors = customTextFieldColors()
    )
}


@Composable
fun TypeSearch (
    onTypeClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    //val bookProperties = Book::class.memberProperties.map { it.name }

    val bookProperties = listOf(
        "Name",
        "Type",
        "Subject",
        "Author"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ){
        Text(
            text = stringResource(R.string.Type),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
        ) {
           items(bookProperties) { type ->
                ItemCard(
                    modifier = Modifier,
                    type = type,
                    onClick = { onTypeClick(it) },
                    diameter = 90.dp
                )
            }
        }

    }

}

@Composable
fun ItemCard (
    modifier: Modifier = Modifier,
    type: String,
    onClick: (String) -> Unit,
    diameter: Dp
){
    Card (
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .clickable { onClick(type) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = CircleShape, // Hinh dạng tròn
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ){
        Box(
            contentAlignment = Alignment.Center, // Căn giữa văn bản trong Card
            modifier = Modifier
                .size(diameter)
        ) {
            Text(
                text = type,
                //color = MaterialTheme.colorScheme.onPrimaryContainer, // Màu chữ
                fontSize = 14.sp, // Kích thước chữ
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}