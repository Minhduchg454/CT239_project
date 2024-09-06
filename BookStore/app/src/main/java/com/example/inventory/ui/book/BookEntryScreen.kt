package com.example.inventory.ui.book

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.inventory.InventoryApplication
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.author.AuthorEntryDestination
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.TextFieldColorsCustom
import kotlinx.coroutines.launch


object BookEntryDestination : NavigationDestination {
    override val route = "Add Book " //Nhap lieu cho item
    override val titleRes = R.string.book_add_title
    override val icon = Icons.Default.Add
    override val buttonText = R.string.book_add_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToAddAuthor: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: BookEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() } //Quan ly trang thai cua snackbar
    val keyboardController = LocalSoftwareKeyboardController.current // Dieu khien ban phim
    /*
    Bạn thường sử dụng rememberCoroutineScope() khi bạn cần chạy một coroutine để thực hiện các tác vụ không đồng bộ
    (như tải dữ liệu, lưu dữ liệu, hoặc thao tác trên cơ sở dữ liệu) trong một composable,
    và bạn muốn đảm bảo rằng coroutine này được hủy bỏ đúng cách khi composable bị hủy.
    */

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val saveSuccessMessage = stringResource(R.string.snackBar_save_books)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            InventoryTopAppBar(
                title = stringResource(BookEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onBookValueChange = viewModel::updateUiState,
            onSaveClick = {
                keyboardController?.hide()

                coroutineScope.launch { //Hoat dong ngam, khong anh huong den Flow chinh
                    viewModel.saveBook()
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = saveSuccessMessage
                    )
                    navigateBack() //lam moi man hinh nhap lieu
                }

            },
            modifier = Modifier
                .fillMaxSize(),
            navigateToAddAuthor = navigateToAddAuthor,
            contentPadding = innerPadding
        )
    }

}


@Composable
fun BookEntryBody(
    bookUiState: BookUiState,
    onBookValueChange: (BookDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddAuthor: () ->Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.padding(
            //top = dimensionResource(id = R.dimen.padding_medium),
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium),
        )
    ) {
        item {
            BookInputForm(
                bookDetails = bookUiState.bookDetails,
                onValueChange = onBookValueChange,
                modifier = Modifier.fillMaxWidth(),
                navigateToAddAuthor = navigateToAddAuthor
            )
            Button(
                onClick = onSaveClick,
                enabled = bookUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }
    }
}







// Định nghĩa biến màu cho các trường nhập liệu




