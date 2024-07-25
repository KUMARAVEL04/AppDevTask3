package com.example.apitestcompose.ui.theme

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.apitestcompose.R

@Composable
fun BottomNavigation() {

    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Tasks
    )

    NavigationBar{
        items.forEach { item ->
            AddItem(
                screen = item
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem
) {
    NavigationBarItem(
        // Text that shows bellow the icon
        label = {
            Text(text = screen.title)
        },

        // The icon resource
        icon = {
            Icon(
                painterResource(id = screen.icon),
                contentDescription = screen.title
            )
        },

        // Display if the icon it is select or not
        selected = true,

        // Always show the label bellow the icon or not
        alwaysShowLabel = true,

        // Click listener for the icon
        onClick = { /*TODO*/ },

        // Control all the colors of the icon
        colors = NavigationBarItemDefaults.colors()
    )
}

sealed class BottomNavItem(
    var title: String,
    var icon: Int
) {
    object Profile :
        BottomNavItem(
            "Home",
            R.drawable.user_nav
        )

    object Tasks :
        BottomNavItem(
            "List",
            R.drawable.tasks_nav
        )

}