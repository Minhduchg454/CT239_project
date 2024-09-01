package com.example.inventory.ui.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Author
import com.example.inventory.data.Book
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

object SettingsDestination : NavigationDestination {
    override val route = "Settings"
    override val titleRes = R.string.Settings
    override val icon = Icons.Default.Settings
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onUpdateDeleteClick : () -> Unit,
    onAddBookClick : () -> Unit
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { //Thanh bar tren cung ~ tieu de
                InventoryTopAppBar(
                    title = stringResource(SettingsDestination.titleRes),
                    canNavigateBack = false,
                    scrollBehavior = scrollBehavior
                )
        },
    ) { innerPadding ->
        SettingsBody(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
            onUpdateDeleteClick = onUpdateDeleteClick,
            onAddBookClick = onAddBookClick
        )
    }
}

@Composable
fun SettingsBody (
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onUpdateDeleteClick: () -> Unit,
    onAddBookClick: () -> Unit
){
    LazyColumn (
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ){
        items(1) { // Sử dụng `items`  lặp lại một số lượng card nhất định
            InfoAppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_medium)
                    )
            )
        }

        items(1) { // Sử dụng `items`  lặp lại một số lượng card nhất định
            AdminArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_medium)
                    ),
                onUpdateDeleteClick = onUpdateDeleteClick,
                onAddBookClick = onAddBookClick
            )
        }
    }
}





@Composable
fun InfoAppCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
        shape = RoundedCornerShape(16.dp), // Hinh dang
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(id = R.string.App_infomations),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentWidth(), //tu dong xuong dong,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )

            Text(
                text = stringResource(id = R.string.Course_Name),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = R.string.Mentor),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = R.string.Developer),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = R.string.StudentId),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = R.string.App_version),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = R.string.Data_source),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AdminArea(
    modifier: Modifier = Modifier,
    onUpdateDeleteClick: () -> Unit,
    onAddBookClick: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), //thiet lap do cao co cad
        shape = RoundedCornerShape(16.dp), // Hinh dang
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = stringResource(id = R.string.Admin_areas),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .wrapContentWidth(), // Tự động xuống dòng
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row (
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_tiny)
                    )
                    .fillMaxWidth()
                    .clickable { onAddBookClick() }
            ) {
                Text(
                    text = stringResource(id = R.string.Add_new_Book),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .wrapContentWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_tiny)
                    )
                    .fillMaxWidth()
                    .clickable { onUpdateDeleteClick() }
            ){
                Text(
                    text = stringResource(id = R.string.Update_Delete_Book),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .wrapContentWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }



        }
    }

}







/*
@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    InventoryTheme {
        SettingsScreen()
    }
}

 */