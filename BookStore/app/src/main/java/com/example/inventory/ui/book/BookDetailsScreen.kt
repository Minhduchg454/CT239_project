package com.example.inventory.ui.book

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.AUTHOR
import com.example.inventory.data.BOOK
import com.example.inventory.data.BOOK_TYPE
import com.example.inventory.data.SUBJECT
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.admins.AdminsScreenDestination
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.ListSubjectScreen
import com.example.inventory.ui.library.LibraryDestination
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchScreenDestination

object BookDetailsDestination : NavigationDestination {
    override val route = "book_details"
    override val titleRes = R.string.book_detail_title
    override val icon = Icons.Default.Home
    const val bookIdArg = "bookId"
    val routeWithArgs = "$route/{$bookIdArg}"
    override val buttonText = R.string.book_detail_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navigateToEditBook: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    val bookDetailsuiState = viewModel.bookDetailsUiState.collectAsState() //Bien chua du lieu tu viewmodel
    val authorUiState by viewModel.AuthorsUiState.collectAsState()
    val bookTypeUiState by viewModel.BookTypesUiState.collectAsState()
    val subjectUiState by viewModel.SubjectsUiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bookId = navBackStackEntry?.arguments?.getInt(BookDetailsDestination.bookIdArg) ?: return

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    // Lấy màn hình trước đó từ backQueue
    val previousBackStackEntry = navController.previousBackStackEntry
    val previousRoute = previousBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(BookDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )

        }, floatingActionButton = {
            if(previousRoute == AdminsScreenDestination.route){
                FloatingActionButton(
                    onClick = {
                        navigateToEditBook(bookId)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary

                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_item_title),
                    )
                }
            }

        }, modifier = modifier.padding(top = 4.dp)
    ) { innerPadding ->
        BookDetailsBody(
            bookDetailsUiState = bookDetailsuiState.value,
            authorList = authorUiState.authorList,
            subjectList = subjectUiState.subjectList,
            bookTypeList = bookTypeUiState.bookTypeList,
            navController = navController,
            onSaveToLibrary = {
                viewModel.saveToLibrary() },
            onRemoveFromLibrary = {
                viewModel.removeFromLibrary()
                navigateBack()
                                  },
            onDelete = {
                viewModel.DeleteBook()
                navigateBack()
            },


            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun BookDetailsBody(
    modifier: Modifier = Modifier,
    bookDetailsUiState: BookDetailsUiState,
    authorList: List<AUTHOR>,
    subjectList: List<SUBJECT>,
    bookTypeList: List<BOOK_TYPE>,
    navController: NavController,
    onSaveToLibrary: () -> Unit,
    onRemoveFromLibrary: () -> Unit = {},
    onDelete: () -> Unit,
) {
    // Lấy màn hình trước đó từ backQueue
    val previousBackStackEntry = navController.previousBackStackEntry
    val previousRoute = previousBackStackEntry?.destination?.route

    // Lấy màn hình hiện tại
    val currentRoute = navController.currentDestination?.route
    val context = LocalContext.current


    val authorName = authorList.find { it.AUTHOR_Id == bookDetailsUiState.bookDetails.toBook().AUTHOR_Id }?.AUTHOR_Name ?: "Unknown Author"
    val subjectRes = subjectList.find { it.SUBJECT_Id == bookDetailsUiState.bookDetails.toBook().SUBJECT_Id }?.SUBJECT_Name ?: "Unknown Subject"
    val typeNameRes = bookTypeList.find { it.BT_Id == bookDetailsUiState.bookDetails.toBook().BT_Id } ?.BT_Name ?: "Unknown Type"

    val subjectName = stringResource(stringSubjectToResourceId(subjectRes))
    val typeName = stringResource(stringTypeToResourceId(typeNameRes))
    val bookName = stringResource(R.string.Book_name)
    val bookType = stringResource(R.string.Book_type)
    val author = stringResource(R.string.Author_name)
    val publicationInfo = stringResource(R.string.Publication_info)
    val shelfNumber = stringResource(R.string.Shelf_number)
    val physicalDescription = stringResource(R.string.Physical_description)
    val subject = stringResource(R.string.Subject)

    val shareContent = """
        $bookName: ${bookDetailsUiState.bookDetails.toBook().BOOK_Name}
        ${bookType}: ${typeName}
        ${author}: ${authorName} 
        ${publicationInfo}: ${bookDetailsUiState.bookDetails.toBook().Book_PublicationInfo}
        ${shelfNumber}: ${bookDetailsUiState.bookDetails.toBook().BOOK_ShelfNumber}
        ${physicalDescription}: ${bookDetailsUiState.bookDetails.toBook().BOOK_PhysicalDescription}
        ${subject}: ${subjectName}
        MFN: ${bookDetailsUiState.bookDetails.toBook().Book_MFN}
    """.trimIndent()

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }



        var enabledSaveBook by rememberSaveable { mutableStateOf(!bookDetailsUiState.bookDetails.saveToLibrary) }

        // Dam bao cap nhat du lieu moi nhat khi co thay doi ve du lieu do
        // Sử dụng LaunchedEffect để theo dõi sự thay đổi của bookDetailsUiState
        LaunchedEffect(bookDetailsUiState.bookDetails.saveToLibrary) {
            enabledSaveBook = !bookDetailsUiState.bookDetails.saveToLibrary
        }



        BookDetailsCard(
            book = bookDetailsUiState.bookDetails.toBook(),
            authorList = authorList,
            subjectList = subjectList,
            bookTypeList = bookTypeList,
            modifier = Modifier.fillMaxWidth()
        )

        if(
            previousRoute == HomeDestination.route ||
            previousRoute == ListSubjectScreen.routeWithArgs ||
            previousRoute == SearchScreenDestination.route
            ){
            Button(
                onClick = {
                    onSaveToLibrary ()
                    enabledSaveBook = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                enabled = enabledSaveBook
            ) {
                Text(stringResource(R.string.save_to_library))
            }
        }


        if (previousRoute == LibraryDestination.route){
            Button(
                onClick = {
                    enabledSaveBook = true
                    onRemoveFromLibrary()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                enabled = !enabledSaveBook
            ) {
                Text(stringResource(R.string.remove_from_library))
            }
        }

        if (previousRoute == AdminsScreenDestination.route){
            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
        }


        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }


        if (previousRoute == HomeDestination.route ||
            previousRoute == LibraryDestination.route ||
            previousRoute == ListSubjectScreen.routeWithArgs ||
            previousRoute == SearchScreenDestination.route){
            OutlinedButton(
                onClick = {
                    shareBookDetails(context,shareContent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary) // Tùy chỉnh viền
            ) {
                Text(stringResource(R.string.share), color = MaterialTheme.colorScheme.primary) // Tùy chỉnh màu chữ
            }
        }

    }
}




@Composable
fun BookDetailsCard(
    book: BOOK,
    authorList: List<AUTHOR>,
    subjectList: List<SUBJECT>,
    bookTypeList: List<BOOK_TYPE>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors()
    ) {
        val paddingRow = dimensionResource(id = R.dimen.padding_medium)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small)),

            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val authorName = authorList.find { it.AUTHOR_Id == book.AUTHOR_Id }?.AUTHOR_Name ?: "Unknown Author"
            val subjectName = subjectList.find { it.SUBJECT_Id == book.SUBJECT_Id }?.SUBJECT_Name ?: "Unknown Subject"
            val typeName = bookTypeList.find { it.BT_Id == book.BT_Id } ?.BT_Name ?: "Unknown Type"

            BookDetailsRow(
                labelResID = R.string.Book_name,
                bookDetail = book.BOOK_Name,
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )

            BookDetailsRow(
                labelResID = R.string.Book_type,
                bookDetail = stringResource(stringTypeToResourceId(typeName)),
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )

            BookDetailsRow(
                labelResID = R.string.Author_name,
                bookDetail = authorName,
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )



            BookDetailsRow(
                labelResID = R.string.Publication_info,
                bookDetail = book.Book_PublicationInfo,
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )

            BookDetailsRow(
                labelResID = R.string.Shelf_number,
                bookDetail = book.BOOK_ShelfNumber,
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )



            BookDetailsRow(
                labelResID = R.string.Physical_description,
                bookDetail = book.BOOK_PhysicalDescription,
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )

            BookDetailsRow(
                labelResID = R.string.Subject,
                bookDetail = stringResource(stringSubjectToResourceId(subjectName)),
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )

            BookDetailsRow(
                labelResID = R.string.MFN,
                bookDetail = book.Book_MFN.toString(),
                modifier = Modifier.padding(
                    horizontal = paddingRow
                )
            )
        }
    }
}



@Composable
fun BookDetailsRow(
    @StringRes labelResID: Int,
    bookDetail: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
    ) {
        Text(
            stringResource(labelResID),
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = bookDetail,
            modifier = Modifier
                .wrapContentWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}








@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}



fun shareBookDetails(
    context: Context,
    content: String,
) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, content)
    }

    // Khởi chạy Intent
    context.startActivity(
        Intent.createChooser(intent, context.getString(R.string.share_with))
    )
}




