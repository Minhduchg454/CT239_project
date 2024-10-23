/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.admins.AdminsScreen
import com.example.inventory.ui.admins.AdminsScreenDestination
import com.example.inventory.ui.author.AddAuthorScreen
import com.example.inventory.ui.author.AuthorEditDestination
import com.example.inventory.ui.author.AuthorEditScreen
import com.example.inventory.ui.author.AuthorEntryDestination
import com.example.inventory.ui.author.ListAuthorScreen
import com.example.inventory.ui.author.ListAuthorScreenDestination
import com.example.inventory.ui.book.BookEditDestination
import com.example.inventory.ui.book.BookEditScreen
import com.example.inventory.ui.book.BookEntryDestination
import com.example.inventory.ui.book.BookEntryScreen
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.home.ListSubjectScreen
import com.example.inventory.ui.settings.SettingsDestination
import com.example.inventory.ui.settings.SettingsScreen
import com.example.inventory.ui.book.BookDetailsDestination
import com.example.inventory.ui.book.BookDetailsScreen
import com.example.inventory.ui.library.LibraryDestination
import com.example.inventory.ui.library.LibraryScreen
import com.example.inventory.ui.search.SearchScreen
import com.example.inventory.ui.search.SearchScreenDestination

/**
 * Provides Navigation graph for the application.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {

        //Home
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                },
                navigateToListSubject = {
                    navController.navigate("${ListSubjectScreen.route}/${it}")
                }

                /*
                Ví dụ, nếu ItemDetailsDestination.route là "item_details" và it là 42,
                thì đường dẫn điều hướng sẽ trở thành "item_details/42".
                 */
            )
        }

        //List Subject
        composable(
            route = ListSubjectScreen.routeWithArgs,
            arguments = listOf(navArgument(ListSubjectScreen.subject) {
                type = NavType.StringType
            })
        ) {
            ListSubjectScreen(
                navigateToItemUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                                       },
                navigateBack = { navController.navigateUp() }
            )
        }

        //Book detail
        composable(
            route = BookDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(BookDetailsDestination.bookIdArg) {
                type = NavType.IntType
            })
        ) {
            BookDetailsScreen(
                navigateToEditBook = { navController.navigate("${BookEditDestination.route}/${it}") },
                navigateBack = { navController.navigateUp() },
                navController = navController
            )
        }

        //Book Edit
        composable(
            route = BookEditDestination.routeWithArgs,
            arguments = listOf(navArgument(BookEditDestination.bookIdArg) {
                type = NavType.IntType
            })
        ) {
            BookEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navController = navController
            )
        }

        //Search screen
        composable(route = SearchScreenDestination.route,
        ){
            SearchScreen(
                navigateToItemUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                }

                /*
                Ví dụ, nếu ItemDetailsDestination.route là "item_details" và it là 42,
                thì đường dẫn điều hướng sẽ trở thành "item_details/42".
                 */
            )
        }


        //Settings screen
        composable(
            route = SettingsDestination.route,
        ){
            SettingsScreen(
                onUpdateDeleteBookClick = {
                    navController.navigate(AdminsScreenDestination.route)
                },
                onAddBookClick = {
                    navController.navigate(BookEntryDestination.route)
                },
                onUpdateDeleteAuthorClick = {
                    navController.navigate(ListAuthorScreenDestination.route)
                }
                ,
                onAddAuthorClick = {
                    navController.navigate(AuthorEntryDestination.route)
                }
            )
        }


        //Admins screen
        composable(
            route = AdminsScreenDestination.route,
        ) {
            AdminsScreen(
                navigateToItemUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                },
                navigateBack = { navController.navigateUp() },
            )
        }

        composable(
            route = LibraryDestination.route,
        ){
            LibraryScreen(
                navigateToItemUpdate = {
                    navController.navigate("${BookDetailsDestination.route}/${it}")
                }
            )
        }


        //Book Entry Screen
        composable(
            route = BookEntryDestination.route,
        ){
            BookEntryScreen(
                navigateBack = { navController.navigate(BookEntryDestination.route) },
                onNavigateUp = {  navController.navigate(SettingsDestination.route)},
                navigateToAddAuthor = {navController.navigate(AuthorEntryDestination.route)},
            )
        }

        //Add author
        composable(route = AuthorEntryDestination.route,){
            AddAuthorScreen(
                onNavigateUp = {navController.navigateUp()}
            )
        }


        composable(route = ListAuthorScreenDestination.route,) {
            ListAuthorScreen(
                navigateUp = { navController.navigateUp() },
                navigateToEditAuthor = {
                    navController.navigate( "${AuthorEditDestination.route}/${it}" )
                }
            )
        }

        //Author Edit Screen
        composable(
            route = AuthorEditDestination.routeWithArgs,
            arguments = listOf(navArgument(AuthorEditDestination.authorIdArg){
                type = NavType.IntType
                })
            )
        {
            AuthorEditScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.navigate(ListAuthorScreenDestination.route) }
            )
        }


    }
}

