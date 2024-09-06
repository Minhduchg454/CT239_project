package com.example.inventory.ui.search

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
    override val buttonText = R.string.search_button
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, truyen id cua item
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
                    typeSelect = typeSearchQuery,
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

    val type  = when (typeSearch) {
        "Name" -> stringResource(id = R.string.Book_name)
        "Type" -> stringResource(id = R.string.Book_type)
        "Subject" -> stringResource(id = R.string.Subject)
        "Author" -> stringResource(id = R.string.Author_name)
        else -> stringResource(id = R.string.Book_name)
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            viewModel.updateSearchQuery(it)
            viewModel.updateAdminSearchQuery(it)
                        }, // Truyền chuỗi tìm kiếm vào ViewModel
        label = {
            Text(text = stringResource(R.string.search) + " " + stringResource(id = R.string.By) + " " + type) },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 0.dp,
                bottom = 0.dp,
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium)
            )
            .height(60.dp), //Chieu cao thanh tim kiem
        shape = MaterialTheme.shapes.medium,
        leadingIcon = { // Thêm biểu tượng tìm kiếm bên trái
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done // Thiết lập hành động Done cho bàn phím
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedBorderColor = Color.Transparent
        )
    )
}


@Composable
fun TypeSearch (
    typeSelect: String,
    onTypeClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    //val bookProperties = Book::class.memberProperties.map { it.name }

    val bookName = stringResource(id = R.string.Book_name)
    val bookType = stringResource(id = R.string.Book_type)
    val subject = stringResource(id = R.string.Subject)
    val authorName = stringResource(id = R.string.Author_name)

    val bookProperties = listOf(
        bookName,
        bookType,
        subject,
        authorName
    )

    val context = LocalContext.current
    val typeName = getTypeName(typeSelect, context)


    Column(
        modifier = modifier
            .fillMaxSize()
    ){
        Text(
            text = stringResource(R.string.Options),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium)
            )
        )

        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
        ) {
           items(bookProperties) { type ->
                ItemCard(
                    modifier = Modifier,
                    type = type,
                    isSelect = ((type.equals(typeName, ignoreCase = true))),
                    onClick = { typeSelect ->
                        val typeQuery = when (typeSelect) {
                            bookName -> "Name"
                            bookType -> "Type"
                            subject -> "Subject"
                            authorName -> "Author"
                            else -> "Name"
                        }
                        onTypeClick(typeQuery)
                    },
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
    isSelect: Boolean
){
    val colorSelect = if(isSelect){
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }else{
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Card (
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_small))//Khoang cach giua card
            .clickable { onClick(type) },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.padding_tiny)),
        shape = MaterialTheme.shapes.large, // Hinh dạng tròn
        colors = colorSelect
    ){
        //Dat trong box de can giua card
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(dimensionResource(id =R.dimen.padding_medium))
        ) {
                if (isSelect){
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.Check),
                        modifier = modifier
                            .size(14.dp)
                    )
                }
            
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_tiny)))
            
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isSelect) FontWeight.Bold else FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }


    }
}


/*
    Để một hàm hoạt động mà không phải giao diện phải có context
    Hàm nhận một tham số context kiểu Context,
        +điều này cần thiết để lấy chuỗi từ tài nguyên.
 */
fun getTypeName(type: String, context: Context): String {
    return when (type) {
        "Name" -> context.getString(R.string.Book_name)
        "Type" -> context.getString(R.string.Book_type)
        "Subject" -> context.getString(R.string.Subject)
        "Author" -> context.getString(R.string.Author_name)
        else -> context.getString(R.string.Book_name) // Mặc định
    }
}