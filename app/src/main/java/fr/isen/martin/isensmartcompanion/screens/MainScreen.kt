package fr.isen.martin.isensmartcompanion.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.martin.isensmartcompanion.R
import fr.isen.martin.isensmartcompanion.ai.GeminiViewModel
import fr.isen.martin.isensmartcompanion.data.ChatExchanges
import fr.isen.martin.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import java.text.SimpleDateFormat
import java.util.*
import fr.isen.martin.isensmartcompanion.utils.formatDate


@Composable
fun MainScreen(innerPadding: PaddingValues, viewModel: GeminiViewModel = viewModel()) {
    val context = LocalContext.current
    var userInput by remember { mutableStateOf("") }
    val chatHistory by viewModel.chatHistory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.isen),
            contentDescription = context.getString(R.string.isen_logo)
        )
        Text(context.getString(R.string.app_name))

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(chatHistory) { exchange ->
                ExchangeCard(exchange)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        ) {
            TextField(
                value = userInput,
                onValueChange = { newValue -> userInput = newValue },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                modifier = Modifier.weight(1F)
            )

            OutlinedButton(
                onClick = {
                    if (userInput.isNotBlank()) {
                        viewModel.generateTextResponse(userInput) { response ->
                            userInput = ""
                        }
                    } else {
                        Toast.makeText(context, "Veuillez entrer un message", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .background(Color.Red, shape = RoundedCornerShape(50))
            ) {
                Image(painterResource(R.drawable.send), contentDescription = "")
            }
        }
    }
}

@Composable
fun ExchangeCard(exchange: ChatExchanges) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👤 Utilisateur : ${exchange.userMessage}", style = MaterialTheme.typography.bodyLarge)
            Text("🤖 IA : ${exchange.aiResponse}", style = MaterialTheme.typography.bodyMedium)
            Text("📅 ${formatDate(exchange.timestamp)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        MainScreen(PaddingValues(8.dp))
    }
}
