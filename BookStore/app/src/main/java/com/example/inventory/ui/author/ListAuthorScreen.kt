package com.example.inventory.ui.author

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.HomeBookBodyLazyrow
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.CardColorsCustom


object ListAuthorScreenDestination : NavigationDestination {
    override val route = "list author"
    override val titleRes = R.string.List_authors
    override val icon = Icons.Default.Menu
    override val buttonText = R.string.List_authors
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAuthorScreen (
    navigateUp: () -> Unit,
    navigateToEditAuthor: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListAuthorViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val authorUiState by viewModel.authorsUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
            Column {
                InventoryTopAppBar(
                    title = stringResource(id = ListAuthorScreenDestination.titleRes),
                    canNavigateBack = true,
                    scrollBehavior = scrollBehavior,
                    navigateUp = navigateUp,
                )
            }
        },
    ) { innerPadding ->
        AuthorBody(
            authorList = authorUiState.authorList,
            onItemClick = navigateToEditAuthor,
            contentPadding = innerPadding,
            modifier = modifier.fillMaxSize()
        )
    }
}


@Composable
fun AuthorBody(
    authorList: List<Author>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    contentPadding: PaddingValues
){
    Column (
        modifier = modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        if (authorList.isEmpty()){
            Text(
                text = stringResource(R.string.no_author_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
            )
        }else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                contentPadding = contentPadding
            ){
                items(items = authorList, key = {it.id}){author ->
                    CardAuthor(
                        author = author,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small)) //padding xung quanh moi card
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable { onItemClick(author.id) }
                    )
                }
            }
        }
    }

}


@Composable
fun CardAuthor (
    author: Author,
    modifier: Modifier = Modifier
){

        Card (
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
            shape = RoundedCornerShape(16.dp),
            colors = CardColorsCustom()
        ){
            Column (
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = author.name,
                    style = MaterialTheme.typography.bodyLarge,
                    //textAlign = TextAlign.Center
                )
            }

        }

}