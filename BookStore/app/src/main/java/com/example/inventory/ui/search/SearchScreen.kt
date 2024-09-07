package com.example.inventory.ui.search

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.data.Book
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.book.BookTypeData
import com.example.inventory.ui.book.SubjectData
import com.example.inventory.ui.home.BookCard
import com.example.inventory.ui.home.HomeBookBodyLazyColumn
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.home.groupBooksBySubject
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.CardColorsCustom
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

    //Lay du lieu tu viewModel
    val searchUiState by viewModel.searchUiState.collectAsState()
    val authorUiState by viewModel.AuthorsUiState.collectAsState()

    //Danh sach lua chon
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectType by viewModel.selectType.collectAsState()
    val selectAuthor by viewModel.selectAuthor.collectAsState()
    val selectSubject by viewModel.selectSubject.collectAsState()


    //Hien thi kieu chon
    val isTypeSearchVision = searchQuery.isEmpty() //khi nao by khi nao =

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())



    //Danh sach tac gia
    val listAuthors = authorUiState.authorList.map { it.name }
    val listAuthorWithAll= listOf("All") + listAuthors


    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                InventoryTopAppBar(
                    title = stringResource(SearchScreenDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
                SearchBarHome(
                    searchQuery = searchQuery,
                    viewModel = viewModel,
                )
            }
        },
    ) { innerPadding ->
            if (isTypeSearchVision){
                TypeSearchChip(
                    typeSelect = selectType,
                    authorSelect = selectAuthor,
                    subjectSelect = selectSubject,
                    onSelectAuthor = {viewModel.updateSelectAuthor(it)},
                    onSelectSubject = {viewModel.updateSelectSubject(it)},
                    onSelectType = { viewModel.updateSelectType(it) },
                    listAuthor = listAuthorWithAll,
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
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
){

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            viewModel.updateSearchQuery(it)
            viewModel.updateAdminSearchQuery(it)
                        },
        label = {
            Text(text = stringResource(R.string.search) + " " + stringResource(id = R.string.By) + " " + stringResource(R.string.Book_name)) },
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
fun TypeSearchChip (
    typeSelect: String,
    authorSelect: String,
    subjectSelect: String,
    listAuthor: List<String>,
    onSelectAuthor: (String) -> Unit,
    onSelectSubject: (String) -> Unit,
    onSelectType: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val listSubject = listOf("All") + SubjectData.listSubject
    val listBookType = listOf("All") + BookTypeData.listBookType

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
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //Select subject
            item("subject") {
                AssistFilterListChip(
                    selectItem = subjectSelect,
                    modifier = Modifier,
                    onValueChange = { onSelectSubject(it) },
                    listSelect = listSubject,
                    label = stringResource(id = R.string.Subject)
                )
            }

            //Selec type book
            item("typeBook") {
                AssistFilterListChip(
                    selectItem = typeSelect,
                    modifier = Modifier,
                    onValueChange = { onSelectType(it) },
                    listSelect = listBookType,
                    label = stringResource(id = R.string.Book_type)
                )
            }

            //Select author
            item("author"){
                AssistFilterListChip(
                    selectItem = authorSelect,
                    modifier = Modifier,
                    onValueChange = { onSelectAuthor(it) },
                    listSelect = listAuthor,
                    label = stringResource(id = R.string.Author_name)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistFilterListChip (
    selectItem: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    listSelect: List<String>,
    label: String
){
    val all = stringResource(R.string.default_all)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    FilterChip(
        selected = (selectItem!="" && selectItem != "All"),
        onClick = {
            showBottomSheet = true
                  },
        label = {
            if (selectItem != "All" && selectItem != ""){

                if (label == stringResource(id = R.string.Author_name)){
                    Text(selectItem)
                }else{
                    Text(stringResource( resStringToSearchText( selectItem ) ) )
                }

            }else{
                Text(label+": "+ all )
            }
                },
        trailingIcon =  {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null

            )
        },
        modifier = modifier.padding(
            start = dimensionResource(R.dimen.padding_medium)
        )
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = Modifier
                .heightIn(max = 400.dp),
            dragHandle = null // Ẩn drag handle để không cho kéo
        ){
            BottomSheetContent(
                listSelect = listSelect,
                onClick = {
                    onValueChange(it)
                    showBottomSheet = false
                },
                selectItem = selectItem,
                label = label,
                isAuthor = (label == stringResource(id = R.string.Author_name))
            )
        }
    }
}



//Noi dung bottom sheet
@Composable
fun BottomSheetContent(
    listSelect: List<String>,
    onClick: (String) -> Unit,
    selectItem: String,
    label: String,
    isAuthor: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(dimensionResource(R.dimen.padding_medium)),
        ,verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_tiny)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Phần tiêu đề
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_medium),
                    start =  dimensionResource(R.dimen.padding_medium)
                )
                .align(Alignment.Start) //Canh trai
        )

        // Divider giữa tiêu đề và nội dung
        HorizontalDivider()
        // Phần nội dung bao quanh một hình có đường biên

        Card(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp)
                .padding(dimensionResource(R.dimen.padding_medium)),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = CardDefaults.outlinedCardBorder(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    listSelect.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (option != "All"){
                                        onClick(option)
                                    }else{
                                        onClick("")
                                    }
                                }
                                .background(
                                    if (option == selectItem) MaterialTheme.colorScheme.secondaryContainer
                                    else Color.Transparent
                                )
                                .padding(vertical = dimensionResource(R.dimen.padding_small)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isAuthor){
                                Text(
                                    text = (
                                        if (option == "All"){
                                            stringResource( resStringToSearchText( option ) )
                                        }else{
                                            option
                                        }
                                            ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(horizontal = dimensionResource(R.dimen.padding_tiny))
                                )
                            }else{
                                Text(
                                    text = stringResource( resStringToSearchText( option ) ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(horizontal = dimensionResource(R.dimen.padding_tiny))
                                )
                            }

                        }
                        HorizontalDivider()
                    }
            }

        }
    }
}



