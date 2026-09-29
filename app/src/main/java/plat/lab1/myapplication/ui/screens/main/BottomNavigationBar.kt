package plat.lab1.myapplication.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Characters"
                )
            },
            label = {
                Text("Characters")
            }
        )

        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Locations"
                )
            },
            label = {
                Text("Locations")
            }
        )

        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}