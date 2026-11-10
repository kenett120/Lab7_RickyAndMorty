package plat.lab1.myapplication.ui.screens.characterform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import plat.lab1.myapplication.ui.viewmodel.CharacterFormState

@Composable
fun CharacterFormScreen(
    state: CharacterFormState,
    onNameChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onSpeciesChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onImageChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Crear o editar personaje")

        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Nombre") }
        )

        OutlinedTextField(
            value = state.status,
            onValueChange = onStatusChange,
            label = { Text("Estado") }
        )

        OutlinedTextField(
            value = state.species,
            onValueChange = onSpeciesChange,
            label = { Text("Especie") }
        )

        OutlinedTextField(
            value = state.gender,
            onValueChange = onGenderChange,
            label = { Text("Género") }
        )

        OutlinedTextField(
            value = state.image,
            onValueChange = onImageChange,
            label = { Text("URL de imagen") }
        )

        if (state.hasError) {
            Text("No se pudo guardar el personaje")
        }

        Button(
            onClick = onSaveClick,
            enabled = !state.isLoading && state.name.isNotBlank()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Guardar")
            }
        }
    }
}