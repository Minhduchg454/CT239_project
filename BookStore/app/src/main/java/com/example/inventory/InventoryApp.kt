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

package com.example.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventory.R.string
import com.example.inventory.ui.book.BookEntryDestination
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.info.SettingsDestination
import com.example.inventory.ui.library.LibraryDestination
import com.example.inventory.ui.navigation.InventoryNavHost
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen (
    navController: NavHostController = rememberNavController()
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Nội dung chính của màn hình, chiếm toàn bộ không gian còn lại
        InventoryNavHost(
            navController = navController,
            modifier = Modifier
                .weight(1f) // Chiếm toàn bộ không gian còn lại
                .fillMaxSize()
        )

        
        // BottomNavigationBar được đặt cố định ở dưới cùng
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng
        )
    }

}


@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController){
    val screens = listOf(
        HomeDestination,
        LibraryDestination,
        SearchScreenDestination,
        SettingsDestination,
        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    // Kiem soat giao dien
    val systemUiController = rememberSystemUiController() //Kiem soat giao dien cua thanh trang thai, thanh dieu huong va thanh thanh noi dung
    val useDarkIcons = !isSystemInDarkTheme() //Kiem tra co phai giao dien tôi hay khong

    // Màu của thanh điều hướng dưới
    val navigationBarColor = Color.Transparent

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = useDarkIcons
        )
    }

    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(), // Đảm bảo Divider chiếm toàn bộ chiều rộng
        thickness = 1.dp, //do day duong phan tach
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f) // Sử dụng màu nhẹ hơn để phân tách

        /*
        .copy(alpha = 0.12f) tạo ra một phiên bản màu sắc với độ trong suốt nhẹ,
        có nghĩa là màu sẽ có độ trong suốt khoảng 12%
         */
    )


    NavigationBar (
        modifier = modifier.height(90.dp),
        containerColor = navigationBarColor
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

//Dinh nghia moi muc trong thanh dieu huong duoc hien thi va phan hoi
@Composable
fun RowScope.AddItem(
    screen: NavigationDestination,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    val isSelected = currentDestination?.route == screen.route

    NavigationBarItem(
        selected = currentDestination?.route == screen.route,
        onClick = {
            navController.navigate(screen.route)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.route
            )
        },
        label = {
            Text(
                text = screen.route,
                style = TextStyle(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )

        }
    )
}



/**
 * App bar to display title and conditionally display the back navigation.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateUp: () -> Unit = {}
) {
    // Lấy màu nền hiện tại của hệ thống
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    // Màu sắc cho thanh trạng thái và tiêu đề
    val statusBarColor = MaterialTheme.colorScheme.background
    val topAppBarColor = MaterialTheme.colorScheme.background

    // Cập nhật màu nền của thanh trạng thái
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
    }


    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 30.sp, // Kích thước chữ to hơn
                    fontWeight = FontWeight.Bold, // Chữ in đậm
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
            .padding(vertical = 4.dp),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = topAppBarColor
        ),

        )
}





/**
 * Top level composable that represents screens for the application.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryApp(navController: NavHostController = rememberNavController()) {
    InventoryNavHost(navController = navController)
}