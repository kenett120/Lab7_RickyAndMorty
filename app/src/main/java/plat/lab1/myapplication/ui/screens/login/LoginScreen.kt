package plat.lab1.myapplication.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import plat.lab1.myapplication.R

@Composable
fun LoginScreen(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(
                id = R.drawable.rick_and_morty_logo
            ),
            contentDescription = "Logo de Rick y Morty",
            modifier = Modifier
                .width(300.dp)
                .height(150.dp),
            contentScale = ContentScale.Fit
            )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Empezar",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Kenett Alexander Ortega Cerón\nCarné: 25777",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}