package plat.lab1.myapplication.ui.screens.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import plat.lab1.myapplication.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    var onlyFavorites by rememberSaveable {
        mutableStateOf(false)
    }

    val visibleCharacters = if (onlyFavorites) {
        characters.filter { it.isFavorite }
    } else {
        characters
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Checkbox(
                    checked = onlyFavorites,
                    onCheckedChange = { onlyFavorites = it }
                )

                Text("Mostrar solo favoritos")
            }

            if (visibleCharacters.isEmpty()) {
                Text(
                    text = "No hay elementos a mostrar",
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = visibleCharacters,
                        key = { it.id }
                    ) { character ->

                        Card(
                            onClick = {
                                onCharacterClick(character.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = character.image,
                                    contentDescription = character.name,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = character.name,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text("Especie: ${character.species}")
                                    Text("Estado: ${character.status}")
                                    Text("Género: ${character.gender}")
                                }

                                IconButton(
                                    onClick = {
                                        onFavoriteClick(character)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (character.isFavorite) {
                                            Icons.Filled.Star
                                        } else {
                                            Icons.Outlined.StarBorder
                                        },
                                        contentDescription = "Favorito",
                                        tint = if (character.isFavorite) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}