//Chuyen tu string sang stringRes, ngon ngu thay doi theo ung dung
@Composable
fun resStringToSearchText(text: String): Int {
    return when (text) {
        "Printed Book" -> R.string.book_type_printed
        "CD-ROM" -> R.string.book_type_cd_rom
        "Printed Thesis" -> R.string.book_type_thesis_printed
        "Digital Thesis" -> R.string.book_type_thesis_digital
        "Printed Report" -> R.string.book_type_report_printed
        "Digital Report" -> R.string.book_type_report_digital
        "E-Book" -> R.string.book_type_ebook
        "Printed Dissertation" -> R.string.book_type_dissertation_printed
        "Digital Dissertation" -> R.string.book_type_dissertation_digital
        "Thesis" -> R.string.book_type_thesis
        "Information Technology" -> R.string.subject_information_technology
        "Philosophy" -> R.string.subject_philosophy
        "Foreign Language" -> R.string.subject_foreign_language
        "Physical Education" -> R.string.subject_physical_education
        "Pedagogy" -> R.string.subject_pedagogy
        "Biotechnology" -> R.string.subject_biotechnology
        "Economics" -> R.string.subject_economics
        "Agriculture" -> R.string.subject_agriculture
        "Fisheries" -> R.string.subject_fisheries
        "Livestock" -> R.string.subject_livestock
        "Veterinary Medicine" -> R.string.subject_veterinary_medicine
        "Processing" -> R.string.subject_processing
        "Environment and Resources" -> R.string.subject_environment_and_resources
        "Tourism" -> R.string.subject_tourism
        "Law" -> R.string.subject_law
        "Construction" -> R.string.subject_construction
        "Other" -> R.string.subject_other
        else -> R.string.default_all // Mặc định về loại sách in nếu không khớp
    }
}