@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookInputForm(
    bookDetails: BookDetails,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit = {},
    enabled: Boolean = true,
    navigateToAddAuthor: () -> Unit = {} // Thêm callback để điều hướng
) {
    //Danh sach cac tac gia da co
    val authorsRepository = (LocalContext.current.applicationContext as InventoryApplication).container.authorsRepository
    val authors by authorsRepository.getAllAuthorsStream().collectAsState(initial = emptyList())

    //Luu tru tac gia duoc chon, su dung remember do khong co co che tai cau truc nhu viewmodel
    var selectAuthor by remember { mutableStateOf<Author?>(null)
    }


    var selectTypeBook by remember { mutableStateOf("") }
    var selectSubject by remember { mutableStateOf("") }

    //Trang thai hien thi DropdownMenu
    var expanded by remember { mutableStateOf(false) }


    //Trang thai hien thi DropdownMenu
    var expandedTypeBook by remember { mutableStateOf(false) }

    //Trang thai hien thi DropdownMenu
    var expandedSubject by remember { mutableStateOf(false) }

    //Trang thai nguoi dung dang nhap tac gia
    var isCreatingAuthor by remember { mutableStateOf(false) }


    val listBookTypeRes = listOf(
        R.string.book_type_printed,
        R.string.book_type_cd_rom,
        R.string.book_type_thesis_printed,
        R.string.book_type_thesis_digital,
        R.string.book_type_report_printed,
        R.string.book_type_report_digital,
        R.string.book_type_ebook,
        R.string.book_type_dissertation_printed,
        R.string.book_type_dissertation_digital,
        R.string.book_type_thesis
    )



    val listSubjectRes = listOf(
        (R.string.subject_information_technology),
        (R.string.subject_philosophy),
        (R.string.subject_foreign_language),
        (R.string.subject_physical_education),
        (R.string.subject_pedagogy),
        (R.string.subject_biotechnology),
        (R.string.subject_economics),
        (R.string.subject_agriculture),
        (R.string.subject_fisheries),
        (R.string.subject_livestock),
        (R.string.subject_veterinary_medicine),
        (R.string.subject_processing),
        (R.string.subject_environment_and_resources),
        (R.string.subject_tourism),
        (R.string.subject_law),
        (R.string.subject_construction),
        (R.string.subject_other)
    )

    val context = LocalContext.current
    val listSubjectToString = listSubjectRes.map { context.getString(it) } //Chuyen doi de su dung trong compose khac, hoac gan du lieu
    val listBookTypeToString = listBookTypeRes.map { context.getString(it) }









    LaunchedEffect(bookDetails) {
        selectAuthor = authors.find { it.id == bookDetails.authorId }
        selectTypeBook = bookDetails.type
        selectSubject = bookDetails.subject
    }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded, //Trang thai hien thi DropdownMenu
            onExpandedChange = {   expanded = !expanded } //
        ) {
            /*Nhập tên tác giả */
            OutlinedTextField(
                value = selectAuthor?.name ?: "",
                onValueChange = {},
                label = {Text(stringResource(id = R.string.Author_name) + "*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                readOnly = true, // Để người dùng chỉ có thể chọn từ dropdown
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldColorsCustom()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                authors.forEach { author ->
                    DropdownMenuItem(
                        text = { Text(author.name) },
                        onClick = {
                            selectAuthor = author
                            onValueChange(bookDetails.copy(authorId = author.id))
                            expanded = false
                            isCreatingAuthor = false // Đảm bảo chuyển sang chọn tác giả hiện có
                        }
                    )
                }
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_new_author)) },
                    onClick = {
                        expanded = false
                        isCreatingAuthor = true // Chuyển sang trạng thái nhập tác giả mới
                        navigateToAddAuthor()
                    }
                )

            }
        }

        /*Nhập tên sách*/
        OutlinedTextField(
            value = bookDetails.name, //Hien thi trong truong nhap du lieu
            onValueChange = { onValueChange(bookDetails.copy(name = it)) }, //lambda cap nhat khi gia tri truong nhap thay doi
            label = { Text(stringResource(R.string.Book_name)+ "*") }, //nhan
            colors = TextFieldColorsCustom(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )



        /*Nhập thông tin xuất bản  */
        OutlinedTextField(
            value = bookDetails.publicationInfo, // Hiển thị trong trường nhập liệu cho thông tin xuất bản
            onValueChange = { onValueChange(bookDetails.copy(publicationInfo = it)) }, // Lambda cập nhật khi giá trị thay đổi
            label = { Text(stringResource(R.string.Publication_info)+ "*") }, // Nhãn
            colors = TextFieldColorsCustom(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        /*Nhập vị trí kệ sách */
        OutlinedTextField(
            value = bookDetails.shelfNumber, // Hiển thị trong trường nhập liệu cho số kệ
            onValueChange = { onValueChange(bookDetails.copy(shelfNumber = it)) }, // Lambda cập nhật khi giá trị thay đổi
            label = { Text(stringResource(R.string.Shelf_number)+ "*") }, // Nhãn
            colors = TextFieldColorsCustom(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )


        /*Nhập mô tả vật lý */
        OutlinedTextField(
            value = bookDetails.physicalDescription, // Hiển thị trong trường nhập liệu cho mô tả vật lý
            onValueChange = { onValueChange(bookDetails.copy(physicalDescription = it)) }, // Lambda cập nhật khi giá trị thay đổi
            label = { Text(stringResource(R.string.Physical_description)+ "*") }, // Nhãn
            colors = TextFieldColorsCustom(),
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(), // Thêm imePadding() để tránh bị bàn phím che,
            shape = RoundedCornerShape(16.dp),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
        )


            /*Nhập loại sách*/
            ExposedDropdownMenuBox(
                expanded = expandedTypeBook, //Trang thai hien thi DropdownMenu
                onExpandedChange = {   expandedTypeBook = !expandedTypeBook } //
            ) {
                OutlinedTextField(
                    value = selectTypeBook,
                    onValueChange = {},
                    label = {Text(stringResource(id = R.string.Book_type)+ "*")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTypeBook) },
                    readOnly = true, // Để người dùng chỉ có thể chọn từ dropdown
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldColorsCustom()
                )



                ExposedDropdownMenu(
                    expanded = expandedTypeBook,
                    onDismissRequest = { expandedTypeBook = false }
                ) {
                    listBookTypeToString.forEach { typeBook ->
                        DropdownMenuItem(
                            text = { Text(typeBook)},
                            onClick = {
                                selectTypeBook = typeBook
                                onValueChange(bookDetails.copy(type = typeBook))
                                expandedTypeBook = false
                            }
                        )
                    }

                }
            }


            /*Nhập chủ đề */
            ExposedDropdownMenuBox(
                expanded = expandedSubject, //Trang thai hien thi DropdownMenu
                onExpandedChange = {   expandedSubject = !expandedSubject } //
            ) {
                OutlinedTextField(
                    value = selectSubject,
                    onValueChange = {},
                    label = {Text(stringResource(id = R.string.Subject)+ "*")},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubject) },
                    readOnly = true, // Để người dùng chỉ có thể chọn từ dropdown
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldColorsCustom()
                )



                ExposedDropdownMenu(
                    expanded = expandedSubject,
                    onDismissRequest = { expandedSubject = false }
                ) {
                    listSubjectToString.forEach { typeSubject ->
                        DropdownMenuItem(
                            text = { Text( typeSubject )},
                            onClick = {
                                selectSubject = typeSubject
                                onValueChange(bookDetails.copy(subject = typeSubject))
                                expandedSubject = false
                            }
                        )
                    }

                }
            }

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}
