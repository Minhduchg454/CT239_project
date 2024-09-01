package com.example.inventory.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.BookDetailsDestination
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchBarHome
import javax.security.auth.Subject

object ListSubjectScreen : NavigationDestination {
    override val route = "List_Subject"
    override val titleRes = R.string.Subject
    override val icon = Icons.Default.MoreVert
    const val subject = "subject"
    val routeWithArgs = "${route}/{$subject}"
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSubjectScreen(
    navigateToItemUpdate: (Int) -> Unit, //Di chuyen toi item da click, id cua item
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListSubjectViewModel = viewModel (factory = AppViewModelProvider.Factory)
) {

    val subjectUiState by viewModel.subjectUiState.collectAsState()
    val authorUiState by viewModel.authorsUiState.collectAsState()

    //Thiet lap cuon cho top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = viewModel.subjectKey,
                    canNavigateBack = true,
                    navigateUp = navigateBack,
                    scrollBehavior = scrollBehavior
                )
            }
        },
    ) { innerPadding ->
        HomeBookBodyLazyColumn(
            bookList = subjectUiState.bookList,
            authorList = authorUiState.authorList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}