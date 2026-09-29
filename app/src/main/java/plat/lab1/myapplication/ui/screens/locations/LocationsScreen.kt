package plat.lab1.myapplication.ui.screens.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import plat.lab1.myapplication.data.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    locations: List<Location>,
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations")
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(
                items = locations,
                key = { location -> location.id }
            ) { location ->

                LocationItem(
                    location = location,
                    onClick = {
                        onLocationClick(location.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
    ) {
        Text(
            text = location.name
        )

        Text(
            text = location.type,
            fontWeight = FontWeight.Bold
        )
    }
}