package com.example.inventory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventory.R.string
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.settings.SettingsDestination
import com.example.inventory.ui.library.LibraryDestination
import com.example.inventory.ui.navigation.InventoryNavHost
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.search.SearchScreenDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController

//Bo cuc giao dien cua ung dung.
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen (
    navController: NavHostController = rememberNavController()
){
    val screensList = listOf(
        HomeDestination,
        LibraryDestination,
        SearchScreenDestination,
        SettingsDestination,
    )

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
            screens = screensList ,
            modifier = Modifier
                .fillMaxWidth() // Chiếm toàn bộ chiều rộng
        )
    }
}


/*
    Thanh dieu huong duoi cung
*/
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    screens: List<NavigationDestination>,
    navController: NavHostController
){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination //Lay man hiinh hien tay


    // Kiem soat giao dien
    val systemUiController = rememberSystemUiController() //Kiem soat giao dien cua thanh trang thai, thanh dieu huong va thanh thanh noi dung
    val useDarkIcons = !isSystemInDarkTheme() //Kiem tra co phai giao dien toi hay khong

    // Màu của thanh điều hướng dưới
    val navigationBarColor = Color.Transparent //Chinh sua mau thanh dieu huong

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
        modifier = modifier,
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


/*
Dinh nghia moi muc trong thanh dieu huong duoc hien thi va phan hoi
 */
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
                contentDescription = screen.route,
                modifier = Modifier
                    .size(24.dp)
            )
        },
        label = {
            Text(
                text = stringResource(id = screen.buttonText),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        },
        modifier = Modifier
            .align(Alignment.CenterVertically),
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary, //Mau sac icon khi duoc chon
            unselectedIconColor = MaterialTheme.colorScheme.onSurface, //Mau sac icon khi khong duoc chon
            selectedTextColor = MaterialTheme.colorScheme.primary, //Mau sac text khi duoc chon
            unselectedTextColor = MaterialTheme.colorScheme.onSurface, //Mau sac text khi khong duoc chon
        ),
        alwaysShowLabel = true,
    )
}

/*
Thanh tiêu đề và điều hướng trên cùng
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
    val barColor = MaterialTheme.colorScheme.background

    // Cập nhật màu nền của thanh trạng thái
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = barColor,
            darkIcons = useDarkIcons
        )
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
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
            containerColor = barColor
        ),
    )
}